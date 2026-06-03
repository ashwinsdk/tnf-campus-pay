package service;

import exception.InvalidInputException;
import model.ExpenseSplit;
import model.GroupExpense;
import repository.ExpenseSplitRepository;
import repository.GroupExpenseRepository;
import repository.GroupMemberRepository;

import java.util.List;

public class SplitwiseService {

    private final GroupExpenseRepository groupExpenseRepo;
    private final GroupMemberRepository groupMemberRepo;
    private final ExpenseSplitRepository expenseSplitRepo;

    // Dependency Injection constructor
    public SplitwiseService(GroupExpenseRepository groupExpenseRepo,
                            GroupMemberRepository groupMemberRepo,
                            ExpenseSplitRepository expenseSplitRepo) {
        this.groupExpenseRepo = groupExpenseRepo;
        this.groupMemberRepo = groupMemberRepo;
        this.expenseSplitRepo = expenseSplitRepo;
    }

    /**
     * Calculates the fair share splits and saves group metrics to MySQL tables.
     */
    public void createExpenseGroup(String groupName, double totalAmount, int createdBy, List<Integer> members) throws InvalidInputException {
        // 1. Core Rule Validations
        if (groupName == null || groupName.trim().isEmpty()) {
            throw new InvalidInputException("Group creation failed: Group name cannot be blank.");
        }
        if (totalAmount <= 0) {
            throw new InvalidInputException("Group creation failed: Expense amount must be a positive value.");
        }
        if (members == null || members.isEmpty()) {
            throw new InvalidInputException("Group creation failed: You must include at least one campus student member.");
        }

        // 2. Build and persist core group metadata
        GroupExpense group = GroupExpense.builder()
                .groupName(groupName)
                .totalAmount(totalAmount)
                .createdBy(createdBy)
                .build();

        int generatedGroupId = groupExpenseRepo.createGroup(group);
        if (generatedGroupId == -1) {
            System.out.println(" Database processing failure. Group was not saved.");
            return;
        }

        // 3. Populate group tracking records
        for (int studentId : members) {
            groupMemberRepo.addMember(generatedGroupId, studentId);
        }

        // 4. Process individual split math with Java Streams
        double fairShareAmount = totalAmount / members.size();


        members.stream()
                .filter(studentId -> studentId != createdBy)
                .forEach(studentId -> {
                    ExpenseSplit split = ExpenseSplit.builder()
                            .groupId(generatedGroupId)
                            .studentId(studentId)
                            .amountOwed(fairShareAmount)
                            .status("PENDING")
                            .build();
                    expenseSplitRepo.createSplit(split);
                });

        System.out.println(" Expense Group '" + groupName + "' calculated successfully [ID: " + generatedGroupId + "].");
        System.out.println("Individual Split Share: ₹" + String.format("%.2f", fairShareAmount));
    }


    public void viewGroupBalances(int groupId) throws InvalidInputException {
        GroupExpense group = groupExpenseRepo.getGroupById(groupId);

        // Reusing your custom exception for data verification rules
        if (group == null) {
            throw new InvalidInputException("Lookup failed: Expense Group ID " + groupId + " does not exist in our campus tables.");
        }

        List<ExpenseSplit> splits = expenseSplitRepo.getSplitsByGroup(groupId);

        System.out.println("\n----------------------------------------------");
        System.out.println("CAMPUS BALANCE MONITOR FOR: " + group.getGroupName().toUpperCase());
        System.out.println("Total Paid: ₹" + group.getTotalAmount() + " | Settled By Student ID: " + group.getCreatedBy());
        System.out.println("-------------------------------------------------");


        System.out.println("Pending Campus Dues:");
        splits.stream()
                .filter(split -> "PENDING".equalsIgnoreCase(split.getStatus()))
                .forEach(split -> System.out.println("   - Student ID " + split.getStudentId() + " owes: ₹" + String.format("%.2f", split.getAmountOwed())));

        System.out.println("\nCleared Ledger Accounts:");
        splits.stream()
                .filter(split -> "PAID".equalsIgnoreCase(split.getStatus()))
                .forEach(split -> System.out.println("   - Student ID " + split.getStudentId() + " has fully settled their share."));

        System.out.println("----------------------------------------------------\n");
    }

    /**
     * Marks a specific split balance ledger row as PAID.
     */
    public void settleExpense(int splitId) {
        expenseSplitRepo.markAsPaid(splitId);
    }
}