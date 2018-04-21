package com.example.k.bankapp;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable{
    private String username;
    private String password;
    private ArrayList<Account> accounts = new ArrayList<Account>();

    public User fromJson (JSONObject jsonObject){
        try {
            User user = new User();
            JSONObject test = new JSONObject();
            user.username = jsonObject.getJSONObject("user").get("username").toString();

            ArrayList<Transaction> transactions = new ArrayList<Transaction>();

            int numOfTransactions = jsonObject.getJSONObject("user").getJSONArray("accounts").getJSONObject(0).getJSONArray("transactions").length();

            for (int x = 0; x < numOfTransactions; x++){
                String id = jsonObject.getJSONObject("user").getJSONArray("accounts").getJSONObject(0).getJSONArray("transactions").getJSONObject(x).get("_id").toString();
                String date = jsonObject.getJSONObject("user").getJSONArray("accounts").getJSONObject(0).getJSONArray("transactions").getJSONObject(x).get("date").toString();
                int sender = Integer.parseInt(jsonObject.getJSONObject("user").getJSONArray("accounts").getJSONObject(0).getJSONArray("transactions").getJSONObject(x).get("accountOne").toString());
                int receiver = Integer.parseInt(jsonObject.getJSONObject("user").getJSONArray("accounts").getJSONObject(0).getJSONArray("transactions").getJSONObject(x).get("accountTwo").toString());
                double amount = Double.parseDouble(jsonObject.getJSONObject("user").getJSONArray("accounts").getJSONObject(0).getJSONArray("transactions").getJSONObject(x).get("amount").toString());
                transactions.add(new Transaction(id, date, sender, receiver, amount));
            }

            Account account1 = new Account();
            account1.setId(jsonObject.getJSONObject("user").getJSONArray("accounts").getJSONObject(0).get("_id").toString());
            account1.setBalance(Double.parseDouble(jsonObject.getJSONObject("user").getJSONArray("accounts").getJSONObject(0).get("balance").toString()));
            account1.setId(jsonObject.getJSONObject("user").getJSONArray("accounts").getJSONObject(0).get("_id").toString());
            account1.setTransactions(transactions);

            user.getAccounts().add(account1);

            return user;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public ArrayList<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(ArrayList<Account> accounts) {
        this.accounts = accounts;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}

