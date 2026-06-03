package model;

import util.InputValidator;

public class Wallet extends AccountType {
    InputValidator input = new InputValidator();

    @Override
    public void deposit(double amount) {
        if(input.checkInput(amount)) {
            setBalance(getBalance() + amount);
        }
    }

    @Override
    public void withdraw(double amount) {
        if (input.checkInput(amount) && amount >= getBalance()){
            setBalance(getBalance() - amount);
        }
    }

    @Override
    public void transfer(int fromWalletId, int toWalletId, double amount) {

    }
}
