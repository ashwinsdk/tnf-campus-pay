package repository.jdbc;

import model.GroupExpense;
import repository.GroupExpenseRepository;

import java.util.List;



import config.DatabaseConfig;


import java.sql.*;
import java.util.ArrayList;


public class GroupExpenseRepositoryJdbc implements GroupExpenseRepository {

    @Override
    public int createGroup(GroupExpense group) {
        String sql = "INSERT INTO group_expenses (group_name, total_amount, created_by) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, group.getGroupName());
            ps.setDouble(2, group.getTotalAmount());
            ps.setInt(3, group.getCreatedBy());

            ps.executeUpdate();


            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error creating group expense: " + e.getMessage());

        }
        return -1; // Return -1 if insertion fails
    }

    @Override
    public GroupExpense getGroupById(int groupId) {
        String sql = "SELECT * FROM group_expenses WHERE group_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, groupId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Mapping row data to your Friend's Lombok Builder model
                    return GroupExpense.builder()
                            .groupId(rs.getInt("group_id"))
                            .groupName(rs.getString("group_name"))
                            .totalAmount(rs.getDouble("total_amount"))
                            .createdBy(rs.getInt("created_by"))
                            .createdAt(rs.getTimestamp("created_at"))
                            .build();
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error fetching group by ID: " + e.getMessage());
        }
        return null; // Return null if group isn't found
    }

    @Override
    public List<GroupExpense> getAllGroups() {
        List<GroupExpense> groups = new ArrayList<>();
        String sql = "SELECT * FROM group_expenses";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                groups.add(GroupExpense.builder()
                        .groupId(rs.getInt("group_id"))
                        .groupName(rs.getString("group_name"))
                        .totalAmount(rs.getDouble("total_amount"))
                        .createdBy(rs.getInt("created_by"))
                        .createdAt(rs.getTimestamp("created_at"))
                        .build());
            }
        } catch (SQLException e) {
            System.err.println("SQL Error fetching all groups: " + e.getMessage());
        }
        return groups;
    }
}