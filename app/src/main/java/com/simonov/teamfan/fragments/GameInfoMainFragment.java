package com.simonov.teamfan.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.simonov.teamfan.BuildConfig;
import com.simonov.teamfan.R;
import com.simonov.teamfan.api.GameApi;
import com.simonov.teamfan.objects.Game;
import com.simonov.teamfan.objects.Player;
import com.simonov.teamfan.utils.Utilities;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class GameInfoMainFragment extends Fragment {

    ImageView mTeamLogo;
    ImageView mOpponentLogo;
    TextView mTeamScore;

    public GameInfoMainFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_game_info_main, container, false);
        mTeamLogo = (ImageView) root.findViewById(R.id.team_logo);
        mOpponentLogo = (ImageView) root.findViewById(R.id.opponent_logo);

        mTeamScore = (TextView) root.findViewById(R.id.team_score);

        return root;
    }

    public void fillViews(Game game){
        Log.d("mytag mGame.home_team.getFullName():", game.home_team.getFullName());
        Log.d("mytag mGame.away_team.getFullName():", game.away_team.getFullName());

        Glide.with(getContext())
                .load(Utilities.getTeamLogo(getContext(), game.home_team.getFullName()))
                .error(R.mipmap.ic_launcher)
                .crossFade()
                .into(mTeamLogo);

        Glide.with(getContext())
                .load(Utilities.getTeamLogo(getContext(), game.away_team.getFullName()))
                .error(R.mipmap.ic_launcher)
                .crossFade()
                .into(mOpponentLogo);
        mTeamScore.setText(String.format("%d:%d", game.away_period_scores.get(0), game.home_period_scores.get(0)));
    }

}
