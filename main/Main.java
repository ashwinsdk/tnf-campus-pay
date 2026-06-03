package main;

import config.DatabaseConfig;
import exception.InvalidInputException;
import repository.ExpenseSplitRepository;
import repository.GroupExpenseRepository;
import repository.GroupMemberRepository;
import repository.jdbc.ExpenseSplitRepositoryJdbc;
import repository.jdbc.GroupExpenseRepositoryJdbc;
import repository.jdbc.GroupMemberRepositoryJdbc;
import service.SplitwiseService;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("🚀 Starting CampusPay Splitwise Service Test Loop...\n");

        // 1. Initialize DB Infrastructure & Repositories
        GroupExpenseRepository groupRepo = new GroupExpenseRepositoryJdbc();
        GroupMemberRepository memberRepo = new GroupMemberRepositoryJdbc();
        ExpenseSplitRepository splitRepo = new ExpenseSplitRepositoryJdbc();

        // 2. Instantiate Your Custom Splitwise Service Class
        SplitwiseService splitwiseService = new SplitwiseService(groupRepo, memberRepo, splitRepo);

        // 3. Clear and Reset Database Tables for a Clean Test Environment
        prepareTestDatabase();

        try {
            // 4. Test Case 1: Create a Campus Expense Group Split
            System.out.println("--- Executing Test Case 1: Group Creation ---");
            String groupName = "Project Dinner";
            double totalBill = 800.0;
            int paidByStudentId = 101; // Keshav Sharma

            List<Integer> groupMembers = new ArrayList<>();
            groupMembers.add(101); // Keshav Sharma
            groupMembers.add(102); // Tharun P R
            groupMembers.add(103); // Vishal Sagar
            groupMembers.add(104); // Vijay S R

            // Triggers split math logic via Java Streams internally
            splitwiseService.createExpenseGroup(groupName, totalBill, paidByStudentId, groupMembers);
            System.out.println("---------------------------------------------\n");


            // 5. Test Case 2: View Balances Report (Reads directly back from MySQL tables)
            System.out.println("--- Executing Test Case 2: View Ledger Report ---");
            // The first generated group will have group_id = 1 due to auto-increment
            splitwiseService.viewGroupBalances(1);
            System.out.println("---------------------------------------------\n");


            // 6. Test Case 3: Settle a Student Debt Entry
            System.out.println("--- Executing Test Case 3: Settling Dues ---");
            // Tharun pays back his share. His entry in expense_split has split_id = 1
            int targetSplitId = 1;
            System.out.println("Student 102 pays back their share...");
            splitwiseService.settleExpense(targetSplitId);
            System.out.println("---------------------------------------------\n");


            // 7. Test Case 4: Verify Stream Bucketing Configuration Updates
            System.out.println("--- Executing Test Case 4: Re-Checking Report Post-Settlement ---");
            splitwiseService.viewGroupBalances(1);
            System.out.println("---------------------------------------------");

        } catch (InvalidInputException e) {
            System.out.println("⚠️ Test Failed due to business validation: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Unexpected execution failure: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Helper routine to wipe and format targeted tables so your tests don't clutter
     * auto-increments or throw duplicate key alerts on repeated compilation runs.
     */
    private static void prepareTestDatabase() {
        System.out.println("🧹 Formatting MySQL staging tables for testing...");

        String cleanSplits = "DELETE FROM expense_split";
        String cleanMembers = "DELETE FROM group_members";
        String cleanExpenses = "DELETE FROM group_expense";

        // Reset auto-increment counters back to 1
        String resetExpenseAI = "ALTER TABLE group_expense AUTO_INCREMENT = 1";
        String resetSplitAI = "ALTER TABLE expense_split AUTO_INCREMENT = 1";

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(cleanSplits);
            stmt.executeUpdate(cleanMembers);
            stmt.executeUpdate(cleanExpenses);
            stmt.executeUpdate(resetExpenseAI);
            stmt.executeUpdate(resetSplitAI);

            System.out.println("✅ Staging environment ready for execution.\n");

        } catch (SQLException e) {
            System.err.println("❌ Database cleaning step crashed: " + e.getMessage());
        }
    }
}