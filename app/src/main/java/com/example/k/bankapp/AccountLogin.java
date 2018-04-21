package com.example.k.bankapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.*;

import org.json.JSONObject;

import java.util.concurrent.CountDownLatch;

import cz.msebera.android.httpclient.Header;

public class AccountLogin extends AppCompatActivity {
    private TextView username;
    private TextView accountNumber;
    private TextView balance;

    private Button mQuickTransactionButton;
    private Button mTransactionListButton;
    private Button mStatsButton;
    private Button mLogoutButton;
    User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        init();
    }

    private void init(){
        user = (User) getIntent().getSerializableExtra("User");
        if(!(user == null)){
            final User userCopy = new User();
            userCopy.setUsername(user.getUsername());
            userCopy.setPassword(user.getPassword());

            username = (TextView) findViewById(R.id.user);
            accountNumber = (TextView) findViewById(R.id.accountNumberField);
            balance = (TextView) findViewById(R.id.currentBalance);

            mQuickTransactionButton = (Button) findViewById(R.id.quickTransactionButton);
            mTransactionListButton = (Button) findViewById(R.id.getTransactionListButton);
            mStatsButton = (Button) findViewById(R.id.statsButton);
            mLogoutButton = (Button) findViewById(R.id.logoutButton);

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

//    private User requestData(){
//        user = (User) getIntent().getSerializableExtra("UserCopy");
//        RequestQueue queue = Volley.newRequestQueue(this);
//        String url ="https://project-tobetodo.c9users.io/userLogin/" + user.getUsername() + "/" + user.getPassword();
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
//                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        User user = new User();
//                        user = user.fromJson(response);
//                        double test = user.getAccounts().get(0).getTransactions().get(0).getAmount();
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.d("TestingVolleyACCOUNT", "That didn't work!");
//                    }
//                });
//
//        // Access the RequestQueue through your singleton class.
//        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
//        return user;
//    }
}

