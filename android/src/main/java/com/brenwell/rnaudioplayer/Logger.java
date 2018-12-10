package com.brenwell.rnaudioplayer;

import android.os.Looper;
import android.util.Log;

public class Logger {
    public static void  d(String klass, String msg){
        String thread = (Looper.myLooper() == Looper.getMainLooper()) ? "MT" : "BT";
        Log.d("RNAPLAYER:"+klass, msg+" => "+thread);
    }
}
