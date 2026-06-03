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

    private final TransactionRepository transactionRepository =
            new TransactionRepository();


    // HISTORY
    public void showTransactionHistory()
            throws Exception {

        List<Transaction> transactions =
                transactionRepository
                        .getAllTransactions();

        transactions.forEach(System.out::println);
    }

}
