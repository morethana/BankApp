package com.example.k.bankapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;
import java.util.Collections;

public class TransactionList extends AppCompatActivity {
    ListView mListView;
    User user;
    User userCopy;
    Button backButton;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_list);
        init();
    }

    private void init(){
        user = (User) getIntent().getSerializableExtra("User");
        userCopy = (User) getIntent().getSerializableExtra("UserCopy");
        mListView = (ListView)findViewById(R.id.listView);
        backButton = (Button)findViewById(R.id.backButton);
        // Reverse the order of transactions so newest is displayed first
        Collections.reverse(user.getAccounts().get(0).getTransactions());
        // Creates new list adapter to display transactions
        TransactionListAdapter adapter = new TransactionListAdapter(this, R.layout.adapter_view_layout,
                user.getAccounts().get(0).getTransactions(), user);
        mListView.setAdapter(adapter);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestDataAndReturn();
            }
        });
    }
    // Request updated data to be displayed on account screen
    private void requestDataAndReturn(){
        // Instantiate the RequestQueue if required
        RequestQueue queue = Volley.newRequestQueue(TransactionList.this);
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
                        Intent intent = new Intent(TransactionList.this, AccountLogin.class);
                        intent.putExtra("User", user);
                        // Current activity closed and next opened
                        finish();
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Display error to the user
                        showNetworkErrorMessage();
                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(TransactionList.this).addToRequestQueue(jsonObjectRequest);
    }
    // Show error message if network error occurred
    private void showNetworkErrorMessage(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Network Error");
        builder.setMessage("Check internet connection.");
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
