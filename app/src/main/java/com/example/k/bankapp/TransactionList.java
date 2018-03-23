package com.example.k.bankapp;

import android.content.Intent;
import android.os.Bundle;
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

    public static final String TAG = "TransactionList";
    ListView mListView;
    User user;
    User userCopy;
    Button backButton;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_list);

        user = (User) getIntent().getSerializableExtra("User");
        userCopy = (User) getIntent().getSerializableExtra("UserCopy");
        mListView = (ListView)findViewById(R.id.listView);
        backButton = (Button)findViewById(R.id.backButton);

        Collections.reverse(user.getAccounts().get(0).getTransactions());

        TransactionListAdapter adapter = new TransactionListAdapter(this, R.layout.adapter_view_layout, user.getAccounts().get(0).getTransactions());
        mListView.setAdapter(adapter);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue queue = Volley.newRequestQueue(TransactionList.this);
                String url ="https://project-tobetodo.c9users.io/userLogin/" + userCopy.getUsername() + "/" + userCopy.getPassword();
                String url2 ="http://www.google.com";

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("TestingVolleyList", "Response is: "+ response.toString());

                                User user = new User();
                                user = user.fromJson(response);
                                Log.d("TEST IN TRANSFER", user.toString());
                                double test = user.getAccounts().get(0).getTransactions().get(0).getAmount();
                                Log.d("ANOTHER IN TRANSFER", Double.toString(test));

                                user.setPassword( userCopy.getPassword());

                                Intent intent = new Intent(TransactionList.this, AccountLogin.class);
                                intent.putExtra("User", user);
                                finish();
                                startActivity(intent);
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("TestingVolleyList", "That didn't work!");

                            }
                        });

                // Access the RequestQueue through your singleton class.
                MySingleton.getInstance(TransactionList.this).addToRequestQueue(jsonObjectRequest);
            }
        });
    }
}
