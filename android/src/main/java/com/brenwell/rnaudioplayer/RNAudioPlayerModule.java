
package com.brenwell.rnaudioplayer;

import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReadableMap;


import java.util.Map;

public class RNAudioPlayerModule extends ReactContextBaseJavaModule {

    private static final String NAME = "Module";
    private final ReactApplicationContext reactContext;
    private final RNAudioPlayerBinder binder = new RNAudioPlayerBinder();


    /**
     *  Recieves the react context
     * @param reactContext
     */
    public RNAudioPlayerModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    /**
     * Exports the class name to JS
     * @return
     */
    @Override
    public String getName() {
    return "RNAudioPlayer";
  }



    @Override
    public void initialize() {
        ReactContext context = getReactApplicationContext();

        Logger.d(NAME, "initialize() called");
    }

    @Override
    public void onCatalystInstanceDestroy() {
//        ReactContext context = getReactApplicationContext();

        Logger.d(NAME, "onCatalystInstanceDestroy() called");
    }

    /**
     * Exports all the constants to JS
     * @return
     */
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

        Logger.d(NAME, "setup: " + options);

        try {
            binder.startService(reactContext);
            promise.resolve(true);
        }

        catch (Exception ex) {
            promise.reject("ERR_UNEXPECTED_EXCEPTION", ex);
        }
    }

    /**
     * Destroy Service
     * @param promise
     */
    @ReactMethod
    public void destroy(final Promise promise)
    {
        Logger.d(NAME, "destroy: ");

        try{
            binder.stopService();
            promise.resolve(true);
        }

        catch (Exception ex) {
            promise.reject("ERR_UNEXPECTED_EXCEPTION", ex);
        }
    }

    /**
     * Adds tracks to the service
     * @param options
     * @param promise
     */
    @ReactMethod
    public void add(final ReadableMap options, final Promise promise)
    {
        Logger.d(NAME, "add: " + options);

        try{
            RNAudioPlayerTrack track  = new RNAudioPlayerTrack(options);
            binder.addTrack(track);
            promise.resolve(true);
        }

        catch (Exception ex) {
            promise.reject("ERR_UNEXPECTED_EXCEPTION", ex);
        }
    }


    @ReactMethod
    public void hideNotification(final Promise promise)
    {
        Logger.d(NAME, "hideNotification");

        try{
            binder.hideNotification();
            promise.resolve(true);
        }

        catch (Exception ex) {
            promise.reject("ERR_UNEXPECTED_EXCEPTION", ex);
        }
    }
}