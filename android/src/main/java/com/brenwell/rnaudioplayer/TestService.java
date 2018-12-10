package com.brenwell.rnaudioplayer;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class TestService  extends Service {

    static final String TAG = "TestService";

    int mStartMode;       // indicates how to behave if the service is killed
    IBinder mBinder;      // interface for clients that bind
    boolean mAllowRebind; // indicates whether onRebind should be used

    @Override
    public void onCreate() {
        // The service is being created
        super.onCreate();

        Log.d(TAG, "onCreate() called");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent,flags,startId);
        // The service is starting, due to a call to startService()

        Log.d(TAG, "onStartCommand() called with: intent = [" + intent + "], flags = [" + flags + "], startId = [" + startId + "]");
        return mStartMode;
    }
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind() called with: intent = [" + intent + "]");
        // A client is binding to the service with bindService()

        if (mBinder == null){
            mBinder = new Binder();
        }

        return mBinder;
    }
    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind() called with: intent = [" + intent + "]");
        // All clients have unbound with unbindService()

        stopSelf();
        return mAllowRebind;
    }
    @Override
    public void onRebind(Intent intent) {
        Log.d(TAG, "onRebind() called with: intent = [" + intent + "]");
        // A client is binding to the service with bindService(),
        // after onUnbind() has already been called
    }
    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy() called");
        super.onDestroy();
        // The service is no longer used and is being destroyed
    }

    public class Binder extends android.os.Binder {
        TestService getService(){
            // Simply return a reference to this instance of the Service.
            return TestService.this;
        }
    }
}