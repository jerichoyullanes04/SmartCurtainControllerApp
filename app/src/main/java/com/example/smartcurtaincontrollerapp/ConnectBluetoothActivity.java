package com.example.smartcurtaincontrollerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class ConnectBluetoothActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_bluetooth);

        // Find the back button by its ID
        ImageView btnBack = findViewById(R.id.btn_back);

        // Set an OnClickListener for the back button
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Simulate the system's back button behavior
                onBackPressed();
            }
        });

    }
}