package main;

import config.DatabaseConfig;
import model.AccountType;
import service.TransactionService;
import service.WalletService;

import java.sql.Connection;

public class Main {

    public static void main(String[] args) throws  Exception{
        Connection conn  = DatabaseConfig.getConnection();
        AccountType w = new WalletService();

        w.deposit(conn, 100, 101, "DEPOSIT");
        TransactionService ts = new TransactionService();
        ts.showTransactionHistory();

    }
}