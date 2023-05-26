package com.project.newsapp.model;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class News {

    @NonNull
    @SerializedName("sectionId")
    private String source;

    @NonNull
    @SerializedName("webTitle")
    private String newsTitle;

    @NonNull
    @SerializedName("description")
    private String newsDescription;

    @NonNull
    @SerializedName("webUrl")
    private String newsUrl;

    @NonNull
    @SerializedName("thumbnail")
    private String thumbnail;

    @NonNull
    @SerializedName("webPublicationDate")
    private String webPublicationDate;

    @NonNull
    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(@NonNull String newsTitle) {
        this.newsTitle = newsTitle;
    }

    @NonNull
    public String getNewsDescription() {
        return newsDescription;
    }

    public void setNewsDescription(@NonNull String newsDescription) {
        this.newsDescription = newsDescription;
    }

    @NonNull
    public String getWebPublicationDate() {
        return webPublicationDate;
    }

    public void setWebPublicationDate(@NonNull String webPublicationDate) {
        this.webPublicationDate = webPublicationDate;
    }

    @NonNull
    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(@NonNull String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @NonNull
    public String getSource() {
        return source;
    }

    public void setSource(@NonNull String source) {
        this.source = source;
    }

    @NonNull
    public String getNewsUrl() {
        return newsUrl;
    }

    public void setNewsUrl(@NonNull String newsUrl) {
        this.newsUrl = newsUrl;
    }


    //Image Binding - I didn't write newsviewmodel for just this method
    @BindingAdapter({"bind:imgUrl"})
    public static void setImage(ImageView imageView, String imgUrl) {
        Glide.with(imageView.getContext()).load(imgUrl).into(imageView);
    }

}
