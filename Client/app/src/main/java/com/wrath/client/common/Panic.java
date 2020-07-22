package com.wrath.client.common;

import android.content.Context;
import android.media.MediaPlayer;

import com.wrath.client.R;

public class Panic {
    public void initiatePanic(Context context){
        final MediaPlayer mp = MediaPlayer.create(context, R.raw.sound);
        mp.start();
    }
}
