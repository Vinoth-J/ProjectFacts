package com.example.wipro.projectfacts.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.wipro.projectfacts.FactsApplication;
import com.example.wipro.projectfacts.R;
import com.example.wipro.projectfacts.adapter.FactsAdapter;
import com.example.wipro.projectfacts.common.Constants;
import com.example.wipro.projectfacts.model.Fact;
import com.example.wipro.projectfacts.network.NetworkConnectivityReceiver;
import com.example.wipro.projectfacts.network.NetworkOperation;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

/**
 * Class to display list of Facts.
 */
public class FactsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, NetworkConnectivityReceiver.NetworkConnectivityListener {
    /**
     * TAG name for class FactsActivity.
     */
    private static final String TAG = FactsActivity.class.getSimpleName();
    /**
     * Instance of SwipeRefreshLayout.
     */
    private SwipeRefreshLayout mSwipeRefreshLayout;
    /**
     * Instance of Recycler view.
     */
    private RecyclerView mRecyclerView;
    /**
     * Instance of GSON for Parsing JSON Object as Fact Object.
     */
    private Gson mGson = new GsonBuilder().serializeNulls().create();
    /**
     * Instance of Adapter to populate UI.
     */
    private FactsAdapter mFactsAdapter;
    /**
     * Instance of Alert Dialog.
     */
    AlertDialog mAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Inside onCreate API");
        setContentView(R.layout.activity_facts);
        // Set Default Title before load from server.
        setTitle(R.string.default_title);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        if (null == mSwipeRefreshLayout) {
            Log.e(TAG, "mSwipeRefreshLayout is Null");
            return;
        }
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.post(new Runnable() {
                                     @Override
                                     public void run() {
                                         boolean isNetworkConnected = NetworkOperation.isNetworkConnected(FactsActivity.this);
                                         Log.d(TAG, "Is Network Connected :: " + isNetworkConnected);
                                         if (isNetworkConnected) {
                                             setRefreshing(true);
                                             fetchFacts();
                                         } else {
                                             showNetworkConnectionDialog();
                                             setRefreshing(false);
                                         }
                                     }
                                 }
        );
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "Inside onStart API");
        //Register for Network status change listener.
        setConnectivityListener(this);
    }

    /**
     * API to register for Network connection status.
     *
     * @param listener
     */
    private void setConnectivityListener(NetworkConnectivityReceiver.NetworkConnectivityListener listener) {
        NetworkConnectivityReceiver.mNetworkConnectivityReceiver = listener;
    }

    /**
     * API to fetch Data from Server and populate List UI via Adapter.
     */
    private void fetchFacts() {
        Log.d(TAG, "Inside fetchFacts API");
        // Request a string response from the requested URL using Volley lib.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_FACTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            // Updating Action bar Title from JSON Data
                            String titleJsonObject = jsonObject.getString(Constants.FACT_TITLE);
                            if (null != titleJsonObject && !TextUtils.isEmpty(titleJsonObject)) {
                                setTitle(titleJsonObject);
                            }
                            JSONArray rowJsonArray = jsonObject.getJSONArray(Constants.FACT_ROW);
                            List<Fact> factList = getFactsList(rowJsonArray);
                            // Setting adapter to load List view
                            if (null != factList && factList.size() > 0) {
                                mFactsAdapter = new FactsAdapter(FactsActivity.this, factList);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                                mRecyclerView.setLayoutManager(mLayoutManager);
                                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                                mRecyclerView.setAdapter(mFactsAdapter);

                                mFactsAdapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        setRefreshing(false);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error : " + error);
            }
        });
        // Adding request to request queue
        FactsApplication.getInstance().addToRequestQueue(stringRequest);
    }

    /**
     * Parsing JSON Array to List of Facts Object using GSON lib
     *
     * @param rowJsonArray
     * @return List of Facts
     */
    private List<Fact> getFactsList(JSONArray rowJsonArray) {
        List<Fact> factList = null;
        if (rowJsonArray == null) {
            Log.e(TAG, "JsonArray is Null");
        } else {
            int jsonLength = rowJsonArray.length();
            Log.d(TAG, "rowJsonArray.length() :: " + jsonLength);
            if (jsonLength > 0) {
                Fact[] facts = mGson.fromJson(rowJsonArray.toString(), Fact[].class);
                if (null != facts && facts.length > 0) {
                    factList = Arrays.asList(facts);
                }
            }
        }
        return factList;
    }

    /**
     * Called when a swipe gesture triggers a refresh
     */
    @Override
    public void onRefresh() {
        Log.d(TAG, "inside onRefresh");
        boolean isNetworkConnected = NetworkOperation.isNetworkConnected(this);
        Log.d(TAG, "Is Network Connected :: " + isNetworkConnected);
        if (isNetworkConnected) {
            setRefreshing(true);
            fetchFacts();
        } else {
            showNetworkConnectionDialog();
            if (null != mFactsAdapter) {
                mFactsAdapter.notifyDataSetChanged();
            }
            setRefreshing(false);
        }
    }

    /**
     * API to Refresh Swipe Refresh Layout.
     *
     * @param status true : Refresh; false : Abort Refresh.
     */
    private void setRefreshing(boolean status) {
        if (null != mSwipeRefreshLayout) {
            mSwipeRefreshLayout.setRefreshing(status);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "Inside onStop API");
        // Cancelling pending request on stop of Activity
        FactsApplication.getInstance().cancelPendingRequests(FactsApplication.TAG);
    }

    /**
     * API to show Alert dialog on No Network connection status.
     */
    private void showNetworkConnectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.no_network_connection);
        builder.setMessage(R.string.dialog_message);
        builder.setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                mAlertDialog = null;
            }
        });
        if (null == mAlertDialog) {
            mAlertDialog = builder.create();
        }
        if (!mAlertDialog.isShowing()) {
            mAlertDialog.show();
        }
    }

    /**
     * Notification Callback on change of Network status.
     *
     * @param isConnected true : Network Available; false : Network not available.
     */
    @Override
    public void onNetworkStatusChanged(boolean isConnected) {
        Log.d(TAG, "onNetworkStatusChanged - isConnected :: " + isConnected);
        if (isConnected) {
            setRefreshing(true);
            fetchFacts();
        } else {
            showNetworkConnectionDialog();
        }
    }
}
