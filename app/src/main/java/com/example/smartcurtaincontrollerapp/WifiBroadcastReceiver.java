package com.example.smartcurtaincontrollerapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.example.smartcurtaincontrollerapp.ControllerActivity;

public class WifiBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (action != null && action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
            // Check the Wi-Fi network state
            NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (networkInfo != null && networkInfo.isConnected()) {
                // Get the connected Wi-Fi SSID
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ssid = wifiInfo.getSSID().replace("\"", ""); // Remove double quotes

                // Check if the connected Wi-Fi is "Smart Curtain"
                if ("Smart Curtain".equals(ssid)) {
                    // Start the ControllerActivity with a flag indicating it was triggered by Wi-Fi connection
                    Intent controllerIntent = new Intent(context, ControllerActivity.class);
                    controllerIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    controllerIntent.putExtra("wifi_connection_triggered", true); // Add this flag
                    context.startActivity(controllerIntent);
                }
            }
        }
    }
}
