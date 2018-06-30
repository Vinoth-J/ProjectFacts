package com.example.wipro.projectfacts.ui;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.wipro.projectfacts.FactsApplication;
import com.example.wipro.projectfacts.R;
import com.example.wipro.projectfacts.adapter.FactsListAdapter;
import com.example.wipro.projectfacts.common.Constants;
import com.example.wipro.projectfacts.model.Fact;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

/**
 * Class to display list of Facts.
 */
public class FactsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    /**
     * TAG name for class FactsActivity.
     */
    private static final String TAG = FactsActivity.class.getSimpleName();
    /**
     * Instance of SwipeRefreshLayout.
     */
    private SwipeRefreshLayout mSwipeRefreshLayout;
    /**
     * Instance of ListView.
     */
    private ListView mListView;
    /**
     * Instance of GSON for Parsing JSON Object as Fact Object.
     */
    private Gson mGson = new GsonBuilder().serializeNulls().create();
    /**
     * list of Facts.
     */
    private List<Fact> mFactList;
    /**
     * Instance of Adapter to populate UI.
     */
    private FactsListAdapter mFactsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"Inside onCreate API");
        setContentView(R.layout.activity_facts);
        // Set Default Title before load from server.
        setTitle("Facts");
        mListView = (ListView) findViewById(R.id.listView_facts);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        if(null == mSwipeRefreshLayout) {
            Log.e(TAG,"mSwipeRefreshLayout is Null");
            return;
        }
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.post(new Runnable() {
                                     @Override
                                     public void run() {
                                         mSwipeRefreshLayout.setRefreshing(true);
                                         fetchFacts();
                                     }
                                 }
        );
    }

    /**
     * API to fetch Data from Server and populate List UI via Adapter.
     */
    private void fetchFacts() {
        Log.d(TAG,"Inside fetchFacts API");
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
                                int jsonLength = rowJsonArray.length();
                                Log.d(TAG, "rowJsonArray.length() :: " + jsonLength);

                                // Parsing JSON Array to List of Facts Object using GSON lib
                                if (jsonLength > 0) {
                                    Fact[] facts = mGson.fromJson(rowJsonArray.toString(), Fact[].class);
                                    if (null != facts && facts.length > 0) {
                                        mFactList = Arrays.asList(facts);
                                    }

                                    // Setting adapter to load List view
                                    mFactsListAdapter = new FactsListAdapter(FactsActivity.this, mFactList);
                                    mListView.setAdapter(mFactsListAdapter);
                                    mFactsListAdapter.notifyDataSetChanged();
                                    mSwipeRefreshLayout.setRefreshing(false);

                                }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mSwipeRefreshLayout.setRefreshing(false);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error : " + error);
            }
        });
        // Adding request to request queue
        FactsApplication.getInstance().addToRequestQueue(stringRequest);
    }

    /**
     * Called when a swipe gesture triggers a refresh
     */
    @Override
    public void onRefresh() {
        fetchFacts();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"Inside onStop API");
        // Cancelling pending request on stop of Activity
        FactsApplication.getInstance().cancelPendingRequests(FactsApplication.TAG);

    }
}
