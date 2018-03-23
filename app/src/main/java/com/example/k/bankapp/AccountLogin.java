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
    private Button mLogoutButton;

    User user = new User();
    User user2;

    private final CountDownLatch loginLatch = new CountDownLatch(1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        init();
    }

    private void init(){

        user = (User) getIntent().getSerializableExtra("User");

//        if(user == null){
//            Log.d("InFIRST_IF+++++++++", "Start of if");
//            user = requestData();
//
//        }

        Log.d("JustBefore2ndIF", "--------------------");
        if(!(user == null)){
            Log.d("InSECOND_IF=======)", "Start of if");

            final User userCopy = new User();
            userCopy.setUsername(user.getUsername());
            userCopy.setPassword(user.getPassword());

            Log.d("In !(user == null)", userCopy.getUsername() + " -- " + userCopy.getPassword());

            Log.d("Test in AccountLogin", user.getUsername());
            Log.d("Test in AccountLogin", user.getAccounts().get(0).getId());
            Log.d("Test in AccountLogin", user.getAccounts().get(0).getTransactions().get(0).getDate());

            username = (TextView) findViewById(R.id.user);
            accountNumber = (TextView) findViewById(R.id.accountNumberField);
            balance = (TextView) findViewById(R.id.currentBalance);

            mQuickTransactionButton = (Button) findViewById(R.id.quickTransactionButton);
            mTransactionListButton = (Button) findViewById(R.id.getTransactionListButton);
            mLogoutButton = (Button) findViewById(R.id.logoutButton);

            username.setText(user.getUsername());
            accountNumber.setText(user.getAccounts().get(0).getId());
            balance.setText(String.valueOf(user.getAccounts().get(0).getBalance()));

            mQuickTransactionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AccountLogin.this, Transfer.class);
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

    private User requestData(){
        Log.d("In requestData", "STARTED...........................");

        user = (User) getIntent().getSerializableExtra("UserCopy");
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://project-tobetodo.c9users.io/userLogin/" + user.getUsername() + "/" + user.getPassword();
        String url2 ="http://www.google.com";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("TestingVolleyACCOUNT", "Response is: "+ response.toString());

                        User user = new User();
                        user = user.fromJson(response);
                        Log.d("TEST IN HOME", user.toString());
                        double test = user.getAccounts().get(0).getTransactions().get(0).getAmount();
                        Log.d("ANOTHER IN HOME", Double.toString(test));

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("TestingVolleyACCOUNT", "That didn't work!");

                    }
                });

// Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);

//        AsyncHttpClient client = new AsyncHttpClient();
//        client.get("https://project-tobetodo.c9users.io/userLogin/kevin/kevin", new JsonHttpResponseHandler(){
//
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                Log.d("onSuccessinAccountLogin", response.toString());
//                Log.d("++++++++++++++++++++++", response.toString());
//                Log.d("----------------------", response.toString());
//                User userr = new User();
//                user = userr.fromJson(response);
//                loginLatch.countDown();
//
//                Log.d("TestInAccountClient", user.toString());
//                Log.d("same again", user.getAccounts().get(0).getTransactions().get(0).getDate());
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
//                Log.d("onFailureinAccountLogin", e.toString());
//            }
//        });
//        if (user == null){
//            Log.d("ERROR!!!!", "Empty user");
//        }
////        try {
////            loginLatch.await();
////        } catch (InterruptedException e) {
////            e.printStackTrace();
////        }
//        Log.d("In requestData", "FINISHED............................");
        return user;

    }
}

