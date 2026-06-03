package repository.jdbc;

import config.DatabaseConfig;
import model.ExpenseSplit;
import repository.ExpenseSplitRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExpenseSplitRepositoryJdbc implements ExpenseSplitRepository {

    @Override
    public void createSplit(ExpenseSplit split) {
        String sql = "INSERT INTO expense_splits (group_id, student_id, amount_owed, status) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, split.getGroupId());
            ps.setInt(2, split.getStudentId());
            ps.setDouble(3, split.getAmountOwed());
            // Default to PENDING if status isn't explicitly set in the model
            ps.setString(4, split.getStatus() != null ? split.getStatus() : "PENDING");

            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("SQL Error creating expense split: " + e.getMessage());
            // Note: Make sure to hook this up to your team's FileLogger helper later!
        }
    }

    @Override
    public List<ExpenseSplit> getSplitsByGroup(int groupId) {
        List<ExpenseSplit> splits = new ArrayList<>();
        String sql = "SELECT * FROM expense_splits WHERE group_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, groupId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Uses your Lombok Builder strategy to rebuild the data objects
                    splits.add(ExpenseSplit.builder()
                            .splitId(rs.getInt("id"))
                            .groupId(rs.getInt("group_id"))
                            .studentId(rs.getInt("student_id"))
                            .amountOwed(rs.getDouble("amount_owed"))
                            .status(rs.getString("status"))
                            .build());
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error fetching splits by group ID: " + e.getMessage());
        }
        return splits;
    }

    @Override
    public void markAsPaid(int splitId) {
        String sql = "UPDATE expense_splits SET status = 'PAID' WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, splitId);
            ps.executeUpdate();

            System.out.println("Split ID " + splitId + " has been successfully marked as PAID.");
        } catch (SQLException e) {
            System.err.println("SQL Error marking split as paid: " + e.getMessage());
        }
    }
}