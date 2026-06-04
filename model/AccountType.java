package model;

import lombok.Data;

import java.sql.Connection;
import java.sql.SQLException;

@Data
public abstract class AccountType {
//    private static int counter =0;

    private int walletId;
    private int studentId;
    private double balance;
//    public AccountType(int studentId, double balance) {
//        counter++;
//        this.walletId  = counter;
//        this.studentId = studentId;
//        this.balance  = balance;
//    }
    public abstract void deposit(Connection conn, double amount, int toId, String type) throws Exception;
    public abstract void withdraw(Connection conn, double amount, int fromId, String type) throws Exception;
    public abstract void transfer(Connection conn, int fromId, int toId, double amount, String type) throws Exception;
}
