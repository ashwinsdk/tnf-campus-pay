package service;

import config.DatabaseConfig;
import exception.InsufficientBalanceException;
import exception.InvalidInputException;
import exception.SuspiciousActivityException;
import model.Transaction;
import repository.TransactionRepository;

import java.sql.Connection;
import java.util.List;

public class TransactionService {

    private final TransactionRepository transactionRepository =
            new TransactionRepository();


    // HISTORY
    public void showTransactionHistory(Connection conn, int walletid)
            throws Exception {

        List<Transaction> transactions =
                transactionRepository.getTransactionById(conn, walletid);

        System.out.println("------Your Transactions------");
        transactions.forEach(System.out::println);
        System.out.println("----------------------------");

        double totalSpent  = transactions.stream()
                .filter(t -> t.getTransactionType().equals("WITHDRAW") || t.getTransactionType().equals("TRANSFER"))
                .map(t -> t.getAmount())
                .reduce(0.0, Double::sum);

        System.out.println("Total spent:" + totalSpent);
        System.out.println("-------------------------");
    }


    public void showReport(Connection conn)
            throws Exception {

        List<Transaction> transactions =
                transactionRepository.getAllTransactions();

        System.out.println("-------Top 5 Spenders-------");
        transactions.stream()
                .sorted((t1, t2) -> Double.compare(t2.getAmount(), t1.getAmount()))
                .limit(5)
                .forEach(System.out::println);

        System.out.println("----------------------------");
    }

    public void showTransactionHistory()
            throws Exception {

        List<Transaction> transactions =
                transactionRepository
                        .getAllTransactions();

        System.out.println("------All Transactions------");
        transactions.forEach(System.out::println);
        System.out.println("----------------------------");

    }



}
