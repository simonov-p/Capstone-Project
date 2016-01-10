package com.simonov.teamfan.fragments;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
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
    private InterstitialAd mInterstitialAd;


    public ScheduleFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        mInterstitialAd = new InterstitialAd(getContext());
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }
        });
        requestNewInterstitial();

        mRecyclerView = (RecyclerView) root.findViewById(R.id.my_recycler_view);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        TextView emptyView = (TextView) root.findViewById(R.id.empty_text_view);

        mAdapter = new ScheduleAdapter(getContext(), new ScheduleAdapter.ScheduleAdapterOnClickHandler() {
            @Override
            public void onClick(Event gameEvent, ScheduleAdapter.ViewHolder vh) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    ((DetailFragmentCallback) getActivity())
                            .onGameSelected(gameEvent,
                                    vh
                            );
                }
            }
        }, emptyView, mChoiceMode, false);
        mRecyclerView.setAdapter(mAdapter);

        if (savedInstanceState != null) {
            mAdapter.onRestoreInstanceState(savedInstanceState);
        }
        // paralax land
//        final View parallaxView = root.findViewById(R.id.parallax_bar);
//        if (null != parallaxView) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//                mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//                    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
//                    @Override
//                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                        super.onScrolled(recyclerView, dx, dy);
//                        int max = parallaxView.getHeight();
//                        if (dy > 0) {
//                            parallaxView.setTranslationY(Math.max(-max, parallaxView.getTranslationY() - dy / 2));
//                        } else {
//                            parallaxView.setTranslationY(Math.min(0, parallaxView.getTranslationY() - dy / 2));
//                        }
//                    }
//                });
//            }
//        }

        final AppBarLayout appbarView = (AppBarLayout) root.findViewById(R.id.appbar);
        if (null != appbarView) {
            ViewCompat.setElevation(appbarView, 0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        if (0 == mRecyclerView.computeVerticalScrollOffset()) {
                            appbarView.setElevation(0);
                        } else {
                            appbarView.setElevation(appbarView.getTargetElevation());
                        }
                    }
                });
            }
        }

        return root;

    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(getString(R.string.device_id))
                .build();

        mInterstitialAd.loadAd(adRequest);
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
            GamesContract.GamesEntry.COLUMN_GAME_NBA_ID,
            GamesContract.GamesEntry.COLUMN_TEAM_EVENTS_WON,
            GamesContract.GamesEntry.COLUMN_TEAM_EVENTS_LOST,
            GamesContract.GamesEntry.COLUMN_OPPONENT_EVENTS_WON,
            GamesContract.GamesEntry.COLUMN_OPPONENT_EVENTS_LOST,
            GamesContract.GamesEntry.COLUMN_EVENT_LOCATION_TYPE,
            GamesContract.GamesEntry.COLUMN_EVENT_LOCATION_NAME
    };

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String sortOrder = GamesContract.GamesEntry.COLUMN_DATE + " ASC";
        String selection = GamesContract.GamesEntry.COLUMN_TEAM_NAME + " = " + "'" +
                Utilities.getFullNameFromQuery(Utilities.getPreferredTeam(getContext())) + "'";

        return new CursorLoader(getActivity(),
                GamesContract.GamesEntry.buildScheduleUri(),
                null,
                selection,
                null,
                sortOrder
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d("mytag", "onLoadFinished0");
        mAdapter.swapCursor(data);
        updateEmptyView();
        scrollToLastGame();
    }

    private void scrollToLastGame(){
        String sortOrder = GamesContract.GamesEntry.COLUMN_DATE + " ASC";
        String selectionAllGames = GamesContract.GamesEntry.COLUMN_TEAM_NAME + " = " + "'" +
                Utilities.getFullNameFromQuery(Utilities.getPreferredTeam(getContext())) + "'";
        String selectionUnfinished = selectionAllGames + " and " + GamesContract.GamesEntry.COLUMN_TEAM_SCORE + " = -1";

        Cursor cursorUnfinishedGames = getContext().getContentResolver().query(GamesContract.GamesEntry.CONTENT_URI,
                null,
                selectionUnfinished,
                null,
                sortOrder
        );
        Cursor cursorAllGames = getContext().getContentResolver().query(GamesContract.GamesEntry.CONTENT_URI,
                null,
                selectionAllGames,
                null,
                sortOrder
        );
        if (null != cursorUnfinishedGames && cursorUnfinishedGames.getCount() > 0) {
            int lastGamePosition = cursorAllGames.getCount() - cursorUnfinishedGames.getCount();
            if (lastGamePosition > 0) lastGamePosition--;
            mRecyclerView.scrollToPosition(lastGamePosition);
            cursorAllGames.close();
            cursorUnfinishedGames.close();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(0, savedInstanceState, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_choose_team_key))) {
            updateEmptyView();
        }
    }

    @Override
    public void onResume() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sp.registerOnSharedPreferenceChangeListener(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sp.unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    private void updateEmptyView() {
        TextView textView = (TextView) getView().findViewById(R.id.empty_text_view);
        if (mAdapter.getItemCount() == 0) {
            if (null != textView) {
                // if cursor is empty, why? do we have an invalid location
                 textView.setText(Utilities.getErrorMessage(getContext()));
                textView.setVisibility(View.VISIBLE);
            }
        } else {
            Utilities.isNetworkAvailable(getActivity());
            textView.setVisibility(View.GONE);
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
        Log.d("mytag: ", "onTeamChanged");
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
