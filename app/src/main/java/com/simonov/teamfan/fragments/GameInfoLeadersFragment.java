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
import com.simonov.teamfan.objects.Game;
import com.simonov.teamfan.objects.Player;
import com.simonov.teamfan.utils.Utilities;

public class GameInfoLeadersFragment extends Fragment {

    ImageView mTeamLogo;
    ImageView mOpponentLogo;
    TextView mTeamPlayer;
    TextView mOpponentPlayer;
    TextView mTeamPlayerPoints;
    TextView mOpponentPlayerPoints;

    public GameInfoLeadersFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_game_info_leaders, container, false);

        mTeamLogo = (ImageView) root.findViewById(R.id.home_logo);
        mOpponentLogo = (ImageView) root.findViewById(R.id.away_logo);

        mTeamPlayer = (TextView) root.findViewById(R.id.team_player_name);
        mOpponentPlayer = (TextView) root.findViewById(R.id.opponent_player_name);
        mTeamPlayerPoints = (TextView) root.findViewById(R.id.team_player_points);
        mOpponentPlayerPoints = (TextView) root.findViewById(R.id.opponent_player_points);
        return root;
    }

    public void fillViews(Game game) {
        Player teamPlayer = Utilities.getBestPlayer(game.home_stats);
        Player opponentPlayer = Utilities.getBestPlayer(game.away_stats);
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

        mTeamPlayer.setText(teamPlayer.display_name);
        mTeamPlayerPoints.setText(String.valueOf(teamPlayer.points));
        mOpponentPlayer.setText(opponentPlayer.display_name);
        mOpponentPlayerPoints.setText(String.valueOf(opponentPlayer.points));
    }
}
