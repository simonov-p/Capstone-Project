package simonov.teamfan;

import android.support.v4.app.LoaderManager;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import simonov.teamfan.data.GamesContract;
import simonov.teamfan.sync.GamesSyncAdapter;

/**
 * A placeholder fragment containing a simple view.
 */
public class GamesFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = GamesFragment.class.getSimpleName();
    private static final int GAMES_LOADER = 0;
    private boolean mHoldForTransaction, mAutoSelectView;
    private int mChoiceMode = AbsListView.CHOICE_MODE_NONE;
    private int mPosition;

    @Bind(R.id.games_recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.empty_text_view)
    TextView mEmptyTextView;

    private GamesAdapter mGamesAdapter;

    public GamesFragment() {
    }

    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(Uri gameUri, GamesAdapter.ViewHolder vh);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView0");

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ButterKnife.bind(this, rootView);

        mRecyclerView.setHasFixedSize(true);

        mGamesAdapter = new GamesAdapter(getActivity(),
                new GamesAdapter.GamesAdapterOnClickHandler() {
                    @Override
                    public void onClick(Long game, GamesAdapter.ViewHolder vh) {
                        ((Callback) getActivity())
                                .onItemSelected(GamesContract.GamesEntry.buildGameUri(
                                                game),
                                        vh);

                    }
                }, mEmptyTextView, mChoiceMode);

        mRecyclerView.setAdapter(mGamesAdapter);

        GamesSyncAdapter.syncImmediately(getContext());
        Log.d(TAG, "onCreateView1");

        if (savedInstanceState != null) {
            mGamesAdapter.onRestoreInstanceState(savedInstanceState);
        }

        return rootView;
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
    public void onActivityCreated(Bundle savedInstanceState) {
        if (mHoldForTransaction) {
            getActivity().supportPostponeEnterTransition();
        }
        getLoaderManager().initLoader(GAMES_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader0");
        Log.d(TAG, "onCreateLoader0 id:" + id);

        String sortOrder = GamesContract.GamesEntry.COLUMN_DATE + " ASC";

        Cursor c = getContext().getContentResolver().query(
                GamesContract.GamesEntry.buildGamesUri(id),
                null,
                null,
                null,
                "");
        if (null != c) Log.d(TAG, "onCreateLoader0 c:" + c.getCount());


        return new CursorLoader(getActivity(),
                GamesContract.GamesEntry.buildGamesUri(id),
                GAMES_COLUMNS,
                null,
                null,
                sortOrder
        );
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "onLoadFinished0");
        Log.d(TAG, "data1" + data.getCount());

        mGamesAdapter.swapCursor(data);
        updateEmptyView();
        Log.d(TAG, "data2" + data.getCount());

        if (data.getCount() == 0) {
            Log.d(TAG, "data = 0");
            getActivity().supportStartPostponedEnterTransition();
        } else {
            mRecyclerView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    if (mRecyclerView.getChildCount() > 0) {
                        mRecyclerView.getViewTreeObserver().removeOnPreDrawListener(this);
                        int itemPosition = mGamesAdapter.getSelectedItemPosition();
                        if (RecyclerView.NO_POSITION == itemPosition) itemPosition = 0;
                        RecyclerView.ViewHolder vh = mRecyclerView.findViewHolderForAdapterPosition(itemPosition);
                        if (null != vh) {
                            mGamesAdapter.selectView(vh);
                        }
                        if (mHoldForTransaction) {
                            getActivity().supportStartPostponedEnterTransition();
                        }
                        Log.d(TAG, "onLoadFinished1");

                        return true;
                    }
                    Log.d(TAG, "onLoadFinished2");

                    return false;
                }
            });
        }
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        Log.d(TAG, "onLoaderReset0");
        mGamesAdapter.swapCursor(null);
        Log.d(TAG, "onLoaderReset1");
    }

    private void updateEmptyView() {
        if (mGamesAdapter.getItemCount() == 0) {
            TextView tv = (TextView) getView().findViewById(R.id.empty_text_view);
            tv.setText("no games");
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }
}
