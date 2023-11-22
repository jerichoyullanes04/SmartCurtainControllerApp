package com.example.smartcurtaincontrollerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.net.wifi.WifiInfo;
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

    private Button btnOpen, btnClose, btnSwitchMode, btnDisconnect, txtRES;
    private BroadcastReceiver wifiReceiver;

    TextView txtTemperature, txtHumidity, txtMode, txtConnection;

    private Handler handler = new Handler();
    private boolean isRepeating = false;

    private WifiManager wifiManager;

    private OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);

        // Initialize Button
        btnOpen = findViewById(R.id.btnOpen);
        btnClose = findViewById(R.id.btnClose);
        btnSwitchMode = findViewById(R.id.btnSwitchMode);
//        btnDisconnect = findViewById(R.id.btnDisconnect);

        // Initialize TextView
        txtTemperature = findViewById(R.id.txtTemperature);
        txtHumidity = findViewById(R.id.txtHumidity);
        txtMode = findViewById(R.id.txtMode);
        txtConnection = findViewById(R.id.txtConnection);

        wifiReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
                    NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                    if (networkInfo != null && networkInfo.isConnected()) {
                        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                        String ipAddress = intToIp(wifiInfo.getIpAddress());
                        if ("192.168.4.2".equals(ipAddress)) {
                            txtConnection.setText("Connected to Smart Curtain");
                        } else {
                            txtConnection.setText("Connected to other Wi-Fi");
                        }
                    } else {
                        txtConnection.setText("Not Connected");
                    }
                }
            }
        };

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

    }

// CUSTOM DEFINED FUNCTIONS

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        registerReceiver(wifiReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(wifiReceiver);
    }

    private String intToIp(int ipAddress) {
        return ((ipAddress & 0xFF) + "." +
                ((ipAddress >> 8) & 0xFF) + "." +
                ((ipAddress >> 16) & 0xFF) + "." +
                ((ipAddress >> 24) & 0xFF));
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
            }, 100); // Change the delay as needed
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
            }, 100); // Change the delay as needed
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

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txtMode.setText(cleanResponse);
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}