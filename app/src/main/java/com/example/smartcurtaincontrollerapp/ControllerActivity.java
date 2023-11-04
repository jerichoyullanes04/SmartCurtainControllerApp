package com.example.smartcurtaincontrollerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ControllerActivity extends AppCompatActivity {

    private Button btnOpen, btnClose, btnSwitchMode, btnDisconnect;
    TextView txtTemperature, txtHumidity, txtMode, txtConnection;

    private Handler handler = new Handler();
    private boolean isRepeating = false;

    private OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);

        // Initialize Button
        btnOpen = findViewById(R.id.btnOpen);
        btnClose = findViewById(R.id.btnClose);
        btnSwitchMode = findViewById(R.id.btnSwitchMode);
        btnDisconnect = findViewById(R.id.btnDisconnect);

        // Initialize TextView
        txtTemperature = findViewById(R.id.txtTemperature);
        txtHumidity = findViewById(R.id.txtHumidity);
        txtMode = findViewById(R.id.txtMode);
        txtConnection = findViewById(R.id.txtConnection);

        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkRequest.Builder builder = new NetworkRequest.Builder();
        builder.addTransportType(NetworkCapabilities.TRANSPORT_WIFI);
        builder.addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
        NetworkRequest request = builder.build();
        connManager.requestNetwork(request, new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    connManager.bindProcessToNetwork(network);
                }
            }
        });
        btnOpen.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Button is pressed, start the repeating action
                        isRepeating = true;
                        repeatOpen();
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        // Button is released, stop the repeating action
                        isRepeating = false;
                        break;
                }
                return true; // Consume the event
            }
        });

        btnClose.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Button is pressed, start the repeating action
                        isRepeating = true;
                        repeatClose();
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        // Button is released, stop the repeating action
                        isRepeating = false;
                        break;
                }
                return true; // Consume the event
            }
        });


        btnSwitchMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCommand("switch");
            }
        });

        btnDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disconnectFromWifi();
                //startActivity(new Intent(ControllerActivity.this  , HomeActivity.class));
            }
        });

    }

    private void repeatOpen() {
        if (isRepeating) {
            // Your action to be repeated goes here
            // For example, you can show a toast
            //Toast.makeText(this, "Action repeated", Toast.LENGTH_SHORT).show();

            sendCommand("open");

            // Schedule the next action after a delay (e.g., 500ms)
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    repeatOpen();
                }
            }, 500); // Change the delay as needed
        }
    }

    private void repeatClose() {
        if (isRepeating) {
            // Your action to be repeated goes here
            // For example, you can show a toast
            //Toast.makeText(this, "Action repeated", Toast.LENGTH_SHORT).show();

            sendCommand("close");

            // Schedule the next action after a delay (e.g., 500ms)
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    repeatClose();
                }
            }, 500); // Change the delay as needed
        }
    }

    private void disconnectFromWifi() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            wifiManager.disconnect();
        }
    }

    public void sendCommand(String cmd) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String command = "http://192.168.4.1/" + cmd;
                Log.d("Command----", command);
                Request request = new Request.Builder().url(command).build();
                try {
                    Response response = client.newCall(request).execute();
                    String myResponse = response.body().string();
                    final String cleanResponse = myResponse.replaceAll("\\<.*?\\>", ""); // remove HTML tags
                    cleanResponse.replace("\n", ""); // remove all new line characters
                    cleanResponse.replace("\r", ""); // remove all carriage characters
                    cleanResponse.replace(" ", ""); // removes all space characters
                    cleanResponse.replace("\t", ""); // removes all tab characters
                    cleanResponse.trim();
                    Log.d("Response  = ", cleanResponse);

//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            txtRES.setText(cleanResponse);
//                        }
//                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}