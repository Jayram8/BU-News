package com.project.newsapp.model;


import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Fields {

    @NonNull
    @SerializedName("trailText")
    private String trailText;

    @NonNull
    @SerializedName("thumbnail")
    private String thumbnail;

    @NonNull
    public String getTrailText() {
        return trailText;
    }

    public void setTrailText(@NonNull String trailText) {
        this.trailText = trailText;
    }

    @NonNull
    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(@NonNull String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Fields() {
    }



}
