package service;

import config.DatabaseConfig;
import config.DatabaseConfig;
import exception.InsufficientBalanceException;
import model.Transaction;
import repository.StudentRepository;
import repository.TransactionRepository;

import java.sql.Connection;
import java.util.List;

public class TransactionService {

    private final StudentRepository studentRepository =
            new StudentRepository();

    private final TransactionRepository transactionRepository =
            new TransactionRepository();

    // ADD MONEY
    public void addMoney(int studentId,
                         double amount)
            throws Exception {

        try (Connection conn =
                     DatabaseConfig.getConnection()) {

            double balance =
                    studentRepository.getBalance(
                            conn,
                            studentId);

            studentRepository.updateBalance(
                    conn,
                    studentId,
                    balance + amount);

            System.out.println(
                    "Money Added Successfully");
        }
    }

    // WITHDRAW MONEY
    public void withdrawMoney(int studentId,
                              double amount)
            throws Exception {

        try (Connection conn =
                     DatabaseConfig.getConnection()) {

            double balance =
                    studentRepository.getBalance(
                            conn,
                            studentId);

            if (balance < amount) {
                throw new InsufficientBalanceException(
                        "Insufficient Balance");
            }

            studentRepository.updateBalance(
                    conn,
                    studentId,
                    balance - amount);

            System.out.println(
                    "Withdrawal Successful");
        }
    }

    // TRANSFER MONEY
    public void transferMoney(int senderId,
                              int receiverId,
                              double amount)
            throws Exception {

        Connection conn = null;

        try {

            conn = DatabaseConfig.getConnection();

            conn.setAutoCommit(false);

            double senderBalance =
                    studentRepository.getBalance(
                            conn,
                            senderId);

            double receiverBalance =
                    studentRepository.getBalance(
                            conn,
                            receiverId);

            if (senderBalance < amount) {
                throw new InsufficientBalanceException(
                        "Insufficient Balance");
            }

            studentRepository.updateBalance(
                    conn,
                    senderId,
                    senderBalance - amount);

            studentRepository.updateBalance(
                    conn,
                    receiverId,
                    receiverBalance + amount);

            Transaction transaction =
                    Transaction.builder()
                            .senderId(senderId)
                            .receiverId(receiverId)
                            .amount(amount)
                            .transactionType("TRANSFER")
                            .build();

            transactionRepository.saveTransaction(
                    conn,
                    transaction);

            conn.commit();

            System.out.println(
                    "Transfer Successful");

        } catch (Exception e) {

            if (conn != null) {
                conn.rollback();
            }

            throw e;

        } finally {

            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    // HISTORY
    public void showTransactionHistory()
            throws Exception {

        List<Transaction> transactions =
                transactionRepository
                        .getAllTransactions();

        transactions.forEach(System.out::println);
    }
}