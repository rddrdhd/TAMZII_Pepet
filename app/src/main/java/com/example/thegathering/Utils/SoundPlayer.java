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
    private static int hitItemSound;
    private static int hitBoostSound;
    private static int hitBlackSound;

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

        hitItemSound = soundPool.load(context, R.raw.bite, 1);
        hitBoostSound = soundPool.load(context, R.raw.burp, 1);
        hitBlackSound = soundPool.load(context, R.raw.punch, 1);
    }

    public void playHitItemSound() {
        soundPool.play(hitItemSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playHitBoostSound() {
        soundPool.play(hitBoostSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playHitBlackSound() {
        soundPool.play(hitBlackSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }
}

