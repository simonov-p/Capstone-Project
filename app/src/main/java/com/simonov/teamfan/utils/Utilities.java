package com.simonov.teamfan.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.simonov.teamfan.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by petr on 03-Jan-16.
 */
public class Utilities {
    public static String getPreferredTeam(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Log.d("mytag", "getPreferredTeam:" + prefs.getString(context.getString(R.string.pref_choose_team_key),
                context.getString(R.string.pref_choose_team_default)));
        return prefs.getString(context.getString(R.string.pref_choose_team_key),
                context.getString(R.string.pref_choose_team_default));
    }

    public static String convertDate(String s) throws ParseException {
        // Set the time zone to use for output
        String TIME_ZONE = "America/New_York";

        // All date-time strings for xmlstats use the ISO 8601 format
        String ISO_8601_FMT = "yyyy-MM-dd'T'HH:mm:mmZ";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(ISO_8601_FMT);

        // Set the time zone for output
        TimeZone tz = TimeZone.getTimeZone(TIME_ZONE);
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a z");
        sdf.setTimeZone(tz);

        Date date = simpleDateFormat.parse(s);

        Log.d("mytag:string:", s + " format:" + sdf.format(date));
        Log.d("mytag:date:", String.valueOf(date.after(new Date())));

        return sdf.format(date);
    }
}
