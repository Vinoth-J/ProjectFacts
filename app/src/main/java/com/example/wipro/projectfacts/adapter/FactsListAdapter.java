package com.example.wipro.projectfacts.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.wipro.projectfacts.R;
import com.example.wipro.projectfacts.common.Constants;
import com.example.wipro.projectfacts.model.Fact;

import java.util.List;

/**
 * Class to populate List view.
 */
public class FactsListAdapter extends BaseAdapter {
    /**
     * TAG name for class FactsListAdapter
     */
    private static final String TAG = FactsListAdapter.class.getSimpleName();
    /**
     * Instance of Layout inflater.
     */
    private LayoutInflater mLayoutInflater;
    /**
     * Instance of Context
     */
    private Context mContext;
    /**
     * Instance of Facts list.
     */
    private List<Fact> mFactList;

    public FactsListAdapter(Context context, List<Fact> factList) {
        mContext = context;
        mFactList = factList;
    }

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return mFactList.size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position) {
        return mFactList.get(position);
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (null == convertView) {
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mLayoutInflater.inflate(R.layout.layout_list_row, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Fact fact = (Fact) getItem(position);
        String title = fact.getTitle();
        String description = fact.getDescription();
        String URL = fact.getImageURL();

        if (null == title && null == description && null == URL) {
            Log.d(TAG, "Title, Description and URL are NULL... so Skipped to Display in UI");
        } else {
            viewHolder.txtTitle.setText(title);
            viewHolder.txtDescription.setText((description == null) ? "No Description on this Fact" : description);
            // Load Image view from provided URL using Glide lib.
            if (null != URL) {
                Glide.with(mContext).load(URL)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .fitCenter()
                        .override(Constants.WIDTH, Constants.HEIGHT)
                        .into(viewHolder.imgViewPicture);
            } else {
                Log.d(TAG, "Image not available");
            }
        }
        return convertView;
    }

    /**
     * Class of View Holder pattern.
     */
    private class ViewHolder {
        TextView txtTitle;
        TextView txtDescription;
        ImageView imgViewPicture;

        private ViewHolder(View convertView) {
            txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);
            txtDescription = (TextView) convertView.findViewById(R.id.txtDescription);
            imgViewPicture = (ImageView) convertView.findViewById(R.id.imgPicture);
        }
    }
}