package com.simonov.teamfan.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.simonov.teamfan.data.GamesContract.GamesEntry;

/**
 * Created by petr on 20-Dec-15.
 */
public class GamesDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "games.db";

    public GamesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_GAMES_TABLE = "CREATE TABLE " + GamesEntry.TABLE_NAME + " (" +
                GamesEntry.COLUMN_GAME_ID + " INTEGER PRIMARY KEY," +
                GamesEntry.COLUMN_GAME_NBA_ID + " TEXT NOT NULL," +
                GamesEntry.COLUMN_DATE + " TEXT NOT NULL, " +
                GamesEntry.COLUMN_TEAM_NAME + " TEXT NOT NULL, " +
                GamesEntry.COLUMN_OPPONENT_NAME + " TEXT NOT NULL, " +
                GamesEntry.COLUMN_TEAM_SCORE + " TEXT NOT NULL, " +
                GamesEntry.COLUMN_OPPONENT_SCORE + " TEXT NOT NULL, " +
                GamesEntry.COLUMN_TEAM_EVENTS_WON + " TEXT NOT NULL, " +
                GamesEntry.COLUMN_TEAM_EVENTS_LOST + " TEXT NOT NULL, " +
                GamesEntry.COLUMN_OPPONENT_EVENTS_WON + " TEXT NOT NULL, " +
                GamesEntry.COLUMN_OPPONENT_EVENTS_LOST + " TEXT NOT NULL, " +
                GamesEntry.COLUMN_EVENT_LOCATION_TYPE + " TEXT NOT NULL " +
                " );";
        db.execSQL(SQL_CREATE_GAMES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + GamesEntry.TABLE_NAME);
        onCreate(db);
    }
}
