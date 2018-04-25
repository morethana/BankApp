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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;

import at.grabner.circleprogress.CircleProgressView;
import at.grabner.circleprogress.TextMode;

public class Stats extends AppCompatActivity {
    private Button backButton;
    private CircleProgressView circleProgressView;
    private TextView progressViewLabel;
    private TextView popularAccount;
    private User user;
    private User userCopy;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        init();
    }

    private void init(){

        user = (User) getIntent().getSerializableExtra("User");
        userCopy = (User) getIntent().getSerializableExtra("UserCopy");

        progressViewLabel = (TextView) findViewById(R.id.progressViewLabel);
        circleProgressView = (CircleProgressView) findViewById(R.id.circleProgressView);
        popularAccount = (TextView) findViewById(R.id.popularAccount);
        // Create new calendar object for current date
        Calendar cal = Calendar.getInstance();
        String dateString = "";
        // Get current month and year
        int currentYear = cal.get(Calendar.YEAR);
        int currentMonth = cal.get(Calendar.MONTH) + 1;
        // Create date string to be compared with transaction dates
        if(cal.get(Calendar.MONTH) < 10){
            dateString = currentYear + "-0" + currentMonth;
        } else {
            dateString = currentYear + "-" + currentMonth;
        }


        double amountSpend = 0.0;
        // Calculate amount spend in current month
        for(int i = 0; i < user.getAccounts().get(0).getTransactions().size(); i++){
            if(user.getAccounts().get(0).getTransactions().get(i).getDate().substring(0, 7).equals(dateString)){
                if(user.getAccounts().get(0).getTransactions().get(i).getReceiver() != Integer.parseInt(user.getAccounts().get(0).getId())){
                    amountSpend = amountSpend + user.getAccounts().get(0).getTransactions().get(i).getAmount();
                }

            }
        }
        // Find favorite account
        int favAccount = getPopularElement(user.getAccounts().get(0).getTransactions(), user.getAccounts().get(0));

        backButton = (Button) findViewById(R.id.backStatsButton);
        progressViewLabel.setText("Amount spend in " + dateString);
        circleProgressView.setTextMode(TextMode.TEXT);
        circleProgressView.setText("£" + amountSpend);
        circleProgressView.setValue((float)amountSpend);
        popularAccount.setText(Integer.toString(favAccount));
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestData();
            }
        });
    }

    private void requestData(){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(Stats.this);
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
                        Intent intent = new Intent(Stats.this, AccountLogin.class);
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
        MySingleton.getInstance(Stats.this).addToRequestQueue(jsonObjectRequest);
    }
    // Show error message to the user if no response received
    private void showNetworkErrorMessage(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Network Error");
        builder.setMessage("Check internet connection.");
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Return most popular integer in array
    private int getPopularElement(ArrayList<Transaction> a, Account account) {
        int count = 1, tempCount;
        int popular = a.get(0).getReceiver();
        int temp = 0;
        for (int i = 0; i < (a.size() - 1); i++) {
            // Do not include user's account in search
            if(a.get(i).getReceiver() == Integer.parseInt(account.getId()))
                continue;
            temp = a.get(i).getReceiver();
            tempCount = 0;
            for (int j = 1; j < a.size(); j++) {
                if (temp == a.get(j).getReceiver())
                    tempCount++;
            }
            if (tempCount > count) {
                popular = temp;
                count = tempCount;
            }
        }
        return popular;
    }
}

