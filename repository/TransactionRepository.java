package repository;

import config.DatabaseConfig;
import model.Transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionRepository {

    public void saveTransaction(Connection conn,
                                int fromId, int toId, double amount, String type )
            throws SQLException {

        String sql = """
                INSERT INTO transactions
                (sender_id, receiver_id, amount, transaction_type)
                VALUES (?, ?, ?, ?)
                """;

        try (PreparedStatement ps =
                     conn.prepareStatement(sql)) {

            ps.setInt(1, fromId);
            ps.setInt(2, toId);
            ps.setDouble(3, amount);
            ps.setString(4, type);

            ps.executeUpdate();
        }
    }
    public List<Transaction> getTransactionById(Connection conn, int walletId) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();

        String sql = "SELECT * FROM transactions WHERE sender_id = ? ORDER BY transaction_time DESC";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1,walletId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                transactions.add(
                        Transaction.builder()
                                .transactionId(
                                        rs.getInt("transaction_id"))
                                .senderId(
                                        rs.getInt("sender_id"))
                                .receiverId(
                                        rs.getInt("receiver_id"))
                                .amount(
                                        rs.getDouble("amount"))
                                .transactionType(
                                        rs.getString("transaction_type"))
                                .transactionTime(
                                        rs.getTimestamp("transaction_time"))
                                .build()
                );
            }
        }


        return transactions;
    }

    public List<Transaction> getAllTransactions() throws SQLException {
        List<Transaction> transactions = new ArrayList<>();

        String sql = "SELECT * FROM transactions ORDER BY transaction_time DESC";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                transactions.add(
                        Transaction.builder()
                                .transactionId(
                                        rs.getInt("transaction_id"))
                                .senderId(
                                        rs.getInt("sender_id"))
                                .receiverId(
                                        rs.getInt("receiver_id"))
                                .amount(
                                        rs.getDouble("amount"))
                                .transactionType(
                                        rs.getString("transaction_type"))
                                .transactionTime(
                                        rs.getTimestamp("transaction_time"))
                                .build()
                );
            }
        }

        return transactions;
    }
}