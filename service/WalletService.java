package service;

import model.AccountType;
import repository.TransactionRepository;
import repository.WalletRepository;
import util.InputValidator;

import java.sql.*;

public class WalletService extends AccountType {
    InputValidator input = new InputValidator();
    WalletRepository walletRepo;
    TransactionRepository tx;
    public WalletService(){
        walletRepo = new WalletRepository();
        tx = new TransactionRepository();

    }
    @Override
    public void deposit(Connection conn, double amount, int toId, String type) throws Exception {
        if (input.checkInput(amount)){
            if (type.equals("DEPOSIT")){
                tx.saveTransaction(conn,toId,toId, amount, type);
            }
            walletRepo.depositIntoDB(conn,amount,toId);
            System.out.println("[SUCCESS] " + amount + " Deposited");
        }
        else {
            System.out.println("[ERROR] Deposit failed !");
        }


    }

    @Override
    public void withdraw(Connection conn, double amount, int fromId, String type) throws SQLException {

        if (!input.checkInput(amount)) {
            throw new SQLException("[ERROR] Withdrawal failed: invalid amount " + amount);
        }

        double balance = walletRepo.getBalanceFromWallet(conn, fromId);

        if (balance >= amount){
            walletRepo.withdrawFromDB(conn, amount, fromId);

            if (type.equals("WITHDRAW")){
                tx.saveTransaction(conn,fromId,fromId, amount, type);
            }
        }
        else {
            throw new SQLException("[ERROR] Withdrawal failed: insufficient balance");
        }

    }

    @Override
    public void transfer(Connection conn, int fromId, int toId, double amount, String type) throws Exception {
        // Withdraw throws on failure, so we only deposit when the debit actually succeeded.

        withdraw(conn, amount, fromId, type);
        deposit(conn, amount, toId, type);
        if (type.equals("TRANSFER")) {
             tx.saveTransaction(conn,fromId,toId, amount, type);
        }
    }
}
