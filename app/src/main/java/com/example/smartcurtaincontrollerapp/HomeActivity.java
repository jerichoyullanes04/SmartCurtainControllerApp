package com.example.smartcurtaincontrollerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class HomeActivity extends AppCompatActivity {

    Button btnConnect;
    Dialog popupDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize the Connect Button
        btnConnect = findViewById(R.id.btnConnect);

        // Initialize the popup dialog
        popupDialog = new Dialog(HomeActivity.this  );

        // Set the dialog content view to custom pop up box
        popupDialog.setContentView(R.layout.connect_popup_box);

        // Make the background of the dialog transparent
        popupDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Find the buttons in the popup layout
        ImageView btnClose = popupDialog.findViewById(R.id.btn_close);
        Button btnWifi = popupDialog.findViewById(R.id.btnWifi);
        Button btnBluetooth = popupDialog.findViewById(R.id.btnBluetooth);

        // Set a click listener for the close icon
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Close the popup window when the close icon is clicked
                popupDialog.dismiss();
            }
        });

        // Set click listener for the Wifi button in the popup
        btnWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to ConnectWifiActivity when the Wi-Fi button is clicked
                startActivity(new Intent(HomeActivity.this  , ConnectWifiActivity.class));
                popupDialog.dismiss(); // Close the popup window
            }
        });

        // Set click listener for the Bluetooth button in the popup
        btnBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to ConnectBluetoothActivity when the Bluetooth button is clicked
                startActivity(new Intent(HomeActivity.this  , ConnectBluetoothActivity.class));
                popupDialog.dismiss(); // Close the popup window
            }
        });

        // Find the root layout of the popup
        View popupRootView = popupDialog.findViewById(R.id.connect_popupbox_layout);

        // Set an OnTouchListener for the root layout to detect touches outside the popup
        popupRootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Check if the touch event is outside the popup window
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    // Close the popup window
                    popupDialog.dismiss();
                    return true; // Consume the touch event
                }
                return false; // Do not consume the touch event
            }
        });

        // Set an OnClickListener for the Connect button
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Show the custom popup dialog
                popupDialog.show();
            }
        });

    }
}