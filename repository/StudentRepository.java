package repository;

import exception.EntityNotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentRepository {

    public double getBalance(Connection conn,
                             int studentId)
            throws SQLException, EntityNotFoundException {

        String sql =
                "SELECT balance FROM students WHERE student_id = ?";

        try (PreparedStatement ps =
                     conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("balance");
                }
            }
        }

        throw new EntityNotFoundException(
                "Student not found with id: " + studentId);
    }

    public void updateBalance(Connection conn,
                              int studentId,
                              double newBalance)
            throws SQLException, EntityNotFoundException {

        String sql =
                "UPDATE students SET balance = ? WHERE student_id = ?";

        try (PreparedStatement ps =
                     conn.prepareStatement(sql)) {

            ps.setDouble(1, newBalance);
            ps.setInt(2, studentId);

            int rows = ps.executeUpdate();

            if (rows == 0) {
                throw new EntityNotFoundException(
                        "Student not found with id: " + studentId);
            }
        }
    }
}
