package service;

import config.DatabaseConfig;
import exception.InsufficientBalanceException;
import exception.InvalidInputException;
import exception.SuspiciousActivityException;
import model.Transaction;
import repository.StudentRepository;
import repository.TransactionRepository;

import java.sql.Connection;
import java.util.List;

public class TransactionService {

    // Any single movement above this amount is flagged as suspicious.
    private static final double SUSPICIOUS_AMOUNT_THRESHOLD = 100_000.0;

    // System account used as the counter-party for deposits/withdrawals.
    private static final int SYSTEM_ACCOUNT_ID = 0;

    private final StudentRepository studentRepository =
            new StudentRepository();

    private final TransactionRepository transactionRepository =
            new TransactionRepository();

    // ADD MONEY
    public void addMoney(int studentId,
                         double amount)
            throws Exception {

        validateAmount(amount);

        Connection conn = null;

        try {

            conn = DatabaseConfig.getConnection();
            conn.setAutoCommit(false);

            double balance =
                    studentRepository.getBalance(
                            conn,
                            studentId);

            studentRepository.updateBalance(
                    conn,
                    studentId,
                    balance + amount);

            recordTransaction(
                    conn,
                    SYSTEM_ACCOUNT_ID,
                    studentId,
                    amount,
                    "DEPOSIT");

            conn.commit();

            System.out.println(
                    "Money Added Successfully");

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

    // WITHDRAW MONEY
    public void withdrawMoney(int studentId,
                              double amount)
            throws Exception {

        validateAmount(amount);

        Connection conn = null;

        try {

            conn = DatabaseConfig.getConnection();
            conn.setAutoCommit(false);

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

            recordTransaction(
                    conn,
                    studentId,
                    SYSTEM_ACCOUNT_ID,
                    amount,
                    "WITHDRAW");

            conn.commit();

            System.out.println(
                    "Withdrawal Successful");

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

    // TRANSFER MONEY
    public void transferMoney(int senderId,
                              int receiverId,
                              double amount)
            throws Exception {

        validateAmount(amount);

        if (senderId == receiverId) {
            throw new InvalidInputException(
                    "Sender and receiver cannot be the same");
        }

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

            recordTransaction(
                    conn,
                    senderId,
                    receiverId,
                    amount,
                    "TRANSFER");

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

    // ---- helpers ----

    private void validateAmount(double amount)
            throws InvalidInputException, SuspiciousActivityException {

        if (amount <= 0) {
            throw new InvalidInputException(
                    "Amount must be greater than zero");
        }

        if (amount > SUSPICIOUS_AMOUNT_THRESHOLD) {
            throw new SuspiciousActivityException(
                    "Suspicious activity detected: amount "
                            + amount
                            + " exceeds the allowed limit of "
                            + SUSPICIOUS_AMOUNT_THRESHOLD);
        }
    }

    private void recordTransaction(Connection conn,
                                   int senderId,
                                   int receiverId,
                                   double amount,
                                   String type)
            throws Exception {

        Transaction transaction =
                Transaction.builder()
                        .senderId(senderId)
                        .receiverId(receiverId)
                        .amount(amount)
                        .transactionType(type)
                        .build();

        transactionRepository.saveTransaction(
                conn,
                transaction);
    }
}
