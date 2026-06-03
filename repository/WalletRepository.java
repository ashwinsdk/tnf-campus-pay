package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WalletRepository {
    public double getBalanceFromWallet (Connection conn, int id) throws SQLException{
        PreparedStatement retrieveBalance = conn.prepareStatement("select balance from wallet where id = ?");
        retrieveBalance.setInt(1, 101);
        ResultSet rs = retrieveBalance.executeQuery();
        if (rs.next()) {
            return rs.getDouble(1);
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
