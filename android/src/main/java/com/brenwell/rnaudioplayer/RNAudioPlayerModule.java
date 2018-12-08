
package com.brenwell.rnaudioplayer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReadableMap;
import com.google.android.exoplayer2.util.Util;

import com.brenwell.rnaudioplayer.RNAudioPlayerService.LocalBinder;

import java.util.Map;

public class RNAudioPlayerModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

    public static final String TAG = "RNAudioPlayerModule";

    boolean mBounded;
    RNAudioPlayerService mServer;

    public RNAudioPlayerModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
    return "RNAudioPlayer";
  }

    @Override
    public Map<String, Object> getConstants() {
        return RNAudioPlayerConstants.getConstants();
    }

    /**
     * Starts and Binds a service
     * @param options
     * @param promise
     */
    @ReactMethod
    public void setup(final ReadableMap options, final Promise promise) {

        Log.d(TAG, "setup: " + options);

        // Starts and Binds a service
        try {
            Intent intent = new Intent(reactContext, RNAudioPlayerService.class);
            reactContext.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
            Util.startForegroundService(reactContext, intent);
            promise.resolve(true);
        } catch (Exception ex) {
            promise.reject("ERR_UNEXPECTED_EXCEPTION", ex);
        }
    }

    /**
     * Adds tracks to the service
     * @param options
     * @param promise
     */
    @ReactMethod
    public void add(final ReadableMap options, final Promise promise) {
        try{
            RNAudioPlayerTrack track  = new RNAudioPlayerTrack(options);
            Log.d(TAG, "add: " + track);
            mServer.add(track);
            promise.resolve(true);
        } catch (Exception ex) {
            promise.reject("ERR_UNEXPECTED_EXCEPTION", ex);
        }
    }

    /**
     *
     */
    ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected: ");
            mBounded = false;
            mServer = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected: ");
            mBounded = true;
            LocalBinder mLocalBinder = (LocalBinder)service;
            mServer = mLocalBinder.getServerInstance();
        }
    };
}