package service;

import config.DatabaseConfig;
import model.AccountType;
import repository.WalletRepository;
import util.InputValidator;

import java.sql.*;

public class Wallet extends AccountType {
    InputValidator input = new InputValidator();
    WalletRepository walletRepo;
    public Wallet(){
        walletRepo = new WalletRepository();

    }
    @Override
    public void deposit(Connection conn, double amount, int toId) throws SQLException {
        if (input.checkInput(amount)){
            walletRepo.depositIntoDB(conn,amount,toId);
            System.out.println("[SUCCESS] " + amount + " Deposited");
        }
        else {
            System.out.println("[ERROR] Deposit failed !");
        }


    }

    @Override
    public void withdraw(Connection conn, double amount, int fromId) throws SQLException {

        double balance = walletRepo.getBalanceFromWallet(conn, fromId);

        if (balance >= amount){
            walletRepo.withdrawFromDB(conn, amount, fromId);
        }
        else {
            System.out.println("[ERROR] Deposit failed !");
        }

    }

    @Override
    public void transfer(Connection conn, int fromId, int toId, double amount) throws SQLException {
        withdraw(conn, amount, fromId);
        deposit(conn, amount, toId);
    }
}
