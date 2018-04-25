package com.example.k.bankapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
    private User user;
    private User userCopy;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        init();
    }

    private void init(){
        user = (User) getIntent().getSerializableExtra("User");
        userCopy = (User) getIntent().getSerializableExtra("UserCopy");
        // Check if getSerializableExtra method provided user object
        if(user != null){
            accountNumberField = (EditText) findViewById(R.id.accountNumberField);
            amountField = (EditText) findViewById(R.id.amountField);
            sendButton = (Button) findViewById(R.id.sendButton);
            cancelButton = (Button) findViewById(R.id.cancelButton);

            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(amountField.getText().toString().equals("") || accountNumberField.getText().toString().equals("") || amountField.getText().equals(null) || accountNumberField.getText().equals(null)){
                        showErrorMessage();
                    } else {
                        double amountValue = Double.parseDouble(amountField.getText().toString());
                        if(user.getAccounts().get(0).getBalance() < amountValue){
                            showInsufficientFundsMessage();
                        } else {
                            getConfirmation();
                        }
                    }
                }
            });

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancelAndReturn();
                }
            });
        }
    }
    // SInform user that fields cannot be empty for transaction to continue
    private void showErrorMessage(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Error");
        builder.setMessage("Fields cannot be empty");
        builder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        accountNumberField.setText("");
                        amountField.setText("");
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showInsufficientFundsMessage(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Error");
        builder.setMessage("You don't have sufficient funds to complete transaction");
        builder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        accountNumberField.setText("");
                        amountField.setText("");
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    // Get transaction confirmation form user
    private void getConfirmation(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to transfer £" + amountField.getText() + " to " + accountNumberField.getText() + "?");
        builder.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        makeTransactionAndReturn();
                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                accountNumberField.setText("");
                amountField.setText("");
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void makeTransactionAndReturn(){
        RequestQueue queue = Volley.newRequestQueue(Transfer.this);
        String url ="https://project-tobetodo.c9users.io/userTransfer/" + userCopy.getUsername() + "/" + userCopy.getPassword()
                + "/" + accountNumberField.getText() + "/" + amountField.getText();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        User user = new User();
                        // Response converted to user object
                        user = user.fromJson(response);
                        user.setPassword( userCopy.getPassword());
                        // Intent object created with attached user to be used by next class
                        Intent intent = new Intent(Transfer.this, AccountLogin.class);
                        intent.putExtra("User", user);
                        // Display short message to the user
                        Toast confirmToast = Toast.makeText(Transfer.this, "Transaction completed!", Toast.LENGTH_SHORT);
                        confirmToast.show();
                        // Current activity closed and next opened
                        finish();
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Show message if error has occurred or no response received
                        showNetworkError();
                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(Transfer.this).addToRequestQueue(jsonObjectRequest);
    }
    // Show error if request unsuccessful
    private void showNetworkError(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Network Error");
        builder.setMessage("Unknown error has occured.\n Check account number you entered exists. \n Ensure you are connected to internet.");
        builder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        accountNumberField.setText("");
                        amountField.setText("");
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void cancelAndReturn(){
        // Instantiate the RequestQueue
        RequestQueue queue = Volley.newRequestQueue(Transfer.this);
        //Create specific url for user
        String url ="https://project-tobetodo.c9users.io/userLogin/" + userCopy.getUsername() + "/" + userCopy.getPassword();
        //Create new jsobObjectRequest as GET request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        User user = new User();
                        // Response converted to user object
                        user = user.fromJson(response);
                        user.setPassword( userCopy.getPassword());
                        // Intent object created with attached user to be used by next class
                        Intent intent = new Intent(Transfer.this, AccountLogin.class);
                        intent.putExtra("User", user);
                        // Current activity closed and next opened
                        finish();
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Display error to the user
                        showNetworkError();
                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(Transfer.this).addToRequestQueue(jsonObjectRequest);
    }
}

