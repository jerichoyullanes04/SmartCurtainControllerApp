package com.example.smartcurtaincontrollerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class ConnectWifiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_wifi);

        // Find the back button by its ID
        ImageView btnBack = findViewById(R.id.btn_back);

        // Find the "Connect" button by its ID
        Button btnConnectWifi = findViewById(R.id.btnConnectWifi);

        // Set an OnClickListener for the back button
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Simulate the system's back button behavior
                onBackPressed();
            }
        });

        // Set an OnClickListener for the "Connect" button
        btnConnectWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open Wi-Fi settings to connect to ESP32's Wi-Fi access point
                openWifiSettings();
            }
        });
    }

    private void openWifiSettings() {
        // Create an Intent to open the Wi-Fi settings
        Intent wifiSettingsIntent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);

        // Start the Wi-Fi settings activity
        startActivity(wifiSettingsIntent);
    }
}
