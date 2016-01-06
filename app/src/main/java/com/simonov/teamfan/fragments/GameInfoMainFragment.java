package com.simonov.teamfan.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.simonov.teamfan.R;
import com.simonov.teamfan.objects.Event;
import com.simonov.teamfan.objects.Game;
import com.simonov.teamfan.utils.Utilities;

public class GameInfoMainFragment extends Fragment {

//    @Bind(R.id.home_logo)
    ImageView mHomeLogo;
    ImageView mAwayLogo;
    TextView mHomeScore;
    TextView mAwayScore;

    public GameInfoMainFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_game_info_main, container, false);
        mHomeLogo = (ImageView) root.findViewById(R.id.home_logo);
        mAwayLogo = (ImageView) root.findViewById(R.id.away_logo);

        mHomeScore = (TextView) root.findViewById(R.id.home_score);
        mAwayScore = (TextView) root.findViewById(R.id.away_score);

        return root;
    }

    public void fillViews(Event event, Game game){
        Glide.with(getContext())
                .load(Utilities.getTeamLogo(getContext(), game.home_team.getFullName()))
                .error(R.mipmap.ic_launcher)
                .crossFade()
                .into(mHomeLogo);

        Glide.with(getContext())
                .load(Utilities.getTeamLogo(getContext(), game.away_team.getFullName()))
                .error(R.mipmap.ic_launcher)
                .crossFade()
                .into(mAwayLogo);

        mHomeScore.setText(String.valueOf(game.home_totals.points));
        mAwayScore.setText(String.valueOf(game.away_totals.points));
    }
}
