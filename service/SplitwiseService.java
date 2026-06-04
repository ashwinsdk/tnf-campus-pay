package service;

import config.DatabaseConfig;
import exception.InvalidInputException;
import model.split_expense.ExpenseSplit;
import model.split_expense.GroupExpense;
import repository.ExpenseSplitRepository;
import repository.GroupExpenseRepository;
import repository.GroupMemberRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SplitwiseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SplitwiseService.class);

    private final GroupExpenseRepository groupExpenseRepo;
    private final GroupMemberRepository groupMemberRepo;
    private final ExpenseSplitRepository expenseSplitRepo;
    private final WalletService wallet;

    public SplitwiseService(GroupExpenseRepository groupExpenseRepo,
                            GroupMemberRepository groupMemberRepo,
                            ExpenseSplitRepository expenseSplitRepo) {
        this.groupExpenseRepo = groupExpenseRepo;
        this.groupMemberRepo = groupMemberRepo;
        this.expenseSplitRepo = expenseSplitRepo;
        wallet = new WalletService();
    }

    public int createExpenseGroup(String groupName, double totalAmount, int createdBy, List<Integer> members) throws InvalidInputException {

        if (groupName == null || groupName.trim().isEmpty()) {
            throw new InvalidInputException("Group creation failed: Group name cannot be blank.");
        }
        if (totalAmount <= 0) {
            throw new InvalidInputException("Group creation failed: Expense amount must be a positive value.");
        }
        if (members == null || members.isEmpty()) {
            throw new InvalidInputException("Group creation failed: You must include at least one campus student member.");
        }

        List<Integer> participants = new ArrayList<>();
        for (int studentId : members) {
            if (!participants.contains(studentId)) {
                participants.add(studentId);
            }
        }
        if (!participants.contains(createdBy)) {
            participants.add(createdBy);
        }

        GroupExpense group = GroupExpense.builder()
                .groupName(groupName)
                .totalAmount(totalAmount)
                .createdBy(createdBy)
                .build();

        int generatedGroupId = groupExpenseRepo.createGroup(group);
        if (generatedGroupId == -1) {
            System.err.println("[ERROR] Database processing failure. Group '" + groupName + "' was not saved.");
            return -1;
        }

        for (int studentId : participants) {
            groupMemberRepo.addMember(generatedGroupId, studentId);
        }

        int participantCount = participants.size();
        long totalPaise = Math.round(totalAmount * 100);
        long basePaise = totalPaise / participantCount;
        long remainderPaise = totalPaise % participantCount;

        for (int i = 0; i < participants.size(); i++) {
            int studentId = participants.get(i);
            if (studentId == createdBy) {
                continue;
            }
            long sharePaise = basePaise + (i < remainderPaise ? 1 : 0);
            ExpenseSplit split = ExpenseSplit.builder()
                    .groupId(generatedGroupId)
                    .studentId(studentId)
                    .amountOwed(sharePaise / 100.0)
                    .status("PENDING")
                    .build();
            expenseSplitRepo.createSplit(split);
        }

        // --- System outputs forced for user transparency ---
        System.out.println("\n--- ENGINE BREAKDOWN ---");
        System.out.println("Expense Group '" + groupName + "' calculated successfully [ID: " + generatedGroupId + "].");
        if (participantCount == 1) {
            System.out.println("Only the payer is in this group, so there are no dues to split.");
        } else {
            System.out.println("Individual Split Share: ₹" + String.format("%.2f", basePaise / 100.0)
                    + " (across " + participantCount + " participants)");
        }
        return generatedGroupId;
    }

    public void viewGroupBalances(int groupId) throws InvalidInputException {
        GroupExpense group = groupExpenseRepo.getGroupById(groupId);

        if (group == null) {
            throw new InvalidInputException("Lookup failed: Expense Group ID " + groupId + " does not exist in our campus tables.");
        }

        List<ExpenseSplit> splits = expenseSplitRepo.getSplitsByGroup(groupId);

        // --- Forced terminal console printing layout ---
        System.out.println("\n----------------------------------------------");
        System.out.println("CAMPUS BALANCE MONITOR FOR: " + group.getGroupName().toUpperCase());
        System.out.println("Total Paid: ₹" + group.getTotalAmount() + " | Settled By Student ID: " + group.getCreatedBy());
        System.out.println("-------------------------------------------------");

        System.out.println("Pending Campus Dues:");
        long pendingCount = splits.stream()
                .filter(split -> "PENDING".equalsIgnoreCase(split.getStatus()))
                .peek(split -> System.out.println("  [Split ID: " + split.getSplitId() + "] Student ID " + split.getStudentId() + " owes: ₹" + String.format("%.2f", split.getAmountOwed())))
                .count();

        if (pendingCount == 0) {
            System.out.println("   (No pending dues)");
        }

        System.out.println("\nCleared Ledger Accounts:");
        long paidCount = splits.stream()
                .filter(split -> "PAID".equalsIgnoreCase(split.getStatus()))
                .peek(split -> System.out.println(" Student ID " + split.getStudentId() + " has fully settled their share."))
                .count();

        if (paidCount == 0) {
            System.out.println("   (No cleared accounts yet)");
        }

        System.out.println("----------------------------------------------------\n");
    }

    public void settleExpense(int splitId) throws SQLException {

        Connection conn = DatabaseConfig.getConnection();
//        wallet.transfer(conn,
//                wallet.getWalletIdByStudId(conn, < FROM_STUDENT_ID>),
//                wallet.getWalletIdByStudId(conn, <TO_STUDENT_ID>),
//                "TRANSFER");

        expenseSplitRepo.markAsPaid(splitId);
        System.out.println("[LEDGER UPDATE] Settlement successfully completed for Split ID " + splitId + ".");
    }
}