package com.example.k.bankapp;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.*;
import com.loopj.android.http.*;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;

public class Login extends AppCompatActivity {

    private String enteredUsername;
    private String enteredPassword;

    private EditText mUsername;
    private EditText mPassword;
    private Button mLoginButton;
    private Button mCancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUsername = (EditText) findViewById(R.id.username);
        mPassword = (EditText) findViewById(R.id.password);

        mLoginButton = (Button) findViewById(R.id.loginButton);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButtonPressed();
            }
        });

        mCancelButton = (Button) findViewById(R.id.loginCancel);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelButtonPressed();
            }
        });
    }

    public void  loginButtonPressed(){
        enteredUsername = mUsername.getText().toString();
        enteredPassword = mPassword.getText().toString();

        Log.d("Test", enteredUsername.toString());
        Log.d("Test", enteredPassword.toString());
        Log.d("Test", "https://project-tobetodo.c9users.io/userLogin/" + enteredUsername + "/" + enteredPassword);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://project-tobetodo.c9users.io/userLogin/" + enteredUsername + "/" + enteredPassword;
        String url2 ="http://www.google.com";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Testing Volley", "Response is: "+ response.toString());

                        User user = new User();
                        user = user.fromJson(response);
                        Log.d("TEST IN HOME", user.toString());
//                        double test = user.getAccounts().get(0).getTransactions().get(0).getAmount();
//                        Log.d("ANOTHER IN HOME", Double.toString(test));

                        user.setPassword(enteredPassword);

                        Intent intent = new Intent(Login.this, AccountLogin.class);
                        intent.putExtra("User", user);
                        finish();
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Testing Volley", "That didn't work!");
                        Toast errorToast = Toast.makeText(Login.this, "Error, your username or password is incorrect!", Toast.LENGTH_SHORT);
                        errorToast.show();

                    }
                });

// Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);

//// Request a string response from the provided URL.
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        // Display the first 500 characters of the response string.
//                        Log.d("Testing Volley", "Response is: "+ response.toString());
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d("Testing Volley", "That didn't work!");
//            }
//        });
//
//// Add the request to the RequestQueue.
//        queue.add(stringRequest);

//        AsyncHttpClient client = new AsyncHttpClient();
//
//        client.get("https://project-tobetodo.c9users.io/userLogin/" + enteredUsername + "/" + enteredPassword, new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                Log.d("onSuccess", response.toString());
//                User user = new User();
//                user = user.fromJson(response);
//                Log.d("TEST IN HOME", user.toString());
//                double test = user.getAccounts().get(0).getTransactions().get(0).getAmount();
//                Log.d("ANOTHER IN HOME", Double.toString(test));
//
//                user.setPassword(enteredPassword);
//
//                Intent intent = new Intent(Home.this, AccountLogin.class);
//                intent.putExtra("User", user);
//                finish();
//                startActivity(intent);
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
//                Log.d("onFailure", Integer.toString(statusCode));
//                Log.d("onFailure", headers.toString());
//                Log.d("onFailure", e.toString());
//                Log.d("onFailure", response.toString());
//
//
//            }
//        });
//        Log.d("Test", "Past the client method");
//
////        Intent intent = new Intent(this, com.example.k.bank.AccountLogin.class);
////        finish();
////        startActivity(intent);
    }

    private void cancelButtonPressed(){
        Intent intent = new Intent(Login.this, Home.class);
        finish();
        startActivity(intent);
    }
}

