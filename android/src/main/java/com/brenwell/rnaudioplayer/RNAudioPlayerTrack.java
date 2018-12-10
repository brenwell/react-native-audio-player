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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.util.Log;

import com.facebook.react.bridge.ReadableMap;


public final class RNAudioPlayerTrack {

    public final Uri uri;
    public final Uri artwork;
    public final String title;
    public final String description;
//    public final int bitmapResource;

    public RNAudioPlayerTrack(ReadableMap options) {

        this.uri = getUrlForKey(options,"uri");
        this.artwork = getUrlForKey(options,"artwork");
        this.title = options.hasKey("title") ? options.getString("title") : null;
        this.description =options.hasKey("description") ? options.getString("description") : null;
    }

    @Override
    public String toString() {
        return title;
    }

    public MediaDescriptionCompat getMediaDescription(Context context) {
        Bundle extras = new Bundle();


//        extras.putParcelable(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, bitmap);
//        extras.putParcelable(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON, bitmap);
        return new MediaDescriptionCompat.Builder()
//                .setMediaId(this.mediaId)
                .setIconUri(this.artwork)
                .setTitle(this.title)
                .setDescription(this.description)
                .setExtras(extras)
                .build();
    }

    private Uri getUrlForKey(ReadableMap options, String key)
    {
        if (!options.hasKey("uri")) return null;

        return Uri.parse(options.getString("uri"));
    }
}


