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
import com.simonov.teamfan.objects.Game;
import com.simonov.teamfan.objects.Player;
import com.simonov.teamfan.utils.Utilities;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GameInfoLeadersFragment extends Fragment {

    @Bind(R.id.home_player_name) TextView mHomePlayerName;
    @Bind(R.id.home_logo) ImageView mHomeLogo;
    @Bind(R.id.home_score) TextView mHomeScore;
    @Bind(R.id.home_FG_per) TextView mHomeFGP;
    @Bind(R.id.home_3P_per) TextView mHome3PP;
    @Bind(R.id.home_TO) TextView mHomeTO;
    @Bind(R.id.home_minutes) TextView mHomePlayerMinutes;
    @Bind(R.id.home_field_goal) TextView mHomePlayerFG;
    @Bind(R.id.home_3P) TextView mHomePlayer3P;
    @Bind(R.id.home_FT) TextView mHomePlayerFT;
    @Bind(R.id.home_FT_per) TextView mHomePlayerFTP;
    @Bind(R.id.home_rebounds) TextView mHomePlayerRebounds;
    @Bind(R.id.home_assists) TextView mHomePlayerAssists;
    @Bind(R.id.home_steals) TextView mHomePlayerSteals;
    @Bind(R.id.home_blocks) TextView mHomePlayerBlocks;
    @Bind(R.id.home_fouls) TextView mHomePlayerFouls;

    @Bind(R.id.away_player_name) TextView mAwayPlayerName;
    @Bind(R.id.away_logo) ImageView mAwayLogo;
    @Bind(R.id.away_score) TextView mAwayScore;
    @Bind(R.id.away_FG_per) TextView mAwayFGP;
    @Bind(R.id.away_3P_per) TextView mAway3PP;
    @Bind(R.id.away_TO) TextView mAwayTO;
    @Bind(R.id.away_minutes) TextView mAwayPlayerMinutes;
    @Bind(R.id.away_field_goal) TextView mAwayPlayerFG;
    @Bind(R.id.away_3P) TextView mAwayPlayer3P;
    @Bind(R.id.away_FT) TextView mAwayPlayerFT;
    @Bind(R.id.away_FT_per) TextView mAwayPlayerFTP;
    @Bind(R.id.away_rebounds) TextView mAwayPlayerRebounds;
    @Bind(R.id.away_assists) TextView mAwayPlayerAssists;
    @Bind(R.id.away_steals) TextView mAwayPlayerSteals;
    @Bind(R.id.away_blocks) TextView mAwayPlayerBlocks;
    @Bind(R.id.away_fouls) TextView mAwayPlayerFouls;
    View mMainLayout;

    public GameInfoLeadersFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mMainLayout = inflater.inflate(R.layout.fragment_game_info_leaders, container, false);
        ButterKnife.bind(this, mMainLayout);
        return mMainLayout;
    }

    public void fillViews(Game game) {
        Player homePlayer = Utilities.getBestPlayer(game.home_stats);
        Player awayPlayer = Utilities.getBestPlayer(game.away_stats);

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

        mAwayPlayerName.setText(String.valueOf(awayPlayer.display_name));
        mAwayScore.setText(String.valueOf(awayPlayer.points));
        mAwayFGP.setText(String.valueOf((int) (awayPlayer.field_goal_percentage * 100)));
        mAway3PP.setText(String.valueOf((int) (awayPlayer.three_point_percentage * 100)));
        mAwayTO.setText(String.valueOf(awayPlayer.turnovers));
        mAwayPlayerMinutes.setText(String.valueOf(awayPlayer.minutes));
        mAwayPlayerFG.setText(String.format(getString(R.string.shot_made_attempted_divider), awayPlayer.field_goals_made, awayPlayer.field_goals_attempted));
        mAwayPlayer3P.setText(String.format(getString(R.string.shot_made_attempted_divider), awayPlayer.three_point_field_goals_made, awayPlayer.three_point_field_goals_attempted));
        mAwayPlayerFT.setText(String.format(getString(R.string.shot_made_attempted_divider), awayPlayer.free_throws_made, awayPlayer.free_throws_attempted));
        mAwayPlayerFTP.setText(String.valueOf((int) (awayPlayer.free_throw_percentage * 100)));
        mAwayPlayerRebounds.setText(String.valueOf(awayPlayer.rebounds));
        mAwayPlayerAssists.setText(String.valueOf(awayPlayer.assists));
        mAwayPlayerSteals.setText(String.valueOf(awayPlayer.steals));
        mAwayPlayerFouls.setText(String.valueOf(awayPlayer.personal_fouls));
        mAwayPlayerBlocks.setText(String.valueOf(awayPlayer.blocks));

        mHomePlayerName.setText(String.valueOf(homePlayer.display_name));
        mHomeScore.setText(String.valueOf(homePlayer.points));
        mHomeFGP.setText(String.valueOf((int) (homePlayer.field_goal_percentage * 100)));
        mHome3PP.setText(String.valueOf((int) (homePlayer.three_point_percentage * 100)));
        mHomeTO.setText(String.valueOf(homePlayer.turnovers));
        mHomePlayerMinutes.setText(String.valueOf(homePlayer.minutes));
        mHomePlayerFG.setText(String.format(getString(R.string.shot_made_attempted_divider), homePlayer.field_goals_made, homePlayer.field_goals_attempted));
        mHomePlayer3P.setText(String.format(getString(R.string.shot_made_attempted_divider), homePlayer.three_point_field_goals_made, homePlayer.three_point_field_goals_attempted));
        mHomePlayerFT.setText(String.format(getString(R.string.shot_made_attempted_divider), homePlayer.free_throws_made, homePlayer.free_throws_attempted));
        mHomePlayerFTP.setText(String.valueOf((int) (homePlayer.free_throw_percentage * 100)));
        mHomePlayerRebounds.setText(String.valueOf(homePlayer.rebounds));
        mHomePlayerAssists.setText(String.valueOf(homePlayer.assists));
        mHomePlayerSteals.setText(String.valueOf(homePlayer.steals));
        mHomePlayerFouls.setText(String.valueOf(homePlayer.personal_fouls));
        mHomePlayerBlocks.setText(String.valueOf(homePlayer.blocks));

        mMainLayout.setContentDescription(String.format(getString(R.string.content_description_best_players),
                homePlayer.display_name,
                homePlayer.points,
                awayPlayer.display_name,
                awayPlayer.points));
        Log.d("mytag players1", awayPlayer.toString());
        Log.d("mytag players2", homePlayer.toString());
    }
}
