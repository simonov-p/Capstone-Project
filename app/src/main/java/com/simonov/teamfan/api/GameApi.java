package com.simonov.teamfan.api;

import com.simonov.teamfan.BuildConfig;
import com.simonov.teamfan.objects.Game;

import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.Path;
import retrofit.Callback;


/**
 * Created by petr on 04-Jan-16.
 */
public interface GameApi {
    @GET("/nba/boxscore/{event_id}.json")
    void getGameStats(@Path("event_id") String groupId, Callback<Game> response);

}
