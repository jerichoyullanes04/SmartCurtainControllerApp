package com.example.smartcurtaincontrollerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ControllerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);

        // Check if the app was launched due to the Wi-Fi connection
        boolean wifiConnectionTriggered = getIntent().getBooleanExtra("wifi_connection_triggered", false);
        if (wifiConnectionTriggered) {
            // Handle the action when the app is launched due to Wi-Fi connection
            // Add your custom logic here
        }
    }
}