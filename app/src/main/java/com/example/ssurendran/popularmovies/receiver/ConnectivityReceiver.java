package com.example.ssurendran.popularmovies.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by ssurendran on 3/15/18.
 */

public class ConnectivityReceiver extends BroadcastReceiver {

    private Context context;
    private boolean isRegistered;
    private ConnectivityCallback callback;

    public ConnectivityReceiver(Context context, ConnectivityCallback callback) {
        this.context = context;
        this.callback = callback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (isNetworkAvailable()){
            callback.onConnected();
        }
    }

    public void register(){
        if (!isRegistered) {
            context.registerReceiver(this, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
            isRegistered = true;
        }
    }

    public void unregister() {
        if (isRegistered) {
            context.unregisterReceiver(this);
            isRegistered = false;
        }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public interface ConnectivityCallback{
        void onConnected();
    }

}
