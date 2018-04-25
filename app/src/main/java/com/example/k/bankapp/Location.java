package com.example.k.bankapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Location extends AppCompatActivity implements OnMapReadyCallback {

    private Button mBackButton;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        init();
    }

    private void init(){
        // Fragment for displaying map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // Button to go back to Home screen
        mBackButton = (Button) findViewById(R.id.locationBackButton);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Location.this, Home.class);
                finish();
                startActivity(intent);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker for branch
        // and move the map's camera to the same location.
        LatLng branch = new LatLng(51.507, -0.127);
        float zoomLevel = 13.0f;
        googleMap.addMarker(new MarkerOptions().position(branch).title("Our Branch"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(branch, zoomLevel));
    }
}
