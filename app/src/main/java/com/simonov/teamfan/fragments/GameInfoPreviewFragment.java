package com.simonov.teamfan.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.simonov.teamfan.R;
import com.simonov.teamfan.objects.Event;
import com.simonov.teamfan.utils.Utilities;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class GameInfoPreviewFragment extends Fragment {

    private final Event mEvent;
    @Bind(R.id.away_logo) ImageView mAwayLogo;
    @Bind(R.id.home_logo) ImageView mHomeLogo;
    @Bind(R.id.away_games_status) TextView mAwayTeamStatus;
    @Bind(R.id.home_games_status) TextView mHomeTeamStatus;
    @Bind(R.id.game_date) TextView mGameDate;
    @Bind(R.id.game_time) TextView mGameTime;
    @Bind(R.id.game_location) TextView mGameLocation;
    @Bind(R.id.fab) FloatingActionButton mFloatButton;

    public GameInfoPreviewFragment(Event event) {
        mEvent = event;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_game_info_preview, container, false);

        ButterKnife.bind(this,root);

        mGameDate.setText(Utilities.convertDateToDate(mEvent.getEventStartDateTime()));
        mGameTime.setText(Utilities.convertDateToTime(mEvent.getEventStartDateTime()));
        mGameLocation.setText(mEvent.eventLocationName);

        if (Utilities.isHomeGame(mEvent)) {
            Glide.with(getContext())
                    .load(Utilities.getTeamLogo(getContext(), mEvent.teamName))
                    .error(R.mipmap.ic_launcher)
                    .crossFade()
                    .into(mHomeLogo);
            Glide.with(getContext())
                    .load(Utilities.getTeamLogo(getContext(), mEvent.opponentName))
                    .error(R.mipmap.ic_launcher)
                    .crossFade()
                    .into(mAwayLogo);
            mHomeTeamStatus.setText(String.format("%s-%s", mEvent.team_events_won, mEvent.team_events_lost));
            mAwayTeamStatus.setText(String.format("%s-%s", mEvent.opponent_events_won, mEvent.opponent_events_lost));
        } else {
            Glide.with(getContext())
                    .load(Utilities.getTeamLogo(getContext(), mEvent.opponentName))
                    .error(R.mipmap.ic_launcher)
                    .crossFade()
                    .into(mHomeLogo);
            Glide.with(getContext())
                    .load(Utilities.getTeamLogo(getContext(), mEvent.teamName))
                    .error(R.mipmap.ic_launcher)
                    .crossFade()
                    .into(mAwayLogo);
            mHomeTeamStatus.setText(String.format("%s-%s", mEvent.opponent_events_won, mEvent.opponent_events_lost));
            mAwayTeamStatus.setText(String.format("%s-%s", mEvent.team_events_won, mEvent.team_events_lost));
        }

        mFloatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Calendar beginTime = Calendar.getInstance();
//                beginTime.set(2016, 0, 10, 7, 30);
//                Calendar endTime = Calendar.getInstance();
//                endTime.set(2016, 0, 10, 8, 30);
                Intent intent = new Intent(Intent.ACTION_INSERT)
                        .setData(CalendarContract.Events.CONTENT_URI)
                        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, Utilities.convertDateToMillis(mEvent.getEventStartDateTime()))
                        .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, Utilities.convertDateEndGameToMillis(mEvent.getEventStartDateTime()))
                        .putExtra(CalendarContract.Events.TITLE, mEvent.getEventId())
                        .putExtra(CalendarContract.Events.DESCRIPTION, "NBA")
                        .putExtra(CalendarContract.Events.EVENT_LOCATION, mEvent.eventLocationName)
                        .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
                startActivity(intent);
            }
        });

        return root;
    }
}
