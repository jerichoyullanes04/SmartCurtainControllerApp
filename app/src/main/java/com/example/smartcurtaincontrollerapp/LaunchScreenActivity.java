package com.example.smartcurtaincontrollerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class LaunchScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_screen);

        new Handler().postDelayed(new  Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LaunchScreenActivity.this, HomeActivity.class);
                startActivity(intent); // Add this line to start the new activity
                finish(); // Add this line to finish the current activity
            }
        }, 2000);

    }
}