package com.simonov.teamfan.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by petr on 20-Dec-15.
 */
public class GamesContract {
    public static final String CONTENT_AUTHORITY = "com.simonov.teamfan";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_GAMES = "games";

    public static class GamesEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI;

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_GAMES;

        public static final String TABLE_NAME = "games";

        public static final String COLUMN_GAME_ID = "_id";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_TEAM_NAME = "team";
        public static final String COLUMN_OPPONENT_NAME = "opponent";
        public static final String COLUMN_TEAM_SCORE = "team_score";
        public static final String COLUMN_OPPONENT_SCORE = "opponent_score";
        public static final String COLUMN_TEAM_EVENTS_WON = "team_events_won";
        public static final String COLUMN_TEAM_EVENTS_LOST = "team_events_lost";
        public static final String COLUMN_OPPONENT_EVENTS_WON = "opponent_events_won";
        public static final String COLUMN_OPPONENT_EVENTS_LOST = "opponent_events_lost";
        public static final String COLUMN_EVENT_LOCATION_TYPE = "event_location_type";
        public static final String COLUMN_EVENT_LOCATION_NAME = "event_location_name";
        public static final String COLUMN_GAME_NBA_ID = "game_id";

        public static final String NO_SCORE = "-1";

        public static Uri buildScheduleUri() {
            return BASE_CONTENT_URI;
        }
    }
}
