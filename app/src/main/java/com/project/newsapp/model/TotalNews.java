package com.project.newsapp.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TotalNews {

    @NonNull
    @SerializedName("results")
    private List<News> newsList;

    public TotalNews() {

    }


    @NonNull
    public List<News> getNewsList() {
        return newsList;
    }

    public void setNewsList(@NonNull List<News> newsList) {
        this.newsList = newsList;
    }
}
