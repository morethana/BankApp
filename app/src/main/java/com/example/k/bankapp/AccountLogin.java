package com.example.k.bankapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AccountLogin extends AppCompatActivity {
    private TextView username;
    private TextView accountNumber;
    private TextView balance;
    private Button mQuickTransactionButton;
    private Button mTransactionListButton;
    private Button mStatsButton;
    private Button mLogoutButton;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        init();
    }

    private void init(){
        // Get user object from previous activity
        user = (User) getIntent().getSerializableExtra("User");
        //Check if getSerializableExtra provided user object
        if(!(user == null)){
            // Copy of user object for request
            final User userCopy = new User();
            userCopy.setUsername(user.getUsername());
            userCopy.setPassword(user.getPassword());
            // Get views from activity
            username = (TextView) findViewById(R.id.user);
            accountNumber = (TextView) findViewById(R.id.accountNumberField);
            balance = (TextView) findViewById(R.id.currentBalance);
            mQuickTransactionButton = (Button) findViewById(R.id.quickTransactionButton);
            mTransactionListButton = (Button) findViewById(R.id.getTransactionListButton);
            mStatsButton = (Button) findViewById(R.id.statsButton);
            mLogoutButton = (Button) findViewById(R.id.logoutButton);
            // Set views
            username.setText(user.getUsername());
            accountNumber.setText(user.getAccounts().get(0).getId());
            balance.setText(String.valueOf(user.getAccounts().get(0).getBalance()));

            mQuickTransactionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AccountLogin.this, Transfer.class);
                    intent.putExtra("User", user);
                    intent.putExtra("UserCopy", userCopy);
                    finish();
                    startActivity(intent);
                }
            });

            mTransactionListButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AccountLogin.this, TransactionList.class);
                    intent.putExtra("User", user);
                    intent.putExtra("UserCopy", userCopy);
                    finish();
                    startActivity(intent);
                }
            });

            mStatsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AccountLogin.this, Stats.class);
                    intent.putExtra("User", user);
                    intent.putExtra("UserCopy", userCopy);
                    finish();
                    startActivity(intent);
                }
            });

            mLogoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AccountLogin.this, Home.class);
                    finish();
                    startActivity(intent);
                }
            });
        }
    }
}