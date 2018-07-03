package com.example.wipro.projectfacts.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Class to perform Network Operations.
 */
public class NetworkOperation {
    /**
     * TAG name for class NetworkOperation.
     */
    private static final String TAG = NetworkOperation.class.getSimpleName();

    /**
     * API to check Network Connection status.
     * @param context : Context
     * @return true : Network connected ; false : Network not connected.
     */
    public static boolean isNetworkConnected(Context context) {
        if (null == context) {
            Log.e(TAG, "Context is NULL");
            return false;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
