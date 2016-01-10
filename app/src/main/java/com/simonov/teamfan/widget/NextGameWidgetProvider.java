package com.simonov.teamfan.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.simonov.teamfan.R;
import com.simonov.teamfan.sync.GamesSyncAdapter;

public class NextGameWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        context.startService(new Intent(context, NextGameWidgetIntentService.class));
        Log.d("mytag", "onUpdate widget ");

    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
                                          int appWidgetId, Bundle newOptions) {
        context.startService(new Intent(context, NextGameWidgetIntentService.class));
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d("mytag", "onReceive widget ");
        if (GamesSyncAdapter.ACTION_DATA_UPDATED.equals(intent.getAction())) {
            Log.d("mytag", "onReceive widget appWidgetManager");

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                    new ComponentName(context, getClass()));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_container);
        }
        if (GamesSyncAdapter.ACTION_DATA_UPDATED.equals(intent.getAction())) {
            Log.d("mytag", "onReceive widget startService");

            context.startService(new Intent(context, NextGameWidgetIntentService.class));
        }
    }
}