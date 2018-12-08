/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.brenwell.rnaudioplayer;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector;
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator;
//import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;
import com.google.android.exoplayer2.ui.PlayerNotificationManager.BitmapCallback;
import com.google.android.exoplayer2.ui.PlayerNotificationManager.MediaDescriptionAdapter;
import com.google.android.exoplayer2.ui.PlayerNotificationManager.NotificationListener;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class RNAudioPlayerService extends Service {

    public static final String TAG = "RNAudioPlayerService";

    IBinder mBinder = new LocalBinder();

    private RNAudioPlayerTrack currentTrack;
    private SimpleExoPlayer player;
    private PlayerNotificationManager playerNotificationManager;
    private MediaSessionCompat mediaSession;
    private MediaSessionConnector mediaSessionConnector;

    public static final int PLAYBACK_CHANNEL_ID = 1;
    public static final int PLAYBACK_NOTIFICATION_ID = 1;
    public static final String PLAYBACK_CHANNEL_NAME = "com.brenwell.rnaudioplayer.channelname";
    public static final String MEDIA_SESSION_TAG = "com.brenwell.rnaudioplayer.mediasession";
    public static final String APP_NAME = "com.brenwell.rnaudioplayer";

    @Override
    public void onCreate() {
        super.onCreate();
        final Context context = this;

        Log.d("Context", "add: " + context);

        player = ExoPlayerFactory.newSimpleInstance(context, new DefaultTrackSelector());

        playerNotificationManager = PlayerNotificationManager.createWithNotificationChannel(
                context,
                PLAYBACK_CHANNEL_NAME,
                PLAYBACK_CHANNEL_ID,
                PLAYBACK_NOTIFICATION_ID,
                new MediaDescriptionAdapter() {
                    @Override
                    public String getCurrentContentTitle(Player player) {
                        return currentTrack.title;
                    }

                    @Nullable
                    @Override
                    public PendingIntent createCurrentContentIntent(Player player) {
                        return null;
                    }

                    @Nullable
                    @Override
                    public String getCurrentContentText(Player player) {
                        return currentTrack.description;
                    }

                    @Nullable
                    @Override
                    public Bitmap getCurrentLargeIcon(Player player, BitmapCallback callback) {
                        return null;
//                        return track.getBitmap(
//                                context, track.bitmapResource);
                    }
                }
        );
        playerNotificationManager.setNotificationListener(new NotificationListener() {
            @Override
            public void onNotificationStarted(int notificationId, Notification notification) {
                startForeground(notificationId, notification);
            }

            @Override
            public void onNotificationCancelled(int notificationId) {
                stopSelf();
            }
        });
        playerNotificationManager.setPlayer(player);

        mediaSession = new MediaSessionCompat(context, MEDIA_SESSION_TAG);
        mediaSession.setActive(true);
        playerNotificationManager.setMediaSessionToken(mediaSession.getSessionToken());

        mediaSessionConnector = new MediaSessionConnector(mediaSession);
        mediaSessionConnector.setQueueNavigator(new TimelineQueueNavigator(mediaSession) {
            @Override
            public MediaDescriptionCompat getMediaDescription(int windowIndex) {
                return currentTrack.getMediaDescription(context);
            }
        });
        mediaSessionConnector.setPlayer(player, null);

    }


    @Override
    public void onDestroy() {
        mediaSession.release();
        mediaSessionConnector.setPlayer(null, null);
        playerNotificationManager.setPlayer(null);
        player.release();
        player = null;

        super.onDestroy();
    }

    public void add(RNAudioPlayerTrack track) {

        currentTrack = track;

        Log.d(TAG, "add: " + currentTrack);

        final Context context = this;

        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(
                context, Util.getUserAgent(context, APP_NAME));

        CacheDataSourceFactory cacheDataSourceFactory = new CacheDataSourceFactory(
                DownloadUtil.getCache(context),
                dataSourceFactory,
                CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR);

//        ConcatenatingMediaSource concatenatingMediaSource = new ConcatenatingMediaSource();

        MediaSource mediaSource = new ExtractorMediaSource.Factory(cacheDataSourceFactory)
                .createMediaSource(track.uri);
//        concatenatingMediaSource.addMediaSource(mediaSource);

        player.prepare(mediaSource);
        player.setPlayWhenReady(true);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }

    public class LocalBinder extends Binder {
        public RNAudioPlayerService getServerInstance() {
            return RNAudioPlayerService.this;
        }
    }
}