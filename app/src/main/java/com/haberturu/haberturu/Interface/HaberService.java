package com.haberturu.haberturu.Interface;

import com.haberturu.haberturu.Common.Common;
import com.haberturu.haberturu.Model.KaynakWebSite;
import com.haberturu.haberturu.Model.News;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface HaberService {

    @GET("v2/sources?&apiKey="+ Common.API_KEY)
    Call<KaynakWebSite> getSources();

    @GET
    Call<News> getNewsArticle(@Url String url);

    @GET
    Call<News> getNewsSearch(@Url String url);
}
