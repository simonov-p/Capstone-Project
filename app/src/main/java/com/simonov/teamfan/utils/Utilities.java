package com.simonov.teamfan.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.simonov.teamfan.R;

/**
 * Created by petr on 03-Jan-16.
 */
public class Utilities {
    public static String getPreferredTeam(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_choose_team_key),
                context.getString(R.string.pref_choose_team_default));
    }
}