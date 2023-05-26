package com.project.newsapp.activities;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.project.newsapp.R;
import com.project.newsapp.adapters.AdapterListNews;
import com.project.newsapp.clicklisteners.AdapterItemClickListener;
import com.project.newsapp.clicklisteners.NewsDialogClickListeners;
import com.project.newsapp.databinding.NewsDialogBinding;
import com.project.newsapp.model.News;
import com.project.newsapp.model.TotalNews;
import com.project.newsapp.utils.LocaleHelper;
import com.project.newsapp.utils.Util;
import com.project.newsapp.viewmodels.MainViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LifecycleOwner, AdapterItemClickListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ivToolbarCountry)
    ImageView ivToolbarCountry;


    MainActivity context;
    MainViewModel viewModel;
    AdapterListNews adapterListNews;
    List<News> newsList;

    private String firstControl = "firstControl";
    private String countryPositionPref = "countryPositionPref";
    SharedPreferences pref;
    private String[] countrys;
    private TypedArray countrysIcons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("response");


        pref = getApplicationContext().getSharedPreferences(Util.APP_NAME, MODE_PRIVATE);
        languageControl();
        setContentView(R.layout.activity_main);
        context = this;
        ButterKnife.bind(this);
        countrys = getResources().getStringArray(R.array.countrys);
        countrysIcons = getResources().obtainTypedArray(R.array.countrysIcons);

        initToolbar();


        TextView stockUpdate = findViewById(R.id.stockUpdate);
        stockUpdate.setSelected(true);

        if (pref.contains(countryPositionPref))
            ivToolbarCountry.setImageResource(countrysIcons.getResourceId(pref.getInt(countryPositionPref, 0), 0));

        viewModel = ViewModelProviders.of(context).get(MainViewModel.class);
        viewModel.getNewsLiveData().observe(context, newsListUpdateObserver);
        viewModel.setApiKey(getString(R.string.news_api_key));
        viewModel.setCountryCode(pref.getString(Util.COUNTRY_PREF, "tr"));
        newsList = new ArrayList<>();
        adapterListNews = new AdapterListNews(newsList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapterListNews);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Object object = dataSnapshot.getValue(Object.class);
                String json = new Gson().toJson(object);
                Log.d("NEWS123", "Value is: " + json.toString());
                TotalNews example= new Gson().fromJson(json, TotalNews.class);
                viewModel.mainNewsList.clear();
                viewModel.mainNewsList.addAll(example.getNewsList());
                newsList.clear();
                newsList.addAll(example.getNewsList());
                adapterListNews.notifyDataSetChanged();
                viewModel.getNews("business");
                Log.d("updated", "Value is: " + example.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w("NEWS123", "Failed to read value.", error.toException());
            }
        });



    }

    private void languageControl() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N && !pref.getBoolean(firstControl, false)) {
            Locale primaryLocale = getResources().getConfiguration().getLocales().get(0);
            LocaleHelper.setLocale(MainActivity.this, primaryLocale.getLanguage());
            int position = getLanguagePosition(primaryLocale.getLanguage());
            pref.edit().putInt(countryPositionPref, position).apply();
            pref.edit().putBoolean(firstControl, true).apply();
            recreate();
        }
    }

    private int getLanguagePosition(String displayLanguage) {
        String[] codes = getResources().getStringArray(R.array.countrysCodes);
        for (int i = 0; i < codes.length; i++) {
            if (codes[i].equals(displayLanguage)) return i;
        }
        return 0;
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle(null);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Util.setSystemBarColor(this, android.R.color.white);
        Util.setSystemBarLight(this);
    }

    private void showLanguageDialog() {
        new AlertDialog.Builder(this).setCancelable(false)
                .setTitle("Choose Country")
                .setSingleChoiceItems(countrys, pref.getInt(countryPositionPref, 0), null)
                .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss())
                .setPositiveButton(R.string.ok, (dialog, whichButton) -> {
                    int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                    pref.edit().putInt(countryPositionPref, selectedPosition).apply();
                    pref.edit().putString(Util.COUNTRY_PREF, getResources().getStringArray(R.array.countrysCodes)[selectedPosition]).apply();
                    LocaleHelper.setLocale(MainActivity.this, getResources().getStringArray(R.array.countrysCodes)[selectedPosition]);
                    recreate();
                    dialog.dismiss();
                })
                .show();
    }


    public void categoryClicked(View view) {
        viewModel.newsCategoryClick(String.valueOf(view.getTag()));
    }

    public void countryClick(View view) {
        showLanguageDialog();
    }

    Observer<List<News>> newsListUpdateObserver = new Observer<List<News>>() {
        @Override
        public void onChanged(List<News> news) {
            newsList.clear();
            if (news != null) {
                newsList.addAll(news);
            }
            adapterListNews.notifyDataSetChanged();
        }
    };


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    public void onNewsItemClick(News news) {
        showDialogPolygon(news);
    }

    private void showDialogPolygon(News news) {
        final Dialog dialog = new Dialog(this);
        NewsDialogBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getApplicationContext()), R.layout.dialog_header_polygon, null, false);
        binding.setNews(news);
        binding.setListener(new NewsDialogClickListeners() {
            @Override
            public void onGotoWebSiteClick(String url) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }

            @Override
            public void onDismissClick() {
                dialog.dismiss();
            }
        });

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(binding.getRoot());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);

        dialog.show();
    }

}
