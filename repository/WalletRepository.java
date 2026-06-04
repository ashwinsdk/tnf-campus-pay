package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WalletRepository {
    public boolean checkDuplicateWallet(Connection conn, int studentId) throws SQLException{
        String sql = "select student_id from wallet where student_id = ? ";
        try (PreparedStatement checkStudentId = conn.prepareStatement(sql)) {
            checkStudentId.setInt(1, studentId);
            try (ResultSet rs = checkStudentId.executeQuery()) {
                if (rs.next()) {
                    return false;
                }
            }
            catch (SQLException e){
                e.printStackTrace();
            }
        }
        return true;
    }

    public void createNewWallet(Connection conn, int walletId, int studentId, double amount) throws SQLException{
        String sql = "insert into wallet(id, student_id, balance) values (?, ?, ?);";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, walletId);
        ps.setInt(2, studentId);
        ps.setDouble(3, amount);
        int r = ps.executeUpdate();
        System.out.println(r +" rows updated");
    }
    public double getBalanceFromWallet (Connection conn, int id) throws SQLException{
        String sql = "select balance from wallet where id = ?";
        try (PreparedStatement retrieveBalance = conn.prepareStatement(sql)) {
            retrieveBalance.setInt(1, id);
            try (ResultSet rs = retrieveBalance.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
            catch (SQLException e){
                e.printStackTrace();
            }
        }
        return 0.0;
    }

    public void depositIntoDB(Connection conn, double amount, int toId) throws SQLException{
        String sql = "update wallet set balance = balance + ? where id = ?";

        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setDouble(1, amount);
        ps.setInt(2, toId);
        ps.executeUpdate();
        ps.close();
    }
    public void withdrawFromDB(Connection conn, double amount, int fromId) throws SQLException{
        String sql = "update wallet set balance = balance - ? where id = ?";

        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setDouble(1, amount);
        ps.setInt(2, fromId);
        ps.executeUpdate();
        ps.close();

        System.out.println("[SUCCESS] " + amount + " Debited");
    }
}
