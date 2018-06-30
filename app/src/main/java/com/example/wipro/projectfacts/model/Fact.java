package com.example.wipro.projectfacts.model;

import com.example.wipro.projectfacts.common.Constants;
import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

/**
 * POJO class of Fact.
 */
public class Fact {
    @SerializedName(Constants.FACT_TITLE)
    private String mTitle;
    @SerializedName(Constants.FACT_DESCRIPTION)
    private String mDescription;
    @SerializedName(Constants.FACT_IMAGEHREF)
    private String mImageURL;

    /**
     * API to get title.
     * @return title
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * API to get Description.
     * @return description
     */
    public String getDescription() {
        return mDescription;
    }

    /**
     * API to get Image URL.
     * @return URL
     */
    public String getImageURL() {
        return mImageURL;
    }

}
