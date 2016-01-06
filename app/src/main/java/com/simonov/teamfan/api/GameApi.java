package com.simonov.teamfan.api;

import com.simonov.teamfan.objects.Game;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.Callback;


/**
 * Created by petr on 04-Jan-16.
 */
public interface GameApi {
    String GAME_BOXSCORE = "/nba/boxscore/{event_id}.json";
    @GET(GAME_BOXSCORE)
    void getGameStats(@Path("event_id") String eventId, Callback<Game> response);

}
