package com.simonov.teamfan.sync;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by petr on 17-Dec-15.
 */
public class GamesSyncService extends Service {
    private static final String TAG = GamesSyncService.class.getSimpleName();
    private static final Object sSyncAdapterLock = new Object();
    private static GamesSyncAdapter sGamesSyncAdapter = null;

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        synchronized (sSyncAdapterLock) {
            if (sGamesSyncAdapter == null) {
                sGamesSyncAdapter = new GamesSyncAdapter(getApplicationContext(),true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sGamesSyncAdapter.getSyncAdapterBinder();
    }
}
