package com.example.a4reddit.data;

import com.example.a4reddit.data.model.Feed;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by George Kimutai on 3/28/2019.
 */
public interface APIfeed {

    @GET("{feed_name}/.rss")
    Call<Feed> listFeed(@Path("feed_name") String feed_name);
    @GET("{type}/.rss")
    Call<Feed>getSubs(@Path("type") String type);
}
