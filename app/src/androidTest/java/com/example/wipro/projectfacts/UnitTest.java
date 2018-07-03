package com.example.wipro.projectfacts;

import android.support.test.filters.MediumTest;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.wipro.projectfacts.adapter.FactsAdapter;
import com.example.wipro.projectfacts.network.NetworkOperation;
import com.example.wipro.projectfacts.ui.FactsActivity;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Class to perform Unit Test.
 */
@MediumTest
@RunWith(AndroidJUnit4.class)
public class UnitTest {
    @Rule
    public ActivityTestRule<FactsActivity> rule  = new  ActivityTestRule<>(FactsActivity.class);

    @Test
    public void ensureRecyclerViewNotNull() throws Exception {
        FactsActivity activity = rule.getActivity();
        View recyclerView = activity.findViewById(R.id.recycler_view);
        assertThat(recyclerView,notNullValue());
    }

    @Test
    public void ensureRecyclerViewInstance() throws Exception {
        FactsActivity activity = rule.getActivity();
        View recyclerView = activity.findViewById(R.id.recycler_view);
        assertThat(recyclerView, instanceOf(RecyclerView.class));
    }

    @Test
    public void ensureRecyclerViewAdapterInstance() throws Exception {
        FactsActivity activity = rule.getActivity();
        Thread.sleep(4000);
        RecyclerView recyclerView = activity.findViewById(R.id.recycler_view);
        assertThat(recyclerView, instanceOf(RecyclerView.class));
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        assertThat(adapter, instanceOf(FactsAdapter.class));
    }

    @Test
    public void ensureAdapterItemCount() throws Exception {
        FactsActivity activity = rule.getActivity();
        Thread.sleep(4000);
        RecyclerView recyclerView = activity.findViewById(R.id.recycler_view);
        assertThat(recyclerView, instanceOf(RecyclerView.class));
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        assertThat(adapter, instanceOf(FactsAdapter.class));
        assertThat(adapter.getItemCount(), greaterThan(10));
    }

    @Test
    public void ensureNetworkSuccessStatus() throws Exception {
        FactsActivity activity = rule.getActivity();
        boolean isNetworkConnected = NetworkOperation.isNetworkConnected(activity.getApplicationContext());
        assertThat(isNetworkConnected,is(true));
    }

    @Test
    public void ensureNetworkFailStatus() throws Exception {
        FactsActivity activity = rule.getActivity();
        boolean isNetworkConnected = NetworkOperation.isNetworkConnected(activity.getApplicationContext());
        assertThat(isNetworkConnected,is(false));
    }

}
