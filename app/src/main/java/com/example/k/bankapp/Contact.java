package com.example.k.bankapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Contact extends AppCompatActivity {

    private Button mBackButton;
    private Button mBranchButton;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        init();
    }

    private void init(){
        mBackButton = (Button) findViewById(R.id.contactBackButton);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Contact.this, Home.class);
                finish();
                startActivity(intent);
            }
        });

        mBranchButton  = (Button) findViewById(R.id.branchButton);
        mBranchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Contact.this, Location.class);
                finish();
                startActivity(intent);
            }
        });
    }
}
