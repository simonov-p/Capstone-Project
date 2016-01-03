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
import android.widget.TextView;

import com.simonov.teamfan.R;
import com.simonov.teamfan.data.GamesContract;

import org.w3c.dom.Text;

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

//        // specify an adapter (see also next example)
//        char[] myDataset;
//        String s = "izhorec";
//        myDataset = s.toCharArray();

        TextView emptyView = (TextView) root.findViewById(R.id.empty_text_view);
        mAdapter = new ScheduleAdapter(getContext(), emptyView);
        mRecyclerView.setAdapter(mAdapter);

        Log.d(TAG, "onCreateView1");
        return root;

    }

    private static final String[] GAMES_COLUMNS = {
            GamesContract.GamesEntry.TABLE_NAME + "." + GamesContract.GamesEntry.COLUMN_GAME_ID,
            GamesContract.GamesEntry.COLUMN_DATE,
            GamesContract.GamesEntry.COLUMN_HOME,
            GamesContract.GamesEntry.COLUMN_AWAY,
            GamesContract.GamesEntry.COLUMN_HOME_SCORE,
            GamesContract.GamesEntry.COLUMN_AWAY_SCORE,
            GamesContract.GamesEntry.COLUMN_GAME_NBA_ID
    };
    static final int COL_GAME_ID = 0;
    static final int COL_DATE = 1;
    static final int COL_HOME = 2;
    static final int COL_AWAY = 3;
    static final int COL_HOME_SCORE = 4;
    static final int COL_AWAY_SCORE = 5;
    static final int COL_GAME_NBA_ID = 6;

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader0");
        Log.d(TAG, "onCreateLoader0 id:" + id);

        String sortOrder = GamesContract.GamesEntry.COLUMN_DATE + " ASC";

//        Cursor c = getContext().getContentResolver().query(
//                GamesContract.GamesEntry.buildGamesUri(id),
//                null,
//                null,
//                null,
//                "");
//        if (null != c) Log.d(TAG, "onCreateLoader0 c:" + c.getCount());


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
}
