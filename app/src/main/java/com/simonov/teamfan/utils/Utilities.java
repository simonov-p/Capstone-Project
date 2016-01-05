package com.simonov.teamfan.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.simonov.teamfan.R;
import com.simonov.teamfan.objects.Player;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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

    public static int getTeamLogo(Context context, String team) {
        if (team.equals(context.getResources().getString(R.string.atlanta_hawks))) {
            return R.mipmap.atlanta_hawks;
        } else if (team.equals(context.getResources().getString(R.string.boston_celtics))) {
            return R.mipmap.boston_celtics;
        } else if (team.equals(context.getResources().getString(R.string.brooklyn_nets))) {
            return R.mipmap.brooklyn_nets;
        } else if (team.equals(context.getResources().getString(R.string.charlotte_hornets))) {
            return R.mipmap.charlotte_hornets;
        } else if (team.equals(context.getResources().getString(R.string.chicago_bulls))) {
            return R.mipmap.chicago_bulls;
        } else if (team.equals(context.getResources().getString(R.string.cleveland_cavaliers))) {
            return R.mipmap.clevland_cavaliers;
        } else if (team.equals(context.getResources().getString(R.string.dallas_mavericks))) {
            return R.mipmap.dallas_mavericks;
        } else if (team.equals(context.getResources().getString(R.string.denver_nuggets))) {
            return R.mipmap.denver_nuggets;
        } else if (team.equals(context.getResources().getString(R.string.detroit_pistons))) {
            return R.mipmap.detroit_pistons;
        } else if (team.equals(context.getResources().getString(R.string.golden_state_warriors))) {
            return R.mipmap.golden_state_warriors;
        } else if (team.equals(context.getResources().getString(R.string.houston_rockets))) {
            return R.mipmap.houston_rockets;
        } else if (team.equals(context.getResources().getString(R.string.indiana_pacers))) {
            return R.mipmap.indiana_pacers;
        } else if (team.equals(context.getResources().getString(R.string.los_angeles_clippers))) {
            return R.mipmap.los_angeles_clippers;
        } else if (team.equals(context.getResources().getString(R.string.los_angeles_lakers))) {
            return R.mipmap.los_angeles_lakers;
        } else if (team.equals(context.getResources().getString(R.string.memphis_grizzlies))) {
            return R.mipmap.memphis_grizzles;
        } else if (team.equals(context.getResources().getString(R.string.miami_heat))) {
            return R.mipmap.miami_heat;
        } else if (team.equals(context.getResources().getString(R.string.milwaukee_bucks))) {
            return R.mipmap.milwaukee_bucks;
        } else if (team.equals(context.getResources().getString(R.string.minnesota_timberwolves))) {
            return R.mipmap.minnesota_timberwolves;
        } else if (team.equals(context.getResources().getString(R.string.new_orleans_pelicans))) {
            return R.mipmap.new_orleans_pelicans;
        } else if (team.equals(context.getResources().getString(R.string.new_york_knicks))) {
            return R.mipmap.new_york_knicks;
        } else if (team.equals(context.getResources().getString(R.string.oklahoma_city_thunder))) {
            return R.mipmap.oklahome_city_thunder;
        } else if (team.equals(context.getResources().getString(R.string.orlando_magic))) {
            return R.mipmap.orlando_magic;
        } else if (team.equals(context.getResources().getString(R.string.philadelphia_76ers))) {
            return R.mipmap.philadelphia_76ers;
        } else if (team.equals(context.getResources().getString(R.string.phoenix_suns))) {
            return R.mipmap.phoenix_suns;
        } else if (team.equals(context.getResources().getString(R.string.portland_trail_blazers))) {
            return R.mipmap.portland_trail_blazers;
        } else if (team.equals(context.getResources().getString(R.string.sacramento_kings))) {
            return R.mipmap.sacramento_kings;
        } else if (team.equals(context.getResources().getString(R.string.san_antonio_spurs))) {
            return R.mipmap.san_antonio_spurs;
        } else if (team.equals(context.getResources().getString(R.string.toronto_raptors))) {
            return R.mipmap.toronto_raptors;
        } else if (team.equals(context.getResources().getString(R.string.utah_jazz))) {
            return R.mipmap.utah_jazz;
        } else if (team.equals(context.getResources().getString(R.string.washington_wizards))) {
            return R.mipmap.washington_capitals;
        } else {
            return R.mipmap.ic_launcher;
        }
    }

    public static Player getBestPlayer (List<Player> players){
        Player bestPlayer = players.get(0);
        for (Player player : players){
            if (player.points > bestPlayer.points) bestPlayer = player;
        }
        return bestPlayer;
    }
}
