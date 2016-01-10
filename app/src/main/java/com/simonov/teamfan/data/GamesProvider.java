package com.simonov.teamfan.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by petr on 20-Dec-15.
 */
public class GamesProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
//    private static final String TAG = GamesProvider.class.getSimpleName();
    private static final String TAG = "mytag";

    private GamesDbHelper mOpenHelper;

    static final int GAMES = 100;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher =  new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(GamesContract.CONTENT_AUTHORITY, GamesContract.PATH_GAMES, GAMES);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new GamesDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor = mOpenHelper.getReadableDatabase().query(
                GamesContract.GamesEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        retCursor.setNotificationUri(getContext().getContentResolver(), uri); // that`s why data don`t fill at the first start of app
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        if (sUriMatcher.match(uri) == GAMES) {
            return GamesContract.GamesEntry.CONTENT_TYPE;
        } else {
            throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        long _id  = db.insert(GamesContract.GamesEntry.TABLE_NAME,null, values);
        Uri returnUri = GamesContract.GamesEntry.buildScheduleUri();
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        if (null == selection) selection = "1";
        int rowsDeleted = db.delete(GamesContract.GamesEntry.TABLE_NAME, selection, selectionArgs);
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rowsUpdated  = db.update(GamesContract.GamesEntry.TABLE_NAME, values, selection, selectionArgs);
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        Log.e(TAG, "Try insert: " + values.length);

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        db.beginTransaction();
        int returnCount = 0;
        try {
            for (ContentValues value : values){
                long _id  = db.insert(GamesContract.GamesEntry.TABLE_NAME,null, value);
                if (_id != -1) {
                    returnCount++;
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        getContext().getContentResolver().notifyChange(uri, null);
        Log.d(TAG, "    insert: " + returnCount + " uri:" + uri);
        Log.e(TAG, "    getContext().getContentResolver().getType: " + getContext().getContentResolver().getType(uri));
        return  returnCount;
    }
}
