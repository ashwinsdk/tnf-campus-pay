package service;

import exception.InvalidInputException;
import model.ExpenseSplit;
import model.GroupExpense;
import repository.ExpenseSplitRepository;
import repository.GroupExpenseRepository;
import repository.GroupMemberRepository;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SplitwiseService {


    private static final Logger LOGGER = LoggerFactory.getLogger(SplitwiseService.class);

    private final GroupExpenseRepository groupExpenseRepo;
    private final GroupMemberRepository groupMemberRepo;
    private final ExpenseSplitRepository expenseSplitRepo;


    public SplitwiseService(GroupExpenseRepository groupExpenseRepo,
                            GroupMemberRepository groupMemberRepo,
                            ExpenseSplitRepository expenseSplitRepo) {
        this.groupExpenseRepo = groupExpenseRepo;
        this.groupMemberRepo = groupMemberRepo;
        this.expenseSplitRepo = expenseSplitRepo;
    }


    public void createExpenseGroup(String groupName, double totalAmount, int createdBy, List<Integer> members) throws InvalidInputException {

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
            LOGGER.error("Database processing failure. Group '{}' was not saved.", groupName);
            return;
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

        LOGGER.info("Expense Group '" + groupName + "' calculated successfully [ID: " + generatedGroupId + "].");
        if (participantCount == 1) {
            LOGGER.info("Only the payer is in this group, so there are no dues to split.");
        } else {
            LOGGER.info("Individual Split Share: ₹" + String.format("%.2f", basePaise / 100.0)
                    + " (across " + participantCount + " participants)");
        }
    }


    public void viewGroupBalances(int groupId) throws InvalidInputException {
        GroupExpense group = groupExpenseRepo.getGroupById(groupId);


        if (group == null) {
            throw new InvalidInputException("Lookup failed: Expense Group ID " + groupId + " does not exist in our campus tables.");
        }

        List<ExpenseSplit> splits = expenseSplitRepo.getSplitsByGroup(groupId);

        LOGGER.info("----------------------------------------------");
        LOGGER.info("CAMPUS BALANCE MONITOR FOR: " + group.getGroupName().toUpperCase());
        LOGGER.info("Total Paid: ₹" + group.getTotalAmount() + " | Settled By Student ID: " + group.getCreatedBy());
        LOGGER.info("-------------------------------------------------");

        LOGGER.info("Pending Campus Dues:");
        splits.stream()
                .filter(split -> "PENDING".equalsIgnoreCase(split.getStatus()))
                .forEach(split -> LOGGER.info("   - Student ID " + split.getStudentId() + " owes: ₹" + String.format("%.2f", split.getAmountOwed())));

        LOGGER.info("Cleared Ledger Accounts:");
        splits.stream()
                .filter(split -> "PAID".equalsIgnoreCase(split.getStatus()))
                .forEach(split -> LOGGER.info("   - Student ID " + split.getStudentId() + " has fully settled their share."));

        LOGGER.info("----------------------------------------------------");
    }


    public void settleExpense(int splitId) {
        expenseSplitRepo.markAsPaid(splitId);
        LOGGER.info("Settlement requested for split ID " + splitId + ".");
    }
}