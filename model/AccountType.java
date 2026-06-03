package model;

import lombok.Data;

@Data
public abstract class Wallet {
    private int walletId;
    private int studentId;
    private double balance;

    public abstract void deposit(double amount);
    public abstract void withdraw(double amount );
    public abstract void transfer(int fromWalletId, int toWalletId, double amount);
}
