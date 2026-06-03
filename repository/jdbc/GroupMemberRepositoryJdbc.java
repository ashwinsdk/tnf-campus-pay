package repository.jdbc;

import config.DatabaseConfig;
import repository.GroupMemberRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GroupMemberRepositoryJdbc implements GroupMemberRepository {

    @Override
    public void addMember(int groupId, int studentId) {
        String sql = "INSERT INTO group_members (group_id, student_id) VALUES (?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, groupId);
            ps.setInt(2, studentId);

            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("SQL Error adding member to group: " + e.getMessage());
            // Remember to hook this up to your FileLogger utility later!
        }
    }

    @Override
    public List<Integer> getMembersByGroup(int groupId) {
        List<Integer> memberIds = new ArrayList<>();
        String sql = "SELECT student_id FROM group_members WHERE group_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, groupId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Extracting the primitive integers directly as required by your interface method signature
                    memberIds.add(rs.getInt("student_id"));
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error fetching members by group ID: " + e.getMessage());
        }
        return memberIds;
    }
}