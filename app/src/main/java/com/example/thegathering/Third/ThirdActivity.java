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
import com.example.thegathering.Utils.SoundPlayer;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class ThirdActivity extends AppCompatActivity {
    // uses pepe - main hero,
    // item - falling food,
    // boost - falling drink,
    // black - falling bad thing

    // frame
    private FrameLayout gameFrame;
    private int frameHeight, frameWidth, initialFrameWidth;
    private LinearLayout startLayout;

    // image
    private ImageView pepe, black, item, boost;
    private Drawable imagePepeRight, imagePepeLeft;

    // size
    private int pepeSize;

    // position
    private float pepeX, pepeY;
    private float blackX, blackY;
    private float itemX, itemY;
    private float boostX, boostY;

    // score
    private TextView scoreLabel, highScoreLabel;
    private int score, highScore, timeCount;
    private SharedPreferences settings;

    // utils
    private Timer timer;
    private Handler handler = new Handler();
    private SoundPlayer soundPlayer;

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
        black = findViewById(R.id.black);
        item = findViewById(R.id.item);
        boost = findViewById(R.id.boost);
        scoreLabel = findViewById(R.id.scoreLabel3);
        highScoreLabel = findViewById(R.id.highScoreLabel3);

        imagePepeLeft = getResources().getDrawable(R.drawable.hungry);
        imagePepeRight = getResources().getDrawable(R.drawable.hungry2);

        // High Score
        settings = getSharedPreferences("GAME_DATA_THIRD", Context.MODE_PRIVATE);
        highScore = settings.getInt("HIGH_SCORE_THIRD", 0);
        highScoreLabel.setText("High Score : " + highScore);
    }

    public void changePos() {

        timeCount += 20;
        itemY += 12;

        float itemCenterX = itemX + item.getWidth() / 2;
        float itemCenterY = itemY + item.getHeight() / 2;

        if (hitCheck(itemCenterX, itemCenterY)) {
            itemY = frameHeight + 100;
            score += 10;
            soundPlayer.playHitItemSound();
        }

        if (itemY > frameHeight) {
            itemY = -100;
            itemX = (float) Math.floor(Math.random() * (frameWidth - item.getWidth()));
        }
        item.setX(itemX);
        item.setY(itemY);


        if (!boost_flg && timeCount % 10000 == 0) {
            boost_flg = true;
            boostY = -20;
            boostX = (float) Math.floor(Math.random() * (frameWidth - boost.getWidth()));
        }

        if (boost_flg) {
            boostY += 20;

            float boostCenterX = boostX + boost.getWidth() / 2;
            float boostCenterY = boostY + boost.getWidth() / 2;

            if (hitCheck(boostCenterX, boostCenterY)) {
                boostY = frameHeight + 30;
                score += 30;
                // Change FrameWidth
                if (initialFrameWidth > frameWidth * 110 / 100) {
                    frameWidth = frameWidth * 110 / 100;
                    changeFrameWidth(frameWidth);
                }
                soundPlayer.playHitBoostSound();
            }

            if (boostY > frameHeight) boost_flg = false;
            boost.setX(boostX);
            boost.setY(boostY);
        }

        // Black
        blackY += 18;

        float blackCenterX = blackX + black.getWidth() / 2;
        float blackCenterY = blackY + black.getHeight() / 2;

        if (hitCheck(blackCenterX, blackCenterY)) {
            blackY = frameHeight + 100;

            // Change FrameWidth
            frameWidth = frameWidth * 80 / 100;
            changeFrameWidth(frameWidth);
            soundPlayer.playHitBlackSound();
            if (frameWidth <= pepeSize) {
                gameOver();
            }

        }

        if (blackY > frameHeight) {
            blackY = -100;
            blackX = (float) Math.floor(Math.random() * (frameWidth - black.getWidth()));
        }

        black.setX(blackX);
        black.setY(blackY);

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
        black.setVisibility(View.INVISIBLE);
        item.setVisibility(View.INVISIBLE);
        boost.setVisibility(View.INVISIBLE);

        // Update High Score
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
        black.setY(3000.0f);
        item.setY(3000.0f);
        boost.setY(3000.0f);

        blackY = black.getY();
        itemY = item.getY();
        boostY = boost.getY();

        pepe.setVisibility(View.VISIBLE);
        black.setVisibility(View.VISIBLE);
        item.setVisibility(View.VISIBLE);
        boost.setVisibility(View.VISIBLE);

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
        finish();
    }
}
