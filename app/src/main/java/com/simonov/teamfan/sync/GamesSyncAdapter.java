package com.simonov.teamfan.sync;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.IntDef;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simonov.teamfan.BuildConfig;
import com.simonov.teamfan.R;
import com.simonov.teamfan.activities.DetailActivity;
import com.simonov.teamfan.activities.MainActivity;
import com.simonov.teamfan.data.GamesContract;
import com.simonov.teamfan.objects.Event;
import com.simonov.teamfan.utils.Utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.zip.GZIPInputStream;

/**
 * Created by petr on 17-Dec-15.
 */
public class GamesSyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String TAG = GamesSyncAdapter.class.getSimpleName();

    public static final String ACTION_DATA_UPDATED =
            "com.simonov.teamfan.ACTION_DATA_UPDATED";

    // Interval at which to sync with the weather, in seconds.
    // 60 seconds (1 minute) * 180 = 3 hours
    public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;
    private static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
    private static final int LAST_GAMES_NOTIFICATION_ID = 1987;

    private String mTeamQuery;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({GAMES_STATUS_OK, GAMES_STATUS_SERVER_DOWN, GAMES_STATUS_SERVER_INVALID, GAMES_STATUS_UNKNOWN, GAMES_STATUS_INVALID})
    public @interface LocationStatus {}
    public static final int GAMES_STATUS_OK = 0;
    public static final int GAMES_STATUS_SERVER_DOWN = 1;
    public static final int GAMES_STATUS_SERVER_INVALID = 2;
    public static final int GAMES_STATUS_UNKNOWN = 3;
    public static final int GAMES_STATUS_INVALID = 4;

    public GamesSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        Log.d(TAG, "GamesSyncAdapter1");
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(TAG, "onPerformSync0");
        String teamName = Utilities.getPreferredTeam(getContext());
        mTeamQuery = teamName;
        GetJson getJson = new GetJson();

        getJson.init();

        Log.d(TAG, "onPerformSync1");

    }
    public static void initializeSyncAdapter(Context context) {
        Log.d(TAG, "initializeSyncAdapter0");

        getSyncAccount(context);
        Log.d(TAG, "initializeSyncAdapter1");

    }
    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        Log.d(TAG, "getSyncAccount0");
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if ( null == accountManager.getPassword(newAccount) ) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
            Log.d(TAG, "getSyncAccount,onAccountCreated");

            onAccountCreated(newAccount, context);
        }
        Log.d(TAG, "getSyncAccount1,new Account");
        Log.d(TAG, accountManager.getPassword(newAccount));

        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        Log.d(TAG, "onAccountCreated0");

        GamesSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        Log.d(TAG, "onAccountCreated1");

        syncImmediately(context);
    }
    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Log.d(TAG, "configurePeriodicSync0");

        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
        Log.d(TAG, "configurePeriodicSync1");

    }
    /**
     * Helper method to have the sync adapter sync immediately
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Log.d(TAG, "syncImmediately0");

        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
        Log.d(TAG, "syncImmediately1");

    }

    public class GetJson {
        String ACCESS_TOKEN = BuildConfig.XMLSTATS_ACCESS_TOKEN;
        final String USER_AGENT_NAME = "simonovP/0.1 (https://pk.simonov@gmail.com/)";
        final String AUTHORIZATION = "Authorization";
        final String BEARER = "Bearer " + ACCESS_TOKEN;
        final String USER_AGENT = "User-agent";
        final String ACCEPT_ENCODING = "Accept-encoding";
        final String GZIP = "gzip";

//        static final String REQUEST_URL = "https://erikberg.com/events.json?sport=nba&date=20151220";
        final String REQUEST_URL = "https://erikberg.com/nba/results/";
//        final String REQUEST_URL = "https://erikberg.com/nba/boxscore/20160102-milwaukee-bucks-at-minnesota-timberwolves.json";
//        final String REQUEST_URL = "https://erikberg.com/nba/boxscore/20160110-new-orleans-pelicans-at-los-angeles-clippers.json";

        public void init() {
            Log.e(TAG,"init");
            InputStream in = null;
            try {
                URL url = new URL(REQUEST_URL + mTeamQuery + ".json");
//                URL url = new URL(REQUEST_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                // Set Authorization header, User agent header. Tell server we can handle gzip content
                connection.setRequestProperty(AUTHORIZATION, BEARER);
                connection.setRequestProperty(USER_AGENT, USER_AGENT_NAME);
                connection.setRequestProperty(ACCEPT_ENCODING, GZIP);

                // Check the HTTP status code for "200 OK"
                int statusCode = connection.getResponseCode();
                String encoding = connection.getContentEncoding();
                if (statusCode == HttpURLConnection.HTTP_OK) {
                    in = connection.getInputStream();
                    if (in != null) {
                        // read in http response
                        String response = readHttpResponse(in, encoding);
                        if (response != null) {
                            // Have the data we want, now call function to parse it
                            printResult(response);
                        }
                    }
                } else {
                    // handle HTTP error
                    Log.e(TAG,"Server returned HTTP status: " + statusCode + " " + connection.getResponseMessage());
                    in = connection.getErrorStream();
                    if (in != null) {
                        String response = readHttpResponse(in, encoding);
                        Log.e("Response:",response);
                    }
                    setGamesStatus(getContext(), GAMES_STATUS_SERVER_DOWN);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                setGamesStatus(getContext(), GAMES_STATUS_SERVER_DOWN);
            }
        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        private String readHttpResponse(InputStream in, String encoding) {
            StringBuilder sb = new StringBuilder();
            // Verify the response is compressed, before attempting to decompress it
            try {
                if (GZIP.equals(encoding)) {
                    in = new GZIPInputStream(in);
                }
            } catch (IOException ex) {
                Log.e(TAG, "Error trying to read gzip data.");
                ex.printStackTrace();
                setGamesStatus(getContext(), GAMES_STATUS_SERVER_DOWN);
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
            } catch (IOException ex) {
                Log.e(TAG, "Error reading response.");
                ex.printStackTrace();
                setGamesStatus(getContext(), GAMES_STATUS_SERVER_DOWN);
            }
            return sb.toString();
        }

        void printResult(String data) {
            try {
                Log.d("mytag data", data);
                // These two lines of code take the JSON string and return a POJO
                // in this case, an Events object (https://erikberg.com/api/methods/events)
                ObjectMapper mapper = new ObjectMapper();
                Event[] events = mapper.readValue(data, Event[].class);

                // Loop through each Event (https://erikberg.com/api/objects/event)
                Vector<ContentValues> cVVector = new Vector<ContentValues>(events.length);

                for (Event evt : events) {
                    cVVector.add(evt.eventToCV());
                }
                if (cVVector.size() > 0) {
                    ContentValues[] cvArray = new ContentValues[cVVector.size()];
                    cVVector.toArray(cvArray);

                    // remove old db rows
                    String whereClause = GamesContract.GamesEntry.COLUMN_TEAM_NAME + " = " + "'" +  Utilities.getFullNameFromQuery(mTeamQuery) + "'";
                    getContext().getContentResolver().delete(GamesContract.GamesEntry.CONTENT_URI, whereClause, null);

                    getContext().getContentResolver().bulkInsert(GamesContract.GamesEntry.CONTENT_URI,
                            cvArray);
                    Log.e(TAG, "Parse JSON elements: " + cvArray.length);
                }
                updateWidgets();
                notifyLastGame();
                setGamesStatus(getContext(), GAMES_STATUS_OK);
            } catch (IOException ex) {
                Log.e(TAG, "Could not parse JSON data: " + ex.getMessage());
                setGamesStatus(getContext(), GAMES_STATUS_SERVER_INVALID);
            }
        }
    }

    private void setGamesStatus(Context c, @LocationStatus int gamesStatus){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor spe = sp.edit();
        spe.putInt(c.getString(R.string.pref_games_status_key), gamesStatus);
        spe.apply();
    }
    private void updateWidgets() {
        Context context = getContext();
        Log.e(TAG, "update widgets");
        // Setting the package ensures that only components in our app will receive the broadcast
        Intent dataUpdatedIntent = new Intent(ACTION_DATA_UPDATED)
                .setPackage(context.getPackageName());
        context.sendBroadcast(dataUpdatedIntent);
    }

    private void notifyLastGame() {
        Context context = getContext();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String displayNotificationsKey = context.getString(R.string.pref_enable_notifications_key);

        boolean displayNotifications = prefs.getBoolean(displayNotificationsKey,
                Boolean.parseBoolean(context.getString(R.string.pref_enable_notifications_default)));

        if ( displayNotifications ) {

            String lastNotificationKey = context.getString(R.string.pref_last_notification);
            long lastSync = prefs.getLong(lastNotificationKey, 0);

            String team = Utilities.getPreferredTeam(context);

            String sortOrder = GamesContract.GamesEntry.COLUMN_DATE + " ASC";
            String selectionAllGames = GamesContract.GamesEntry.COLUMN_TEAM_NAME + " = " + "'" +
                    Utilities.getFullNameFromQuery(Utilities.getPreferredTeam(getContext())) + "'";
            String selectionFinished = selectionAllGames + " and " + GamesContract.GamesEntry.COLUMN_TEAM_SCORE + " > -1";

            Cursor cursorFinishedGames = getContext().getContentResolver().query(GamesContract.GamesEntry.CONTENT_URI,
                    null,
                    selectionFinished,
                    null,
                    sortOrder
            );

            if (null != cursorFinishedGames && cursorFinishedGames.getCount() > lastSync) {
                if (cursorFinishedGames.moveToLast()) {
                    String homeTeamName;
                    String awayTeamName;
                    String homeTeamScore;
                    String awayTeamScore;
                    if (cursorFinishedGames.getString(
                            cursorFinishedGames.getColumnIndex(
                                    GamesContract.GamesEntry.COLUMN_EVENT_LOCATION_TYPE)).equals("h")) {
                        homeTeamName = cursorFinishedGames.getString(
                                cursorFinishedGames.getColumnIndex(GamesContract.GamesEntry.COLUMN_TEAM_NAME));
                        awayTeamName = cursorFinishedGames.getString(
                                cursorFinishedGames.getColumnIndex(GamesContract.GamesEntry.COLUMN_OPPONENT_NAME));
                        homeTeamScore = cursorFinishedGames.getString(
                                cursorFinishedGames.getColumnIndex(GamesContract.GamesEntry.COLUMN_TEAM_SCORE));
                        awayTeamScore = cursorFinishedGames.getString(
                                cursorFinishedGames.getColumnIndex(GamesContract.GamesEntry.COLUMN_OPPONENT_SCORE));

                    } else {
                        homeTeamName = cursorFinishedGames.getString(
                                cursorFinishedGames.getColumnIndex(GamesContract.GamesEntry.COLUMN_OPPONENT_NAME));
                        awayTeamName = cursorFinishedGames.getString(
                                cursorFinishedGames.getColumnIndex(GamesContract.GamesEntry.COLUMN_TEAM_NAME));
                        homeTeamScore = cursorFinishedGames.getString(
                                cursorFinishedGames.getColumnIndex(GamesContract.GamesEntry.COLUMN_OPPONENT_SCORE));
                        awayTeamScore = cursorFinishedGames.getString(
                                cursorFinishedGames.getColumnIndex(GamesContract.GamesEntry.COLUMN_TEAM_SCORE));
                    }

                    String title = context.getString(R.string.app_name);

                    String contentText = String.format(context.getString(R.string.format_notification),
                            awayTeamName,awayTeamScore,homeTeamScore,homeTeamName);

                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(getContext())
                                    .setColor(getContext().getResources().getColor(R.color.colorNotification))
                                    .setSmallIcon(Utilities.getTeamLogo(getContext(),
                                            cursorFinishedGames.getString(
                                                    cursorFinishedGames.getColumnIndex(GamesContract.GamesEntry.COLUMN_TEAM_NAME))))
                                    .setContentTitle(title)
                                    .setContentText(contentText);

                    // Make something interesting happen when the user clicks on the notification.
                    // In this case, opening the app is sufficient.
                    Intent resultIntent = new Intent(getContext(), DetailActivity.class)
                            .putExtra(MainActivity.SEND_GAME_ID, new Event(cursorFinishedGames));

                    // The stack builder object will contain an artificial back stack for the
                    // started Activity.
                    // This ensures that navigating backward from the Activity leads out of
                    // your application to the Home screen.
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                    stackBuilder.addNextIntent(resultIntent);
                    PendingIntent resultPendingIntent =
                            stackBuilder.getPendingIntent(
                                    0,
                                    PendingIntent.FLAG_UPDATE_CURRENT
                            );
                    mBuilder.setContentIntent(resultPendingIntent);

                    NotificationManager mNotificationManager =
                            (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                    // WEATHER_NOTIFICATION_ID allows you to update the notification later on.
                    mNotificationManager.notify(LAST_GAMES_NOTIFICATION_ID, mBuilder.build());

                    //refreshing last sync
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putLong(lastNotificationKey, System.currentTimeMillis());
                    editor.apply();
                }
                cursorFinishedGames.close();
            }
        }
    }
}