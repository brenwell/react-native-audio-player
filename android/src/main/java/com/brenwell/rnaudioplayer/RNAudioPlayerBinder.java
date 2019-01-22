package com.brenwell.rnaudioplayer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.google.android.exoplayer2.util.Util;

public class RNAudioPlayerBinder {

    private static final String NAME = "Binder";
    private boolean isBound;
    private RNAudioPlayerService audioService;
    private ReactContext rnContext;
    private Handler mainHandler;

    /**
     * Starts the service
     * @param reactContext
     */
    public void startService(ReactApplicationContext reactContext) {
        rnContext = reactContext;
        mainHandler = new Handler(rnContext.getMainLooper());
        Intent intent = new Intent(rnContext, RNAudioPlayerService.class);

        Util.startForegroundService(reactContext, intent);
        boolean success = reactContext.bindService(intent, sConnection, Context.BIND_AUTO_CREATE);

        Logger.d(NAME, "startService: " + success);
    }

    /**
     *  Stops the service
     */
    protected void stopService(){
        if (isBound){
            rnContext.unbindService(sConnection);
            isBound = false;
        }
    }

    /**
     *  Add a track to the player
     * @param track
     */
    public void addTrack(RNAudioPlayerTrack track) {
        Logger.d(NAME, "addTrack() called with: track = [" + track + "]");

        mainHandler.post(() -> {
            audioService.add(track);
        });
    }


    /**
     *  Hides the notification
     */
    public void hideNotification() {
        Logger.d(NAME, "hideNotification");

        mainHandler.post(() -> {
            audioService.hideNotification();
        });
    }

    /**
     *  Service listener
     */
    ServiceConnection sConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            Logger.d(NAME, "onServiceConnected ");

            RNAudioPlayerService.Binder binder = (RNAudioPlayerService.Binder) service;

            audioService = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

            Logger.d(NAME, "onServiceDisconnected ");

            audioService = null;
            isBound = false;
        }
    };
}


