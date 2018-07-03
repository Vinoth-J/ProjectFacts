package com.example.wipro.projectfacts.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Movie;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.wipro.projectfacts.R;
import com.example.wipro.projectfacts.common.Constants;
import com.example.wipro.projectfacts.common.GlideApp;
import com.example.wipro.projectfacts.model.Fact;

import java.util.List;

import static com.bumptech.glide.load.engine.DiskCacheStrategy.*;
import static com.example.wipro.projectfacts.R.mipmap.ic_launcher;

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

/**
 * Adapter Class to populate Recycler view.
 */
public class FactsAdapter extends RecyclerView.Adapter<FactsAdapter.MyViewHolder> {

    /**
     * TAG name for class FactsAdapter
     */
    private static final String TAG = FactsAdapter.class.getSimpleName();
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

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle;
        TextView txtDescription;
        ImageView imgViewPicture;

        public MyViewHolder(View view) {
            super(view);
            txtTitle = (TextView) view.findViewById(R.id.txtTitle);
            txtDescription = (TextView) view.findViewById(R.id.txtDescription);
            imgViewPicture = (ImageView) view.findViewById(R.id.imgPicture);
        }
    }

    public FactsAdapter(Context context, List<Fact> factList) {
        mContext = context;
        mFactList = factList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {
        Fact fact = mFactList.get(position);
        String title = fact.getTitle();
        String description = fact.getDescription();
        String URL = fact.getImageURL();

        if (null == title && null == description && null == URL) {
            Log.e(TAG, "Title, Description and URL are NULL... so Skipped to Display in UI");
        } else {
            viewHolder.txtTitle.setText(title);
            viewHolder.txtDescription.setText((description == null) ? "No Description on this Fact" : description);

            // Load Image view from provided URL using Glide lib.
            /*Glide.with(mContext)
                        .setDefaultRequestOptions(mRequestOptions)
                        .load(URL).into(viewHolder.imgViewPicture);*/
            GlideApp.with(mContext)
                    .load(URL)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.imagenotfound)
                    .diskCacheStrategy(ALL)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            Log.e(TAG, "Error loading image");
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            Log.e(TAG, "Loading image : Success");
                            return false;
                        }
                    })
                    .into(viewHolder.imgViewPicture);
        }
    }


    @Override
    public int getItemCount() {
        return (mFactList == null) ? 0 : mFactList.size();
    }
}