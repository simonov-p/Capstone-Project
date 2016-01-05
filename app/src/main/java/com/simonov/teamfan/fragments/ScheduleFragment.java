package com.simonov.teamfan.fragments;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import com.simonov.teamfan.R;
import com.simonov.teamfan.data.GamesContract;
import com.simonov.teamfan.objects.Event;
import com.simonov.teamfan.utils.Utilities;

/**
 * A placeholder fragment containing a simple view.
 */
public class ScheduleFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>,
        SharedPreferences.OnSharedPreferenceChangeListener
{
    private static final String TAG = ScheduleFragment.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private ScheduleAdapter mAdapter;
    private int mChoiceMode = AbsListView.CHOICE_MODE_NONE;


    public ScheduleFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView0");

        View root = inflater.inflate(R.layout.fragment_main, container, false);

        mRecyclerView = (RecyclerView) root.findViewById(R.id.my_recycler_view);


        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
//        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        TextView emptyView = (TextView) root.findViewById(R.id.empty_text_view);
        mAdapter = new ScheduleAdapter(getContext(), new ScheduleAdapter.ScheduleAdapterOnClickHandler() {
            @Override
            public void onClick(Event gameEvent, ScheduleAdapter.ViewHolder vh) {
                String team = Utilities.getPreferredTeam(getActivity());
                ((DetailFragmentCallback) getActivity())
                        .onGameSelected(gameEvent,
                                vh
                        );
            }
        }, emptyView, mChoiceMode);
        mRecyclerView.setAdapter(mAdapter);

        Log.d(TAG, "onCreateView1");

        if (savedInstanceState != null) {
            mAdapter.onRestoreInstanceState(savedInstanceState);
        }
        return root;

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // When tablets rotate, the currently selected list item needs to be saved.
        mAdapter.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    private static final String[] GAMES_COLUMNS = {
            GamesContract.GamesEntry.TABLE_NAME + "." + GamesContract.GamesEntry.COLUMN_GAME_ID,
            GamesContract.GamesEntry.COLUMN_DATE,
            GamesContract.GamesEntry.COLUMN_TEAM_NAME,
            GamesContract.GamesEntry.COLUMN_OPPONENT_NAME,
            GamesContract.GamesEntry.COLUMN_TEAM_SCORE,
            GamesContract.GamesEntry.COLUMN_OPPONENT_SCORE,
            GamesContract.GamesEntry.COLUMN_GAME_NBA_ID
    };

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader0");
        Log.d(TAG, "onCreateLoader0 id:" + id);

        String sortOrder = GamesContract.GamesEntry.COLUMN_DATE + " ASC";

        return new CursorLoader(getActivity(),
                GamesContract.GamesEntry.buildGamesUri(id),
                GAMES_COLUMNS,
                null,
                null,
                sortOrder
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "onLoadFinished0 data:" + data.getCount());
        mAdapter.swapCursor(data);
        updateEmptyView();
        if (data.getCount() == 0) {
            getLoaderManager().initLoader(0,null,null);
        } else {
            String sortOrder = GamesContract.GamesEntry.COLUMN_DATE + " ASC";

            String whereClause = GamesContract.GamesEntry.COLUMN_TEAM_SCORE + " = -1";

            Cursor c = getContext().getContentResolver().query(GamesContract.GamesEntry.CONTENT_URI,
                    null,
                    whereClause,
                    null,
                    sortOrder
                    );
            if (null != c && c.getCount() > 0) {
                int lastGamePosition = data.getCount() - c.getCount();
                if (lastGamePosition > 0) lastGamePosition--;
                mRecyclerView.scrollToPosition(lastGamePosition);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(TAG, "onLoaderReset0");

        mAdapter.swapCursor(null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated. Start loader.");

        getLoaderManager().initLoader(0, savedInstanceState, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_choose_team_key))) {
            updateEmptyView();
        }
    }

    private void updateEmptyView() {
        if (mAdapter.getItemCount() == 0) {
            TextView textView = (TextView) getView().findViewById(R.id.empty_text_view);
            if (null != textView){
                String message = "something wrong";
                textView.setText(message);

            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mRecyclerView) {
            mRecyclerView.clearOnScrollListeners();
        }
    }

    public void onTeamChanged() {
        getLoaderManager().restartLoader(0,null,this);
    }

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface DetailFragmentCallback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onGameSelected(Event gameEvent, ScheduleAdapter.ViewHolder vh);
    }
}
