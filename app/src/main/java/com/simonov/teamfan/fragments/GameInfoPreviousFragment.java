package com.simonov.teamfan.fragments;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import com.simonov.teamfan.R;
import com.simonov.teamfan.activities.DetailActivity;
import com.simonov.teamfan.data.GamesContract;
import com.simonov.teamfan.objects.Event;
import com.simonov.teamfan.utils.Utilities;

/**
 * A simple {@link Fragment} subclass.
 */
public class GameInfoPreviousFragment extends Fragment {
    private static final String TAG = GameInfoPreviousFragment.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private ScheduleAdapter mAdapter;
    private int mChoiceMode = AbsListView.CHOICE_MODE_NONE;
    private Event mEvent;

    public GameInfoPreviousFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_game_info_previous, container, false);

        Bundle bundle = this.getArguments();
        mEvent = bundle.getParcelable(DetailActivity.sendEvent);

        mRecyclerView = (RecyclerView) root.findViewById(R.id.my_recycler_view);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        TextView emptyView = (TextView) root.findViewById(R.id.empty_text_view);

        mAdapter = new ScheduleAdapter(getContext(), new ScheduleAdapter.ScheduleAdapterOnClickHandler() {
            @Override
            public void onClick(Event gameEvent, ScheduleAdapter.ViewHolder vh) {
                ((ScheduleFragment.DetailFragmentCallback) getActivity())
                        .onGameSelected(gameEvent,
                                vh
                        );
            }
        }, emptyView, mChoiceMode, true);
        fillAdapter();
        mRecyclerView.setAdapter(mAdapter);

        if (savedInstanceState != null) {
            mAdapter.onRestoreInstanceState(savedInstanceState);
        }
        return root;
    }

    private void fillAdapter(){
        String sortOrder = GamesContract.GamesEntry.COLUMN_DATE + " ASC";
        String selectionAllGames = GamesContract.GamesEntry.COLUMN_TEAM_NAME + " = " + "'" +
                Utilities.getFullNameFromQuery(Utilities.getPreferredTeam(getContext())) + "'";
        String selectionWithOpponent = selectionAllGames + " and " +
                GamesContract.GamesEntry.COLUMN_OPPONENT_NAME + " = " + "'" + mEvent.opponentName + "'";
        Cursor cursorWithOpponent = getContext().getContentResolver().query(GamesContract.GamesEntry.CONTENT_URI,
                null,
                selectionWithOpponent,
                null,
                sortOrder
        );
        mAdapter.swapCursor(cursorWithOpponent);
    }
}
