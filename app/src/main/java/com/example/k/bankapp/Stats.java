package com.example.k.bankapp;

import android.content.Intent;
import android.os.Bundle;
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

        ArrayList<Integer> testArray = new ArrayList<Integer>();
        testArray.add(1);
        testArray.add(2);
        testArray.add(1);
        testArray.add(3);
        testArray.add(1);
        testArray.add(3);
        testArray.add(1);
        testArray.add(2);
        testArray.add(1);
        testArray.add(3);
        testArray.add(1);
        testArray.add(1);
        testArray.add(8);
        testArray.add(8);
        testArray.add(8);
        testArray.add(8);
        testArray.add(8);
        testArray.add(8);
        testArray.add(8);
        testArray.add(8);
        //int mostPopular = getPopularElement(testArray);

        //Log.d("TestingMethodStats", Integer.toString(mostPopular));

        user = (User) getIntent().getSerializableExtra("User");
        userCopy = (User) getIntent().getSerializableExtra("UserCopy");

        progressViewLabel = (TextView) findViewById(R.id.progressViewLabel);
        circleProgressView = (CircleProgressView) findViewById(R.id.circleProgressView);
        popularAccount = (TextView) findViewById(R.id.popularAccount);

        Calendar cal = Calendar.getInstance();
        String dateString = "";

        Log.d("In Stats ssssssssssssss", "Current YEAR is: " + cal.get(Calendar.YEAR));
        Log.d("In Stats ssssssssssssss", "Current MONTH is: " + cal.get(Calendar.MONTH));
        Log.d("In Stats ssssssssssssss", "Current DAY is: " + cal.get(Calendar.DATE));

        int currentYear = cal.get(Calendar.YEAR);
        int currentMonth = cal.get(Calendar.MONTH) + 1;

        if(cal.get(Calendar.MONTH) < 10){
            dateString = currentYear + "-0" + currentMonth;
        } else {
            dateString = currentYear + "-" + currentMonth;
        }

        Log.d("In Stats ssssssssssssss", "dateString is: " + dateString);

        double amountSpend = 0.0;
        int mostPopularAccount = 0;

        Log.d("TESTING IN STATS", Integer.toString(user.getAccounts().get(0).getTransactions().size()));

        for(int i = 0; i < user.getAccounts().get(0).getTransactions().size(); i++){
            Log.d("StartofFOR", user.getAccounts().get(0).getTransactions().get(i).getDate().substring(0, 7));
            Log.d("StartofFOR", dateString);
            if(user.getAccounts().get(0).getTransactions().get(i).getDate().substring(0, 7).equals(dateString)){
                if(user.getAccounts().get(0).getTransactions().get(i).getReceiver() != Integer.parseInt(user.getAccounts().get(0).getId())){
                    amountSpend = amountSpend + user.getAccounts().get(0).getTransactions().get(i).getAmount();
                }

            }
//            amountSpend = amountSpend + user.getAccounts().get(0).getTransactions().get(i).getAmount();
            Log.d("TESTINGnumberSTATS", Integer.toString(i));
            Log.d("TESTINGnumberSTATS", Double.toString(user.getAccounts().get(0).getTransactions().get(i).getAmount()));
        }

        Log.d("In Stats ssssssssssssss", "The amount spend in " + dateString + " = " + amountSpend);

        int favAccount = getPopularElement(user.getAccounts().get(0).getTransactions(), user.getAccounts().get(0));
        Log.d("TESTING_FAV STATS", Integer.toString(favAccount));

        backButton = (Button) findViewById(R.id.backStatsButton);

        progressViewLabel.setText("Amount spend in " + dateString);

        circleProgressView.setTextMode(TextMode.TEXT);
        circleProgressView.setText("£" + amountSpend);
        circleProgressView.setValue((float)amountSpend);

        popularAccount.setText(Integer.toString(favAccount));

        Log.d("In Stats", userCopy.getUsername());

        Log.d("In Stats", Double.toString(user.getAccounts().get(0).getTransactions().get(0).getAmount()));

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue queue = Volley.newRequestQueue(Stats.this);
                String url ="https://project-tobetodo.c9users.io/userLogin/" + userCopy.getUsername() + "/" + userCopy.getPassword();
                String url2 ="http://www.google.com";

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("TestingVolleyStats", "Response is: "+ response.toString());

                                User user = new User();
                                user = user.fromJson(response);
                                Log.d("TEST IN Stats", user.toString());
                                double test = user.getAccounts().get(0).getTransactions().get(0).getAmount();
                                Log.d("ANOTHER IN Stats", Double.toString(test));

                                user.setPassword( userCopy.getPassword());

                                Intent intent = new Intent(Stats.this, AccountLogin.class);
                                intent.putExtra("User", user);
                                finish();
                                startActivity(intent);
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("TestingVolleyStats", "That didn't work!");

                            }
                        });

// Access the RequestQueue through your singleton class.
                MySingleton.getInstance(Stats.this).addToRequestQueue(jsonObjectRequest);

            }
        });
    }

    private int getPopularElement(ArrayList<Transaction> a, Account account)
    {
        int count = 1, tempCount;
        int popular = a.get(0).getReceiver();
        int temp = 0;
        for (int i = 0; i < (a.size() - 1); i++)
        {
            if(a.get(i).getReceiver() == Integer.parseInt(account.getId()))
                continue;
            temp = a.get(i).getReceiver();
            tempCount = 0;
            for (int j = 1; j < a.size(); j++)
            {
                if (temp == a.get(j).getReceiver())
                    tempCount++;
            }
            if (tempCount > count)
            {
                popular = temp;
                count = tempCount;
            }
        }
        return popular;
    }
}

