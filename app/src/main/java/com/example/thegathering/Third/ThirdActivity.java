package com.example.thegathering.Third;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thegathering.R;
import com.example.thegathering.Utils.Score;
import com.example.thegathering.Utils.SoundPlayer;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class ThirdActivity extends AppCompatActivity {
    // pepe - main hero,
    // goodItem - falling food,
    // betterItem - falling drink,
    // badItem - falling bad thing

    // frame
    private FrameLayout gameFrame;
    private int frameHeight, frameWidth, initialFrameWidth;
    private LinearLayout startLayout;

    // image
    private ImageView pepe, badItem, goodItem, betterItem;
    private Drawable imagePepeRight, imagePepeLeft;

    // size
    private int pepeSize;

    // position
    private float pepeX, pepeY;
    private float badItemX, badItemY;
    private float goodItemX, goodItemY;
    private float betterItemX, betterItemY;

    // score
    private TextView scoreLabel, highScoreLabel;
    private int score, highScore, timeCount;
    private SharedPreferences settings;

    // utils
    private Timer timer;
    private Handler handler = new Handler();
    private SoundPlayer soundPlayer;
    Random r = new Random();

    // flags
    private boolean start_flg = false;
    private boolean action_flg = false;
    private boolean boost_flg = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        soundPlayer = new SoundPlayer(this);

        gameFrame = findViewById(R.id.gameFrame);
        startLayout = findViewById(R.id.startLayout);
        pepe = findViewById(R.id.pepeThird);
        badItem = findViewById(R.id.black);
        goodItem = findViewById(R.id.item);
        betterItem = findViewById(R.id.boost);
        scoreLabel = findViewById(R.id.scoreLabel3);
        highScoreLabel = findViewById(R.id.highScoreLabel3);

        imagePepeLeft = getResources().getDrawable(R.drawable.game_pepe_hungry);
        imagePepeRight = getResources().getDrawable(R.drawable.game_pepe_hungry2);

        // High Score
        settings = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
        highScore = settings.getInt("HIGH_SCORE_THIRD", 0);
        highScoreLabel.setText("High Score : " + highScore);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    public void changePos() {

        timeCount += 20;
        goodItemY += 12;

        float itemCenterX = goodItemX + goodItem.getWidth() / 2;
        float itemCenterY = goodItemY + goodItem.getHeight() / 2;

        if (hitCheck(itemCenterX, itemCenterY)) {
            goodItemY = frameHeight + 100;
            score += r.nextInt(5);
            soundPlayer.playHitGoodItemSound();
        }

        if (goodItemY > frameHeight) {
            goodItemY = -100;
            goodItemX = (float) Math.floor(Math.random() * (frameWidth - goodItem.getWidth()));
        }

        goodItem.setX(goodItemX);
        goodItem.setY(goodItemY);

        if (!boost_flg && timeCount % 10000 == 0) {
            boost_flg = true;
            betterItemY = -20;
            betterItemX = (float) Math.floor(Math.random() * (frameWidth - betterItem.getWidth()));
        }

        if (boost_flg) {
            betterItemY += 20;

            float boostCenterX = betterItemX + betterItem.getWidth() / 2;
            float boostCenterY = betterItemY + betterItem.getWidth() / 2;

            if (hitCheck(boostCenterX, boostCenterY)) {
                betterItemY = frameHeight + 30;
                score += 10+r.nextInt(5);;
                // Change FrameWidth
                if (initialFrameWidth > frameWidth * 110 / 100) {
                    frameWidth = frameWidth * 110 / 100;
                    changeFrameWidth(frameWidth);
                }
                soundPlayer.playHitBetterItemSound();
            }

            if (betterItemY > frameHeight) boost_flg = false;
            betterItem.setX(betterItemX);
            betterItem.setY(betterItemY);
        }

        badItemY += 18;

        float blackCenterX = badItemX + badItem.getWidth() / 2;
        float blackCenterY = badItemY + badItem.getHeight() / 2;

        if (hitCheck(blackCenterX, blackCenterY)) {
            badItemY = frameHeight + 100;

            // Change FrameWidth
            frameWidth = frameWidth * 80 / 100;
            changeFrameWidth(frameWidth);
            soundPlayer.playHitBadItemSound();
            if (frameWidth <= pepeSize) {
                gameOver();
            }

        }

        if (badItemY > frameHeight) {
            badItemY = -100;
            badItemX = (float) Math.floor(Math.random() * (frameWidth - badItem.getWidth()));
        }

        badItem.setX(badItemX);
        badItem.setY(badItemY);

        // Move Pepe
        if (action_flg) {
            // Touching
            pepeX += 14;
            pepe.setImageDrawable(imagePepeRight);
        } else {
            // Releasing
            pepeX -= 14;
            pepe.setImageDrawable(imagePepeLeft);
        }

        // Check pepe position.
        if (pepeX < 0) {
            pepeX = 0;
            pepe.setImageDrawable(imagePepeRight);
        }
        if (frameWidth - pepeSize < pepeX) {
            pepeX = frameWidth - pepeSize;
            pepe.setImageDrawable(imagePepeLeft);
        }

        pepe.setX(pepeX);

        scoreLabel.setText("Score : " + score);

    }

    public boolean hitCheck(float x, float y) {
        if (pepeX <= x && x <= pepeX + pepeSize &&
                pepeY <= y && y <= frameHeight) {
            return true;
        }
        return false;
    }

    public void changeFrameWidth(int frameWidth) {
        ViewGroup.LayoutParams params = gameFrame.getLayoutParams();
        params.width = frameWidth;
        gameFrame.setLayoutParams(params);
    }

    public void gameOver() {
        // Stop timer.
        timer.cancel();
        timer = null;
        start_flg = false;

        // Before showing startLayout, sleep 1 second.
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        changeFrameWidth(initialFrameWidth);

        startLayout.setVisibility(View.VISIBLE);
        pepe.setVisibility(View.INVISIBLE);
        badItem.setVisibility(View.INVISIBLE);
        goodItem.setVisibility(View.INVISIBLE);
        betterItem.setVisibility(View.INVISIBLE);

        // Update Score
        Score.thirdGame += score;
        if (score > highScore) {
            highScore = score;
            highScoreLabel.setText("High Score : " + highScore);

            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("HIGH_SCORE_THIRD", highScore);
            editor.commit();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (start_flg) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                action_flg = true;
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                action_flg = false;
            }
        }
        return true;
    }

    public void startGame(View view) {
        start_flg = true;
        startLayout.setVisibility(View.INVISIBLE);

        if (frameHeight == 0) {
            frameHeight = gameFrame.getHeight();
            frameWidth = gameFrame.getWidth();
            initialFrameWidth = frameWidth;
            pepeSize = pepe.getHeight();
            pepeX = pepe.getX();
            pepeY = pepe.getY();
        }

        frameWidth = initialFrameWidth;

        pepe.setX(0.0f);
        badItem.setY(3000.0f);
        goodItem.setY(3000.0f);
        betterItem.setY(3000.0f);

        badItemY = badItem.getY();
        goodItemY = goodItem.getY();
        betterItemY = betterItem.getY();

        pepe.setVisibility(View.VISIBLE);
        badItem.setVisibility(View.VISIBLE);
        goodItem.setVisibility(View.VISIBLE);
        betterItem.setVisibility(View.VISIBLE);

        timeCount = 0;
        score = 0;
        scoreLabel.setText("Score : 0");


        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (start_flg) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changePos();
                        }
                    });
                }
            }
        }, 0, 20);
    }

    public void quitGame(View view) {
        soundPlayer.stop();
        finish();
    }


    @Override
    public void onBackPressed() {
        gameOver();
        soundPlayer.stop();
        finish();
        super.onBackPressed();
    }
}
