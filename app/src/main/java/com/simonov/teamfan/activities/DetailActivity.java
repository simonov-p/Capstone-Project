package com.simonov.teamfan.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.simonov.teamfan.BuildConfig;
import com.simonov.teamfan.R;
import com.simonov.teamfan.api.GameApi;
import com.simonov.teamfan.fragments.GameInfoLeadersFragment;
import com.simonov.teamfan.fragments.GameInfoMainFragment;
import com.simonov.teamfan.fragments.GameInfoPreviousFragment;
import com.simonov.teamfan.objects.Event;
import com.simonov.teamfan.objects.Game;
import com.simonov.teamfan.utils.Utilities;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DetailActivity extends AppCompatActivity {
    private static final String TAG = DetailActivity.class.getSimpleName();

    private Event mGameEvent;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private Fragment mSecondaryFragment;
    private GameInfoMainFragment mMainFragment;
    private boolean mGameFinished;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mGameEvent = getIntent().getParcelableExtra(MainActivity.SEND_GAME_ID);
        mGameFinished = Utilities.compareDate(mGameEvent.getEventStartDateTime());
        if (mGameFinished) {
            mMainFragment = new GameInfoMainFragment();
            mSecondaryFragment = new GameInfoLeadersFragment();
            getGameInfo(mGameEvent.getEventId());
        } else {
            mMainFragment = new GameInfoMainFragment();
            mSecondaryFragment = new GameInfoPreviousFragment(mGameEvent);
        }

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
                return mSecondaryFragment;
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
                    return getString(R.string.label_main_info_fragment);
                case 1:
                    return mGameFinished ? getString(R.string.label_leaders_fragment) : getString(R.string.label_previous_games_fragment) ;
            }
            return null;
        }
    }

    private void getGameInfo(String gameIdNBA){
        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(getString(R.string.base_url))
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
                        mMainFragment.fillViews(mGameEvent, game);
                        ((GameInfoLeadersFragment) mSecondaryFragment).fillViews(game);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("mytag error", error.toString());
                        Log.d("mytag error.getUrl", error.getUrl().toString());
                    }
                });
    }
}
