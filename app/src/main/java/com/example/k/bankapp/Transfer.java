package com.example.k.bankapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class Transfer extends AppCompatActivity {
    private EditText accountNumberField;
    private EditText amountField;
    private Button sendButton;
    private Button cancelButton;
    private User userCopy;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        //remember to check if not empty
        accountNumberField = (EditText) findViewById(R.id.accountNumberField);
        amountField = (EditText) findViewById(R.id.amountField);
        sendButton = (Button) findViewById(R.id.sendButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);

        userCopy = (User) getIntent().getSerializableExtra("UserCopy");

        Log.d("In Transfer", userCopy.getUsername() + " -- " + userCopy.getPassword());

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue queue = Volley.newRequestQueue(Transfer.this);
                String url ="https://project-tobetodo.c9users.io/userTransfer/" + userCopy.getUsername() + "/" + userCopy.getPassword()
                        + "/" + accountNumberField.getText() + "/" + amountField.getText();
                String url2 ="http://www.google.com";

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("TestingVolleyTransfer", "Response is: "+ response.toString());

                                User user = new User();
                                user = user.fromJson(response);
                                Log.d("TEST IN TRANSFER", user.toString());
                                double test = user.getAccounts().get(0).getTransactions().get(0).getAmount();
                                Log.d("ANOTHER IN TRANSFER", Double.toString(test));

                                user.setPassword( userCopy.getPassword());

                                Intent intent = new Intent(Transfer.this, AccountLogin.class);
                                intent.putExtra("User", user);
                                finish();
                                startActivity(intent);
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("TestingVolleyTransfer", "That didn't work!");

                            }
                        });

                // Access the RequestQueue through your singleton class.
                MySingleton.getInstance(Transfer.this).addToRequestQueue(jsonObjectRequest);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue queue = Volley.newRequestQueue(Transfer.this);
                String url ="https://project-tobetodo.c9users.io/userLogin/" + userCopy.getUsername() + "/" + userCopy.getPassword();
                String url2 ="http://www.google.com";

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("TestingVolleyTransfer", "Response is: "+ response.toString());

                                User user = new User();
                                user = user.fromJson(response);
                                Log.d("TEST IN TRANSFER", user.toString());
                                double test = user.getAccounts().get(0).getTransactions().get(0).getAmount();
                                Log.d("ANOTHER IN TRANSFER", Double.toString(test));

                                user.setPassword( userCopy.getPassword());

                                Intent intent = new Intent(Transfer.this, AccountLogin.class);
                                intent.putExtra("User", user);
                                finish();
                                startActivity(intent);
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("TestingVolleyTransfer", "That didn't work!");

                            }
                        });

// Access the RequestQueue through your singleton class.
                MySingleton.getInstance(Transfer.this).addToRequestQueue(jsonObjectRequest);

            }
        });
    }
}

