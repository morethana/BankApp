package com.example.k.bankapp;

import java.io.Serializable;

// Class to define transaction objects from json response
public class Transaction implements Serializable{
    private String id;
    private String date;
    private int sender;
    private int receiver;
    private double amount;

    public Transaction(String id, String date, int sender, int receiver, double amount){
        this.id = id;
        this.date = date;
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public int getSender() {
        return sender;
    }

    public int getReceiver() {
        return receiver;
    }

    public double getAmount() {
        return amount;
    }
}

