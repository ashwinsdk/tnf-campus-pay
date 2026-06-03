package model;

import lombok.Data;

import java.sql.Connection;
import java.sql.SQLException;

@Data
public abstract class AccountType {
    private int walletId;
    private int studentId;
    private double balance;

    public abstract void deposit(Connection conn, double amount, int toId) throws SQLException;
    public abstract void withdraw(Connection conn, double amount, int fromId) throws SQLException;
    public abstract void transfer(Connection conn, int fromId, int toId, double amount) throws SQLException;
}
