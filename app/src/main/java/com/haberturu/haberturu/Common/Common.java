package com.haberturu.haberturu.Common;

import com.haberturu.haberturu.Interface.HaberService;
import com.haberturu.haberturu.Interface.IconBetterIdeaService;
import com.haberturu.haberturu.Remote.IconBetterIdeaClient;
import com.haberturu.haberturu.Remote.RetrofitClient;

public class Common {

    private static final String BASE_URL = "https://newsapi.org/";
    public static final String  API_KEY = "1f7e41099f9b49ad9bf3e09cec9abfee";

    public static HaberService getHaberService() {

        return RetrofitClient.getClient(BASE_URL).create(HaberService.class);
    }

    public static IconBetterIdeaService getIconService() {

        return IconBetterIdeaClient.getClient().create(IconBetterIdeaService.class);
    }

    public static String getAPIUrl(String source, String apiKey){

        StringBuilder apiUrl = new StringBuilder("https://newsapi.org/v2/everything?sources=");
        return apiUrl.append(source)
                .append("&apiKey=")
                .append(apiKey)
                .toString();
    }

    public static String getSearchAPIUrl(String source, String query, String apiKey){

        StringBuilder searchApiUrl = new StringBuilder("https://newsapi.org/v2/everything?sources=");
        return searchApiUrl.append(source)
                .append("&q=")
                .append(query)
                .append("&apiKey=")
                .append(apiKey)
                .toString();
    }

}
