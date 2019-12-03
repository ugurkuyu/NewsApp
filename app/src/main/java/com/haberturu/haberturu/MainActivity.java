package com.haberturu.haberturu;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.google.gson.Gson;
import com.haberturu.haberturu.Adapter.ListSourceAdapter;
import com.haberturu.haberturu.Common.Common;
import com.haberturu.haberturu.Interface.HaberService;
import com.haberturu.haberturu.Model.KaynakWebSite;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView listSourceRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    HaberService haberService;
    ListSourceAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Paper.init(this);

        haberService = Common.getHaberService();

        listSourceRecyclerView = findViewById(R.id.listSourceRecyclerView);
        listSourceRecyclerView.setHasFixedSize(true);
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        listSourceRecyclerView.setLayoutManager(layoutManager);


        loadNewsSource(false);
    }



    private void loadNewsSource(boolean isRefreshed) {

        if (!isRefreshed) {
            String cache = Paper.book().read("cache");
            if (cache != null && !cache.isEmpty() && !cache.equals("null")) {

                KaynakWebSite webSite = new Gson().fromJson(cache, KaynakWebSite.class);
                adapter = new ListSourceAdapter(getBaseContext(), webSite);
                adapter.notifyDataSetChanged();
                listSourceRecyclerView.setAdapter(adapter);
                layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                listSourceRecyclerView.setLayoutManager(layoutManager);
            } else {

                haberService.getSources().enqueue(new Callback<KaynakWebSite>() {
                    @Override
                    public void onResponse(Call<KaynakWebSite> call, Response<KaynakWebSite> response) {
                        adapter = new ListSourceAdapter(getBaseContext(), response.body());
                        adapter.notifyDataSetChanged();
                        listSourceRecyclerView.setAdapter(adapter);
                        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                        listSourceRecyclerView.setLayoutManager(layoutManager);

                        Paper.book().write("cache", new Gson().toJson(response.body()));
                    }

                    @Override
                    public void onFailure(Call<KaynakWebSite> call, Throwable t) {

                    }
                });
            }
        } else {

            haberService.getSources().enqueue(new Callback<KaynakWebSite>() {
                @Override
                public void onResponse(Call<KaynakWebSite> call, Response<KaynakWebSite> response) {
                    adapter = new ListSourceAdapter(getBaseContext(), response.body());
                    adapter.notifyDataSetChanged();
                    listSourceRecyclerView.setAdapter(adapter);
                    layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                    listSourceRecyclerView.setLayoutManager(layoutManager);

                    Paper.book().write("cache", new Gson().toJson(response.body()));

                    swipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onFailure(Call<KaynakWebSite> call, Throwable t) {

                }
            });
        }
    }
}
