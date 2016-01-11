package com.simonov.teamfan.sync;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by petr on 17-Dec-15.
 */
public class GamesAuthenticatorService extends Service {
    private GamesAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        mAuthenticator = new GamesAuthenticator(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
