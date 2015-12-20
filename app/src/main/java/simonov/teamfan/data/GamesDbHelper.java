package simonov.teamfan.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import simonov.teamfan.data.GamesContract.GamesEntry;

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
                GamesEntry.COLUMN_DATA + " TEXT NOT NULL, " +
                GamesEntry.COLUMN_HOME + " TEXT NOT NULL, " +
                GamesEntry.COLUMN_AWAY + " TEXT NOT NULL, " +
                GamesEntry.COLUMN_HOME_SCORE + " TEXT NOT NULL, " +
                GamesEntry.COLUMN_AWAY_SCORE + " TEXT NOT NULL " +
                " );";
        db.execSQL(SQL_CREATE_GAMES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + GamesEntry.TABLE_NAME);
        onCreate(db);
    }
}
