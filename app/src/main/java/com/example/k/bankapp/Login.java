package com.example.k.bankapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.*;
import org.json.JSONObject;


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
        init();
    }
    // Initialise components and set onClickListeners on buttons
    private void init(){
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
    // Check after user pressed login button
    public void  loginButtonPressed(){
        //If text fields are equals to null or empty
        if (mUsername.getText().toString().equals("") || mPassword.getText().toString().equals("") || mUsername.getText().equals(null) || mPassword.getText().equals(null)){
            showEmptyFieldMessage();
        } else {
            proceedWithLogin();
        }
    }
    // Show error message that the fields cannot be empty
    private void showEmptyFieldMessage(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Error");
        builder.setMessage("Fields cannot be empty");
        builder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mUsername.setText("");
                        mPassword.setText("");
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    // Call a login request to the server
    private void proceedWithLogin(){
        enteredUsername = mUsername.getText().toString();
        enteredPassword = mPassword.getText().toString();

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        //Create specific url for user
        String url ="https://project-tobetodo.c9users.io/userLogin/" + enteredUsername + "/" + enteredPassword;
        //Create new jsobObjectRequest as GET request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override // Method fired when response received
                    public void onResponse(JSONObject response) {
                        User user = new User();
                        // Response converted to user object
                        user = user.fromJson(response);
                        user.setPassword(enteredPassword);
                        // Intent object created with attached user to be used by next class
                        Intent intent = new Intent(Login.this, AccountLogin.class);
                        intent.putExtra("User", user);
                        // Toast object created to display quick message to the user
                        Toast confirmToast = Toast.makeText(Login.this, "Login successful!", Toast.LENGTH_SHORT);
                        confirmToast.show();
                        // Current activity closed and next opened
                        finish();
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {

                    @Override // Method fired when no response received or error occurred
                    public void onErrorResponse(VolleyError error) {
                        //Display error to the user
                        showNetworkErrorMessage();
                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
    // Show error message to the user if no response received
    private void showNetworkErrorMessage(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Network Error");
        builder.setMessage("Esnure username and password are correct. \nCheck internet connection.");
        builder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mUsername.setText("");
                        mPassword.setText("");
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    // Go back to home screen
    private void cancelButtonPressed(){
        Intent intent = new Intent(Login.this, Home.class);
        finish();
        startActivity(intent);
    }
}