package com.simonov.teamfan.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.simonov.teamfan.BuildConfig;
import com.simonov.teamfan.R;
import com.simonov.teamfan.api.GameApi;
import com.simonov.teamfan.api.RestError;
import com.simonov.teamfan.fragments.GameInfoLeadersFragment;
import com.simonov.teamfan.fragments.GameInfoMainFragment;
import com.simonov.teamfan.fragments.GameInfoPreviewFragment;
import com.simonov.teamfan.fragments.GameInfoPreviousFragment;
import com.simonov.teamfan.fragments.GamesInfoMapFragment;
import com.simonov.teamfan.fragments.ScheduleAdapter;
import com.simonov.teamfan.fragments.ScheduleFragment;
import com.simonov.teamfan.objects.Event;
import com.simonov.teamfan.objects.Game;
import com.simonov.teamfan.utils.Utilities;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DetailActivity extends AppCompatActivity
implements ScheduleFragment.DetailFragmentCallback {
    private static final String TAG = DetailActivity.class.getSimpleName();

    private Event mGameEvent;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private boolean mGameFinished;
    private TextView mEmptyView;

    private AdView mAdView;

    private ArrayList<Fragment> mListFragment = new ArrayList<>();

    public static String sendEvent = "send event";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mAdView = (AdView) findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(getString(R.string.device_id))
                .build();
        mAdView.loadAd(adRequest);

        mEmptyView = (TextView) findViewById(R.id.empty_text_view);

        mGameEvent = getIntent().getParcelableExtra(MainActivity.SEND_GAME_ID);
        Bundle bundle = new Bundle();
        bundle.putParcelable(sendEvent, mGameEvent);
        Log.d("mytag", mGameEvent.getEventId());
        mGameFinished = Utilities.compareDate(mGameEvent.getEventStartDateTime()) && mGameEvent.getOpponentPointsScored() >= 0;
        if (mGameFinished) {
            mListFragment.add(new GameInfoMainFragment());
            mListFragment.add(new GameInfoLeadersFragment());
            if (Utilities.isNetworkAvailable(this)){
                getGameInfo(mGameEvent.getEventId());
            } else {
                updateEmptyView();
            }
        } else {

            Fragment previewFragment = new GameInfoPreviewFragment();
            previewFragment.setArguments(bundle);
            mListFragment.add(previewFragment);

            Fragment mapFragment = new GamesInfoMapFragment();
            mapFragment.setArguments(bundle);
            mListFragment.add(mapFragment);
        }

        Fragment previousFragment = new GameInfoPreviousFragment();
        previousFragment.setArguments(bundle);
        mListFragment.add(previousFragment);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }

    private void updateEmptyView() {
        mEmptyView.setVisibility(View.VISIBLE);
        mEmptyView.setText(Utilities.getErrorMessage(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        menuItem.setIntent(createShareForecastIntent());
        return true;
    }

    private Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mGameEvent.toString());
        return shareIntent;
    }

    @Override
    public void onGameSelected(Event gameEvent, ScheduleAdapter.ViewHolder vh) {
        Intent intent = new Intent(this, DetailActivity.class)
                .putExtra(MainActivity.SEND_GAME_ID, gameEvent);

        ActivityOptionsCompat activityOptions =
                ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                        new Pair<View, String>(vh.mAwayTeamLogo, getString(R.string.detail_icon_transition_name)));
        ActivityCompat.startActivity(this, intent, activityOptions.toBundle());
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mListFragment.get(position);
        }

        @Override
        public int getCount() {
            return mListFragment.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return mGameFinished ? getString(R.string.label_main_info_fragment) : getString(R.string.label_preview_game_fragment);
                case 1:
                    return mGameFinished ? getString(R.string.label_leaders_fragment) : getString(R.string.label_map_game_fragment);
                case 2:
                    return getString(R.string.label_previous_games_fragment);
            }
            return null;
        }
    }

    private void getGameInfo(String gameIdNBA){
        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(getString(R.string.base_url))
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addHeader("Authorization", "Bearer " + BuildConfig.XMLSTATS_ACCESS_TOKEN);
                        request.addHeader("User-agent", "simonovP/0.1 (https://pk.simonov@gmail.com/)");
//                        request.addHeader("Accept-encoding", "gzip"); //com.google.gson.JsonSyntaxException: java.lang.IllegalStateException: Expected BEGIN_OBJECT but was STRING at line 1 column 1 path $
                    }
                })
                .build();

        GameApi gameApi = adapter.create(GameApi.class);

        gameApi.getGameStats(
                gameIdNBA,
                new Callback<Game>() {
                    @Override
                    public void success(Game game, Response response) {
                        ((GameInfoMainFragment) mListFragment.get(0)).fillViews(mGameEvent, game);
                        ((GameInfoLeadersFragment) mListFragment.get(1)).fillViews(game);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("mytag error", error.toString());
                        Log.d("mytag error.getUrl", error.getUrl().toString());
//                        Log.d("mytag error.getBody().toString", error.getBody().toString());
                        Log.d("mytag error.getMessage", error.getMessage());
//                        Log.d("mytag error.getLocalizedMessage", error.getLocalizedMessage());
//                        Log.d("mytag error.getResponse().toString", error.getResponse().toString());
//                        Log.d("mytag error.getResponse().getReason", error.getResponse().getReason());
                        RestError body = (RestError) error.getBodyAs(RestError.class);
                        Log.d("mytag error.body.code", body.error.code);
                        Log.d("mytag error.body.errorDetails", body.error.description);
                    }
                });
    }

    /** Called when leaving the activity */
    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    /** Called when returning to the activity */
    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }
}
