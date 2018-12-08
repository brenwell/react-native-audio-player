
package com.brenwell.rnaudioplayer;

import java.util.HashMap;
import java.util.Map;

public class RNAudioPlayerConstants {

    public static final String PLAYER_NONE = "PLAYER_NONE";
    public static final String PLAYER_LOADING = "PLAYER_LOADING";
    public static final String PLAYER_LOADED = "PLAYER_LOADED";
    public static final String PLAYER_BUFFERING = "PLAYER_BUFFERING";
    public static final String PLAYER_PLAYING = "PLAYER_PLAYING";
    public static final String PLAYER_PAUSED = "PLAYER_PAUSED";


    public static Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        constants.put(PLAYER_NONE,0);
        constants.put(PLAYER_LOADING,1);
        constants.put(PLAYER_LOADED,2);
        constants.put(PLAYER_BUFFERING,3);
        constants.put(PLAYER_PLAYING,4);
        constants.put(PLAYER_PAUSED,5);
        return constants;
    }
}