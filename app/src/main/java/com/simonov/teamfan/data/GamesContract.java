package com.simonov.teamfan.data;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by petr on 20-Dec-15.
 */
public class GamesContract {
    public static final String CONTENT_AUTHORITY = "teamfan.simonov.com";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_GAMES = "games";

    public static class GamesEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_GAMES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_GAMES;

        public static final String TABLE_NAME = "games";

        public static final String COLUMN_GAME_ID = "_id";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_HOME = "home";
        public static final String COLUMN_AWAY = "away";
        public static final String COLUMN_HOME_SCORE = "home_score";
        public static final String COLUMN_AWAY_SCORE = "away_score";
        public static final String COLUMN_GAME_NBA_ID = "game_id";

        public static Uri buildGamesUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildGameUri(Long game) {
            return ContentUris.withAppendedId(CONTENT_URI, game);
        }
    }
}
