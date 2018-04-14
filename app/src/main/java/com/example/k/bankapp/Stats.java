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

public class Stats extends AppCompatActivity {
    private Button backButton;
    private User user;
    private User userCopy;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        backButton = (Button) findViewById(R.id.backStatsButton);

        user = (User) getIntent().getSerializableExtra("User");
        userCopy = (User) getIntent().getSerializableExtra("UserCopy");

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
}

