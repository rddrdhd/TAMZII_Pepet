package com.example.thegathering.Utils;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import com.example.thegathering.R;


public class SoundPlayer {
    private AudioAttributes audioAttributes;
    final int SOUND_POOL_MAX = 3;

    private static SoundPool soundPool;
    private static int hitGoodItemSound;
    private static int hitBetterItemSound;
    private static int hitBadItemSound;
    private static int saxGuySound;

    public SoundPlayer(Context context) {

        // SoundPool is deprecated in API level 21. (Lollipop)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .setMaxStreams(SOUND_POOL_MAX)
                    .build();

        } else {
            soundPool = new SoundPool(SOUND_POOL_MAX, AudioManager.STREAM_MUSIC, 0);
        }

        hitGoodItemSound = soundPool.load(context, R.raw.bite, 1);
        hitBetterItemSound = soundPool.load(context, R.raw.burp, 1);
        hitBadItemSound = soundPool.load(context, R.raw.punch, 1);
        saxGuySound = soundPool.load(context, R.raw.e_s_g, 1);
    }

    public void playHitGoodItemSound() {
        soundPool.play(hitGoodItemSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playHitBetterItemSound() {
        soundPool.play(hitBetterItemSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playHitBadItemSound() {
        soundPool.play(hitBadItemSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playSaxGuy() {
        soundPool.play(saxGuySound, 1.0f, 1.0f, 1, -1, 1.0f);
    }
    public void stop() {
        soundPool.autoPause();
    }
}

