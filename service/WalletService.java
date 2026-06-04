package service;

import model.AccountType;
import repository.TransactionRepository;
import repository.WalletRepository;
import util.InputValidator;

import java.sql.*;

public class WalletService extends AccountType {

    InputValidator input = new InputValidator();
    private WalletRepository walletRepo;
    private TransactionService txService;
    private TransactionRepository tx;

    public WalletService(){
        walletRepo = new WalletRepository();
        tx = new TransactionRepository();
        txService= new TransactionService();

    }
    public TransactionService getTx(){
        return txService;
    }
    public int getWalletIdByStudId(Connection conn,int student_id) throws SQLException {
        return walletRepo.getWalletIdByStudentId(conn, student_id);
    }
    public boolean checkStudentId(Connection conn, int studentId) throws SQLException {
        return walletRepo.checkDuplicateStudentId(conn, studentId);
    }

    public boolean checkWalletId(Connection conn, int walletId) throws SQLException {
        return walletRepo.checkDuplicateWallet(conn, walletId);
    }


    public void createWallet(Connection conn,
                             int studentId,
                             double amount) throws Exception {
        int walletId = walletRepo.getLatestWalletId(conn) + 1;

        try {

            conn.setAutoCommit(false);

            if (walletRepo.checkDuplicateWallet(conn, studentId)) {

                walletRepo.createNewWallet(conn, walletId, studentId, amount);
                tx.saveTransaction(conn, walletId, walletId, amount, "DEPOSIT");
                conn.commit();
                System.out.println("[SUCCESS] Wallet Created, Wallet id: "+ walletId);

            }

        } catch (Exception e) {

            conn.rollback();
            tx.saveTransaction(conn, walletId, walletId, amount, "FAILED");
            System.out.println("[ERROR] Wallet Creation Failed");
            throw e;

        } finally {

            conn.setAutoCommit(true);

        }
    }


    @Override
    public void deposit(Connection conn, double amount, int toId, String type) throws Exception {
        if (input.checkInput(amount)){
            walletRepo.depositIntoDB(conn, amount, toId);
            if (type.equals("DEPOSIT")) {
                tx.saveTransaction(conn, toId, toId, amount, type);
            }
            System.out.println("[SUCCESS] " + amount + " Deposited");
        }
        else {
            System.out.println("[ERROR] Deposit failed !");
            tx.saveTransaction(conn, toId, toId, amount, "FAILED");
        }


    }

    @Override
    public void withdraw(Connection conn, double amount, int fromId, String type) throws SQLException {

        if (!input.checkInput(amount)) {
            throw new SQLException("[ERROR] Withdrawal failed: invalid amount " + amount);
        }

        double balance = walletRepo.getBalanceFromWallet(conn, fromId);

        if (balance >= amount){
            conn.setAutoCommit(false);
            walletRepo.withdrawFromDB(conn, amount, fromId);

            switch (type){
                case "WITHDRAW":
                    tx.saveTransaction(conn,fromId,fromId, amount, "WITHDRAW");
                    break;
                case "HOSTEL":
                    tx.saveTransaction(conn,fromId,fromId, amount, "HOSTEL");
                    break;
                case "LIBRARY":
                    tx.saveTransaction(conn,fromId,fromId, amount, "LIBRARY");
                    break;
                case "HACKATHON":
                    tx.saveTransaction(conn,fromId,fromId, amount, "HACKATHON");
                    break;
                case "CANTEEN":
                    tx.saveTransaction(conn,fromId,fromId, amount, "CANTEEN");
                    break;
                case "WORKSHOP":
                    tx.saveTransaction(conn,fromId,fromId, amount, "WORKSHOP");
                    break;
                default:
                    break;
            }
        }
        else {
            conn.rollback();
            tx.saveTransaction(conn,fromId,fromId, amount, "FAILED");
            throw new SQLException("[ERROR] Withdrawal failed: insufficient balance");
        }

    }

    @Override
    public void transfer(Connection conn, int fromId, int toId,
                         double amount, String type) throws Exception {

        try {

            conn.setAutoCommit(false);

            withdraw(conn, amount, fromId, type);
            deposit(conn, amount, toId, type);
            tx.saveTransaction(conn, fromId, toId, amount, type);
            conn.commit();

            System.out.println("[SUCCESS] Transfer Completed");

        } catch (Exception e) {

            conn.rollback();
            tx.saveTransaction(conn, fromId, toId, amount, "FAILED");
            System.out.println("[ERROR] Transfer Failed. Transaction Rolled Back");
            throw e;

        } finally {

            conn.setAutoCommit(true);

        }
    }

}
