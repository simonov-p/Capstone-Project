package simonov.teamfan.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.zip.GZIPInputStream;

import simonov.teamfan.BuildConfig;
import simonov.teamfan.R;
import simonov.teamfan.objects.Event;
import simonov.teamfan.objects.Events;

/**
 * Created by petr on 17-Dec-15.
 */
public class GamesSyncAdapter extends AbstractThreadedSyncAdapter {
    private static final String TAG = GamesSyncAdapter.class.getSimpleName();

    public static final String ACTION_DATA_UPDATED =
            "teamfan.simonov.ACTION_DATA_UPDATED";
    // Interval at which to sync with the weather, in seconds.
    // 60 seconds (1 minute) * 180 = 3 hours
    public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;
    private static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;

    public GamesSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        Log.d(TAG, "GamesSyncAdapter1");
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(TAG, "onPerformSync0");

        String teamName = "san-antonio-spurs";

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String teamJsonStr = null;

        GetJson getJson = new GetJson();
        getJson.init();

//        try {
            final String BASE_URL = "/sport/results/";
            Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                    .appendPath(teamName)
                    .build();
//            URL url = new URL(builtUri.toString()):

//        }
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
    public static class GetJson {

        // Replace with your access token
        static String ACCESS_TOKEN = BuildConfig.XMLSTATS_ACCESS_TOKEN;

        // Replace with your bot name and email/website to contact if there is a
        // problem e.g., "mybot/0.1 (https://erikberg.com/)"
        static final String USER_AGENT_NAME = "simonovP/0.1 (https://pk.simonov@gmail.com/)";

        static final String AUTHORIZATION = "Authorization";

        static final String BEARER = "Bearer " + ACCESS_TOKEN;

        static final String USER_AGENT = "User-agent";

        static final String ACCEPT_ENCODING = "Accept-encoding";

        static final String GZIP = "gzip";

        // For brevity, the url with api method, format, and parameters
        static final String REQUEST_URL = "https://erikberg.com/events.json?sport=nba&date=20151220";

        // Set the time zone to use for output
        static final String TIME_ZONE = "America/New_York";

        // All date-time strings for xmlstats use the ISO 8601 format
        static final String ISO_8601_FMT = "yyyy-MM-dd'T'HH:mm:mmZ";
        static final SimpleDateFormat XMLSTATS_DATE = new SimpleDateFormat(ISO_8601_FMT);

        public static void init() {
            Log.e(TAG,"init");

            InputStream in = null;
            try {
                URL url = new URL(REQUEST_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                // Set Authorization header
                connection.setRequestProperty(AUTHORIZATION, BEARER);
                // Set User agent header
                connection.setRequestProperty(USER_AGENT, USER_AGENT_NAME);
                // Tell server we can handle gzip content
                connection.setRequestProperty(ACCEPT_ENCODING, GZIP);

                Log.d(TAG,connection.toString());

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
                    System.exit(1);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        static String readHttpResponse(InputStream in, String encoding) {
            StringBuilder sb = new StringBuilder();
            // Verify the response is compressed, before attempting to decompress it
            try {
                if (GZIP.equals(encoding)) {
                    in = new GZIPInputStream(in);
                }
            } catch (IOException ex) {
                Log.e(TAG,"Error trying to read gzip data.");
                ex.printStackTrace();
                System.exit(1);
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
            } catch (IOException ex) {
                Log.e(TAG,"Error reading response.");
                ex.printStackTrace();
                System.exit(1);
            }
            return sb.toString();
        }

        static void printResult(String data) {
            try {
                // These two lines of code take the JSON string and return a POJO
                // in this case, an Events object (https://erikberg.com/api/methods/events)
                ObjectMapper mapper = new ObjectMapper();
                Events events = mapper.readValue(data, Events.class);

                Date date = XMLSTATS_DATE.parse(events.getEventsDate());
                SimpleDateFormat full = new SimpleDateFormat("EEEE, MMMM d, yyyy");
                Log.d(TAG, String.format("Events on %s%n%n", date));
                Log.d(TAG, String.format("%-35s %5s %34s%n", "Time", "Event", "Status"));

                // Set the time zone for output
                TimeZone tz = TimeZone.getTimeZone(TIME_ZONE);
                SimpleDateFormat sdf = new SimpleDateFormat("h:mm a z");
                sdf.setTimeZone(tz);
                // Loop through each Event (https://erikberg.com/api/objects/event)
                for (Event evt : events.getEventList()) {
                    date = XMLSTATS_DATE.parse(evt.getStartDateTime());
                    Log.d(TAG, String.format("%12s %24s vs. %-24s %9s%n",
                            sdf.format(date),
                            evt.getAwayTeam().getFullName(),
                            evt.getHomeTeam().getFullName(),
                            evt.getEventStatus()));
                }
            } catch (IOException | ParseException ex) {
                Log.e(TAG, "Could not parse JSON data: " + ex.getMessage());
            }
        }
    }
}