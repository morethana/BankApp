package com.example.k.bankapp;

import java.io.Serializable;
import java.util.ArrayList;

// Class for storing account information from Json
public class Account implements Serializable {
    private String id;
    private double balance;
    private ArrayList<Transaction> transactions;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }
}

