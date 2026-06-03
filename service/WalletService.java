package service;

import model.AccountType;
import repository.WalletRepository;
import util.InputValidator;

import java.sql.*;

public class WalletService extends AccountType {
    InputValidator input = new InputValidator();
    WalletRepository walletRepo;
    public WalletService(){
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

        if (!input.checkInput(amount)) {
            throw new SQLException("[ERROR] Withdrawal failed: invalid amount " + amount);
        }

        double balance = walletRepo.getBalanceFromWallet(conn, fromId);

        if (balance >= amount){
            walletRepo.withdrawFromDB(conn, amount, fromId);
        }
        else {
            throw new SQLException("[ERROR] Withdrawal failed: insufficient balance");
        }

    }

    @Override
    public void transfer(Connection conn, int fromId, int toId, double amount) throws SQLException {
        // Withdraw throws on failure, so we only deposit when the debit actually succeeded.
        withdraw(conn, amount, fromId);
        deposit(conn, amount, toId);
    }
}
