package com.simonov.teamfan.widget;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.RemoteViews;

import com.simonov.teamfan.activities.MainActivity;
import com.simonov.teamfan.R;
import com.simonov.teamfan.data.GamesContract;
import com.simonov.teamfan.objects.Event;
import com.simonov.teamfan.utils.Utilities;

/**
 * Created by petr on 10-Jan-16.
 */
public class NextGameWidgetIntentService extends IntentService {

    public NextGameWidgetIntentService() {
        super("NextGameWidgetIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                NextGameWidgetProvider.class));

        String sortOrder = GamesContract.GamesEntry.COLUMN_DATE + " ASC";
        String selectionAllGames = GamesContract.GamesEntry.COLUMN_TEAM_NAME + " = " + "'" +
                Utilities.getFullNameFromQuery(Utilities.getPreferredTeam(getApplicationContext())) + "'";

        String selectionUnfinished = selectionAllGames + " and " + GamesContract.GamesEntry.COLUMN_TEAM_SCORE + " = -1";

        Cursor data = getContentResolver().query(GamesContract.GamesEntry.CONTENT_URI, null, selectionUnfinished,
                null, sortOrder);

        if (data == null) {
            return;
        }
        if (!data.moveToFirst()) {
            data.close();
            return;
        }

        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_last_game);
            int widgetWidth = getWidgetWidth(appWidgetManager, appWidgetId);
            int smallWidth = getResources().getDimensionPixelSize(R.dimen.widget_last_game_small_width);
            int middleWidth = getResources().getDimensionPixelSize(R.dimen.widget_last_game_middle_width);

            data.moveToFirst();

            if (widgetWidth <= smallWidth) {
                views.setViewVisibility(R.id.list_item_score_home_team, View.GONE);
                views.setViewVisibility(R.id.list_item_score_away_team, View.GONE);
                views.setViewVisibility(R.id.list_item_date, View.GONE);

            } else if (widgetWidth <= middleWidth) {
                views.setViewVisibility(R.id.list_item_score_home_team, View.GONE);
                views.setViewVisibility(R.id.list_item_score_away_team, View.GONE);
                views.setViewVisibility(R.id.list_item_date, View.VISIBLE);
            } else {
                views.setViewVisibility(R.id.list_item_score_home_team, View.VISIBLE);
                views.setViewVisibility(R.id.list_item_score_away_team, View.VISIBLE);
                views.setViewVisibility(R.id.list_item_date, View.VISIBLE);
            }

            Event event = new Event(data);

            if (Utilities.isHomeGame(event)) {
                views.setImageViewResource(R.id.list_item_logo_away_team, Utilities.getTeamLogo(getApplicationContext(),
                        data.getString(data.getColumnIndex(GamesContract.GamesEntry.COLUMN_TEAM_NAME))));
                views.setTextViewText(R.id.list_item_score_away_team,
                        String.format(getString(R.string.win_loss_divider),
                                data.getString(data.getColumnIndex(GamesContract.GamesEntry.COLUMN_TEAM_EVENTS_WON)),
                                data.getString(data.getColumnIndex(GamesContract.GamesEntry.COLUMN_TEAM_EVENTS_LOST))
                        ));

                views.setImageViewResource(R.id.list_item_logo_home_team, Utilities.getTeamLogo(getApplicationContext(),
                        data.getString(data.getColumnIndex(GamesContract.GamesEntry.COLUMN_OPPONENT_NAME))));
                views.setTextViewText(R.id.list_item_score_home_team,
                        String.format(getString(R.string.win_loss_divider),
                                data.getString(data.getColumnIndex(GamesContract.GamesEntry.COLUMN_OPPONENT_EVENTS_WON)),
                                data.getString(data.getColumnIndex(GamesContract.GamesEntry.COLUMN_OPPONENT_EVENTS_LOST))
                        ));

            } else {
                views.setImageViewResource(R.id.list_item_logo_away_team, Utilities.getTeamLogo(getApplicationContext(),
                        data.getString(data.getColumnIndex(GamesContract.GamesEntry.COLUMN_OPPONENT_NAME))));
                views.setTextViewText(R.id.list_item_score_away_team,
                        String.format(getString(R.string.win_loss_divider),
                                data.getString(data.getColumnIndex(GamesContract.GamesEntry.COLUMN_OPPONENT_EVENTS_WON)),
                                data.getString(data.getColumnIndex(GamesContract.GamesEntry.COLUMN_OPPONENT_EVENTS_LOST))
                        ));

                views.setImageViewResource(R.id.list_item_logo_home_team, Utilities.getTeamLogo(getApplicationContext(),
                        data.getString(data.getColumnIndex(GamesContract.GamesEntry.COLUMN_TEAM_NAME))));
                views.setTextViewText(R.id.list_item_score_home_team,
                        String.format(getString(R.string.win_loss_divider),
                                data.getString(data.getColumnIndex(GamesContract.GamesEntry.COLUMN_TEAM_EVENTS_WON)),
                                data.getString(data.getColumnIndex(GamesContract.GamesEntry.COLUMN_TEAM_EVENTS_LOST))
                        ));
            }

            views.setTextViewText(R.id.list_item_date, Utilities.getDaysBefore(
                    data.getString(data.getColumnIndex(GamesContract.GamesEntry.COLUMN_DATE))));

            data.close();

            // Create an Intent to launch MainActivity
            Intent launchIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, launchIntent, 0);
            views.setOnClickPendingIntent(R.id.widget_container, pendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    private int getWidgetWidth(AppWidgetManager appWidgetManager, int appWidgetId) {
        // prior to Jelly Bean, widgets were always their default size
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            return getResources().getDimensionPixelSize(R.dimen.widget_detail_default_width);
        }
        return getWidgetWidthFromOptions(appWidgetManager, appWidgetId);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private int getWidgetWidthFromOptions(AppWidgetManager appWidgetManager, int appWidgetId) {
        Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
        if (options.containsKey(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)) {
            int minWidtDp = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
            // the width returned is in dp, but we`ll convert it to pixels to match the other widths
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, minWidtDp,
                    displayMetrics);
        }
        return 0;
    }
}
