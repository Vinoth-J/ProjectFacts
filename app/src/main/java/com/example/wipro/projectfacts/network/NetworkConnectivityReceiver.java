package com.example.wipro.projectfacts.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Class to listen for Network connection status.
 */
public class NetworkConnectivityReceiver extends BroadcastReceiver {
    /**
     * TAG name for class NetworkConnectivityReceiver.
     */
    private static final String TAG = NetworkConnectivityReceiver.class.getSimpleName();
    /**
     * Instance of NetworkConnectivityListener.
     */
    public static NetworkConnectivityListener mNetworkConnectivityReceiver;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive");
        if (null == context) {
            Log.e(TAG, "Context is Null");
            return;
        }
        boolean isNetworkConnected = NetworkOperation.isNetworkConnected(context);
        if (null != mNetworkConnectivityReceiver) {
            mNetworkConnectivityReceiver.onNetworkStatusChanged(isNetworkConnected);
        }
    }

    /**
     * Interface to notify Network connection status.
     */
    public interface NetworkConnectivityListener {
        /**
         * Notification Callback on change of Network status.
         *
         * @param isConnected true : Network Available ; false : Network not available.
         */
        void onNetworkStatusChanged(boolean isConnected);
    }
}
