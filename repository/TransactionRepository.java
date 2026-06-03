package repository;

import config.DatabaseConfig;
import model.Transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionRepository {

    public void saveTransaction(Connection conn,
                                Transaction transaction)
            throws SQLException {

        String sql = """
                INSERT INTO transactions
                (sender_id, receiver_id, amount, transaction_type)
                VALUES (?, ?, ?, ?)
                """;

        try (PreparedStatement ps =
                     conn.prepareStatement(sql)) {

            ps.setInt(1, transaction.getSenderId());
            ps.setInt(2, transaction.getReceiverId());
            ps.setDouble(3, transaction.getAmount());
            ps.setString(4, transaction.getTransactionType());

            ps.executeUpdate();
        }
    }

    public List<Transaction> getAllTransactions()
            throws SQLException {

        List<Transaction> transactions =
                new ArrayList<>();

        String sql =
                "SELECT * FROM transactions ORDER BY transaction_time DESC";

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