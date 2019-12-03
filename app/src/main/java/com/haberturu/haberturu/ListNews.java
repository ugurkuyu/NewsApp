package com.haberturu.haberturu;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.haberturu.haberturu.Adapter.ListNewsAdapter;
import com.haberturu.haberturu.Common.Common;
import com.haberturu.haberturu.Common.ISO8601Parse;
import com.haberturu.haberturu.Interface.HaberService;
import com.haberturu.haberturu.Model.Article;
import com.haberturu.haberturu.Model.News;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.haberturu.haberturu.Adapter.ListNewsAdapter.SPAN_COUNT_ONE;
import static com.haberturu.haberturu.Adapter.ListNewsAdapter.SPAN_COUNT_ONE_ALT;
import static com.haberturu.haberturu.Adapter.ListNewsAdapter.SPAN_COUNT_TWO;

public class ListNews extends AppCompatActivity {

    HaberService haberService;
    RelativeLayout relativeLayout;
    TextView news_title, news_source, news_space;
    RelativeTimeTextView news_time;
    ImageView imageView;
    SwipeRefreshLayout swipeRefreshLayout;
    private Menu menu;

    String source = "", webHotURL = "";

    ListNewsAdapter adapter;
    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_news);

        haberService = Common.getHaberService();

        news_title = findViewById(R.id.list_news_title_first);
        news_source = findViewById(R.id.list_news_source_first);
        news_time = findViewById(R.id.timestamp_first);
        imageView = findViewById(R.id.list_news_image_first);


        news_space = findViewById(R.id.space_tv);

        swipeRefreshLayout = findViewById(R.id.swipe_list_news);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadNews(source, null, true);
            }
        });

        relativeLayout = findViewById(R.id.relative);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detail = new Intent(getBaseContext(), DetailsNewsActivity.class);
                detail.putExtra("webUrl", webHotURL);
                detail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(detail);
            }
        });

        recyclerView = findViewById(R.id.list_news_rv);
        recyclerView.setHasFixedSize(true);
        gridLayoutManager = new GridLayoutManager(this, SPAN_COUNT_ONE);
        recyclerView.setLayoutManager(gridLayoutManager);

        if (getIntent() != null) {
            source = getIntent().getStringExtra("source");
            if (!source.isEmpty()) {
                loadNews(source, null, false);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;

        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        MenuItem menuItem = menu.findItem(R.id.menu_search);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint("Search News..");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() > 2) {
                    loadNews(source, query, false);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                loadNews(source, query, false);
                return false;
            }
        });

        menuItem.getIcon().setVisible(false, false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_switch_layout) {
            switchLayout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void switchLayout() {
        if (gridLayoutManager.getSpanCount() == SPAN_COUNT_ONE) {
            gridLayoutManager.setSpanCount(SPAN_COUNT_TWO);
            menu.findItem(R.id.menu_switch_layout).setIcon(R.drawable.ic_view_grid);

        } else if (gridLayoutManager.getSpanCount() == SPAN_COUNT_TWO) {
            gridLayoutManager.setSpanCount(SPAN_COUNT_ONE_ALT);
            menu.findItem(R.id.menu_switch_layout).setIcon(R.drawable.ic_view_normal);
        }

        adapter.notifyItemRangeChanged(0, adapter.getItemCount());
    }

    private void loadNews(final String source, final String query, boolean isRefreshed) {

        Call<News> call;

        if (query != null && query.length() > 0) {
            call = haberService.getNewsSearch(Common.getSearchAPIUrl(source, query, Common.API_KEY));
        } else {

            call = haberService.getNewsArticle(Common.getAPIUrl(source, Common.API_KEY));
        }

        if (!isRefreshed) {

            callLoad(call);

        } else {

            callLoad(call);
            swipeRefreshLayout.setRefreshing(false);

        }

    }

    private void callLoad(Call<News> call) {


        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if (response.body() != null && response.body().getArticles() != null
                        && response.body().getArticles().size() > 0
                        && !TextUtils.isEmpty(response.body().getArticles().get(0).getUrl())) {

                    Picasso.get()
                            .load(response.body().getArticles().get(0).getUrlToImage())
                            .fit()
                            .centerCrop()
                            .into(imageView);

                    if (response.body() != null && response.body().getArticles() != null
                            && response.body().getArticles().size() > 0
                            && !TextUtils.isEmpty(response.body().getArticles().get(0).getTitle())
                            && response.body().getArticles().get(0).getTitle().length() > 65) {
                        news_title.setText(response.body().getArticles().get(0).getTitle().substring(0, 65) + getString(R.string.uc_nokta));
                    } else {
                        news_title.setText(response.body().getArticles().get(0).getTitle());
                    }

                    if (response.body() != null && response.body().getArticles() != null
                            && response.body().getArticles().size() > 0
                            && !TextUtils.isEmpty(response.body().getArticles().get(0).getAuthor())
                            && response.body().getArticles().get(0).getAuthor().length() > 30) {
                        news_source.setText(response.body().getArticles().get(0).getAuthor().substring(0, 30) + getString(R.string.uc_nokta));
                    } else {
                        news_source.setText(response.body().getArticles().get(0).getAuthor());
                    }

                    Date date = null;
                    try {
                        date = ISO8601Parse.parse(response.body().getArticles().get(0).getPublishedAt());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    news_time.setReferenceTime(date.getTime());

                    webHotURL = response.body().getArticles().get(0).getUrl();


                    List<Article> all_article = response.body().getArticles();
                    all_article.remove(0);
                    adapter = new ListNewsAdapter(all_article, gridLayoutManager, getBaseContext());
                    adapter.notifyDataSetChanged();
                    recyclerView.setAdapter(adapter);


                }

            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {

            }
        });

    }
}
