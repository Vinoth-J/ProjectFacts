package com.example.wipro.projectfacts;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Class for application control.
 */

public class FactsApplication extends Application {
    /**
     * TAG name for class FactsApplication
     */
    public static final String TAG = FactsApplication.class.getSimpleName();
    /**
     * Instance of FactsApplication
     */
    private static FactsApplication mInstance;
    /**
     * Instance of Request Queue
     */
    private RequestQueue mRequestQueue;

    /**
     * API to get instance of FactsApplication
     * @return FactsApplication
     */
    public static synchronized FactsApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"inside onCreate");
        mInstance = this;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}