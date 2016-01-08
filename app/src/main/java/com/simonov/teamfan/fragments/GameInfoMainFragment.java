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
import com.simonov.teamfan.R;
import com.simonov.teamfan.objects.Event;
import com.simonov.teamfan.objects.Game;
import com.simonov.teamfan.utils.Utilities;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GameInfoMainFragment extends Fragment {

    @Bind(R.id.home_logo) ImageView mHomeLogo;
    @Bind(R.id.away_logo) ImageView mAwayLogo;
    @Bind(R.id.home_score) TextView mHomeScore;
    @Bind(R.id.away_score) TextView mAwayScore;
    @Bind(R.id.away_games_status) TextView mAwayTeamStatus;
    @Bind(R.id.home_games_status) TextView mHomeTeamStatus;
    @Bind(R.id.away_FG_per) TextView mAwayFGP;
    @Bind(R.id.away_3P_per) TextView mAway3PP;
    @Bind(R.id.away_TO_per) TextView mAwayTO;
    @Bind(R.id.home_FG_per) TextView mHomeFGP;
    @Bind(R.id.home_3P_per) TextView mHome3PP;
    @Bind(R.id.home_TO_per) TextView mHomeTO;
    View mMainLayout;

    public GameInfoMainFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mMainLayout = inflater.inflate(R.layout.fragment_game_info_main, container, false);
        ButterKnife.bind(this, mMainLayout);

        return mMainLayout;
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
        mHomeFGP.setText(String.valueOf((int) (game.home_totals.field_goal_percentage * 100)));
        mHome3PP.setText(String.valueOf((int) (game.home_totals.field_goal_percentage * 100)));
        mHomeTO.setText(String.valueOf(game.home_totals.turnovers));

        mAwayScore.setText(String.valueOf(game.away_totals.points));
        mAwayFGP.setText(String.valueOf((int) (game.away_totals.field_goal_percentage * 100)));
        mAway3PP.setText(String.valueOf((int) (game.away_totals.three_point_percentage * 100)));
        mAwayTO.setText(String.valueOf(game.away_totals.turnovers));

        if (event.opponentName.equals(game.away_team.getFullName())){
            mAwayTeamStatus.setText(String.format(getString(R.string.win_loss_divider), event.opponent_events_won, event.opponent_events_lost));
            mHomeTeamStatus.setText(String.format(getString(R.string.win_loss_divider), event.team_events_won, event.team_events_lost));
        } else {
            mAwayTeamStatus.setText(String.format(getString(R.string.win_loss_divider), event.team_events_won, event.team_events_lost));
            mHomeTeamStatus.setText(String.format(getString(R.string.win_loss_divider), event.opponent_events_won, event.opponent_events_lost));
        }
        int ii = 0;
        for (int i : game.away_period_scores){
            ii++;
            Log.d("mytag", " "+ ii+":" + i + "-" + game.home_period_scores.get(ii-1));
        }
        mMainLayout.setContentDescription(String.format(getString(R.string.content_description_main_game_info),
                game.away_team.getFullName(),
                game.home_team.getFullName(),
                (int) (game.home_totals.field_goal_percentage * 100),
                game.home_team.getFullName(),
                (int) (game.away_totals.field_goal_percentage * 100),
                game.away_team.getFullName()
        ));
    }
}
