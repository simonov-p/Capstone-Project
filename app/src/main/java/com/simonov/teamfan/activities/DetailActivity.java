package com.simonov.teamfan.activities;

import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.simonov.teamfan.BuildConfig;
import com.simonov.teamfan.R;
import com.simonov.teamfan.api.GameApi;
import com.simonov.teamfan.fragments.GameInfoLeadersFragment;
import com.simonov.teamfan.fragments.GameInfoMainFragment;
import com.simonov.teamfan.objects.Game;
import com.simonov.teamfan.objects.Player;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DetailActivity extends AppCompatActivity {
    private String mGameId;

    private static final String TAG = DetailActivity.class.getSimpleName();
    /**
     * The {@link PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    static String s = "none";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private GameInfoLeadersFragment mLeadersFragment;
    private GameInfoMainFragment mMainFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mLeadersFragment = new GameInfoLeadersFragment();
        mMainFragment = new GameInfoMainFragment();

        mGameId = getIntent().getStringExtra(MainActivity.SEND_GAME_ID);
        Log.d(TAG, "gameIdNBA:" + mGameId);
        getGameInfo(mGameId);

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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if (position == 0) {
                return mMainFragment;
            } else if (position == 1){
                return mLeadersFragment;
            }
            return mMainFragment;
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "MAIN";
                case 1:
                    return "LEADERS";
            }
            return null;
        }
    }

    private void getGameInfo(String gameIdNBA){
        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint("https://erikberg.com")
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestInterceptor.RequestFacade request) {
                        request.addHeader("Accept", "application/json;versions=1");
                        request.addHeader("Authorization", "Bearer " + BuildConfig.XMLSTATS_ACCESS_TOKEN);

                    }
                })
                .build();

        GameApi gameApi = adapter.create(GameApi.class);

        gameApi.getGameStats(
                gameIdNBA,
                new Callback<Game>() {
                    @Override
                    public void success(Game game, Response response) {
                        Log.d("mytag response:game", game.toString());
                        Log.d("mytag response:game", new Gson().toJson(game));
                        Log.d("mytag response:getUrl", response.getUrl());
                        Log.d("mytag response:getBody().toString", response.getBody().toString());
                        Log.d("mytag response:getBody().mimeType", response.getBody().mimeType());
                        Log.d("mytag response:getReason", response.getReason());
                        Log.d("mytag response:getHeaders().toString", response.getHeaders().toString());
                        Player bestAwayPlayer = game.away_stats.get(0);
                        for (Player player : game.away_stats){
                            Log.d("mytag away player:", player.toString());
                            if (player.points > bestAwayPlayer.points) bestAwayPlayer = player;
                        }
                        Player bestHomePlayer = game.home_stats.get(0);
                        for (Player player : game.home_stats){
                            Log.d("mytag home player:", player.toString());
                            if (player.points > bestAwayPlayer.points) bestHomePlayer = player;
                        }
                        Log.d("mytag best home player:", bestHomePlayer.toString());
                        Log.d("mytag best away player:", bestAwayPlayer.toString());

                        mMainFragment.fillViews(game);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("mytag error", error.toString());
                        Log.d("mytag error.getUrl", error.getUrl().toString());

                    }
                });
    }
}
