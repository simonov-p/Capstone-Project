package com.simonov.teamfan.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.simonov.teamfan.R;
import com.simonov.teamfan.fragments.ScheduleAdapter;
import com.simonov.teamfan.fragments.ScheduleFragment;
import com.simonov.teamfan.objects.Event;
import com.simonov.teamfan.sync.GamesSyncAdapter;
import com.simonov.teamfan.utils.Utilities;

public class MainActivity extends AppCompatActivity
implements ScheduleFragment.DetailFragmentCallback {

    public static String SEND_GAME_ID = "send_game_id_from_main_activity";
    private String mTeam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        GamesSyncAdapter.initializeSyncAdapter(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_change_team) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onResume() {
        super.onResume();
        String team = Utilities.getPreferredTeam(this);
        if (team != null && !team.equals(mTeam)) {
            ScheduleFragment scheduleFragment = (ScheduleFragment)getSupportFragmentManager().findFragmentById(R.id.fragment);
            if ( null != scheduleFragment ) {
                scheduleFragment.onTeamChanged();
            }
            mTeam = team;
        }
    }

    @Override
    public void onGameSelected(Event gameEvent, ScheduleAdapter.ViewHolder vh) {
        Intent intent = new Intent(this, DetailActivity.class)
                .putExtra(SEND_GAME_ID, gameEvent);

        ActivityCompat.startActivity(this, intent, null);
    }
}
