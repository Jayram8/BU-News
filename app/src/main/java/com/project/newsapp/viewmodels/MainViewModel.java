package com.project.newsapp.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.newsapp.model.News;
import com.project.newsapp.model.TotalNews;
import com.project.newsapp.restapi.ApiClient;
import com.project.newsapp.restapi.RestInterface;
import com.project.newsapp.utils.Util;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends ViewModel {

    private MutableLiveData<List<News>> newsLiveData;
    private List<News> newsList;
    private String countryCode;
    private String apiKey;
    public List<News> mainNewsList = new ArrayList<>();

    public MainViewModel() {
        newsLiveData = new MutableLiveData<>();
        newsList = new ArrayList<>();
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
        getNews( "");
    }

    public MutableLiveData<List<News>> getNewsLiveData() {
        return newsLiveData;
    }

    private RestInterface getRestInterface() {
        RestInterface[] restInterface = new RestInterface[1];
        restInterface[0] = ApiClient.getClient(Util.API_BASE_URL).create(RestInterface.class);
        return restInterface[0];
    }

    public void getNews(String category) {
        newsList.clear();
        if (category == null || category.isEmpty() ) {
            fillNewsList(mainNewsList);
        } else {

            for (int i=0; i< mainNewsList.size(); i++) {
                if (category.equals(mainNewsList.get(i).getSource())){
                    newsList.add(mainNewsList.get(i));
                }
            }
            newsLiveData.setValue(newsList);
        }

    }

    private void getSearchedNews(String keyword) {

    }

    private void fillNewsList(List<News> totalNews) {
        newsList.addAll(totalNews);
        newsLiveData.setValue(newsList);
    }

    public void newsCategoryClick(Object category) {
        newsList.clear();
        getNews(String.valueOf(category));
    }

    public void searchNews(String keyword) {
        getSearchedNews(keyword);
    }
}
