package com.example.thegathering.Second;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thegathering.R;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class SecondActivity extends AppCompatActivity implements SensorEventListener {
    private TextView textView;
    private SensorManager sensorManager;
    private Sensor sensor;
    private FrameLayout danceFrame;
    private LinearLayout startLayout;
    private int frameHeight, frameWidth;


    private ImageView arrow, arrowUp, arrowLeft, arrowRight, arrowDown;
    private ImageView[] arrows;

    private int tilt;
    private final int NO = 0;
    private final int UP = 1;
    private final int DOWN = 2;
    private final int LEFT = 3;
    private final int RIGHT = 4;

    private float arrowX, arrowY;
    private float upX, upY, leftX, leftY, rightX, rightY, downX, downY;
    private ProgressBar progress;

    private Timer timer;
    private Handler handler = new Handler();

    private SharedPreferences settings;
    private int score, highScore, timeCount;
    private TextView scoreLabel, highScoreLabel;

    private boolean start_flg = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        tilt = NO;

        textView = findViewById(R.id.textView);
        danceFrame = findViewById(R.id.danceFrame);
        scoreLabel = findViewById(R.id.scoreLabel2);
        highScoreLabel = findViewById(R.id.highScoreLabel2);
        progress = findViewById(R.id.progressBarDance);
        startLayout = findViewById(R.id.startLayout2);

        arrowUp = findViewById(R.id.arrowUp);
        arrowDown = findViewById(R.id.arrowDown);
        arrowLeft = findViewById(R.id.arrowLeft);
        arrowRight = findViewById(R.id.arrowRight);
        arrows = new ImageView[]{arrowUp, arrowLeft, arrowRight, arrowDown};

        startLayout.setVisibility(View.VISIBLE);
        arrowRight.setVisibility(View.INVISIBLE);
        arrowDown.setVisibility(View.INVISIBLE);
        arrowLeft.setVisibility(View.INVISIBLE);
        arrowUp.setVisibility(View.INVISIBLE);


        settings = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
        highScore = settings.getInt("HIGH_SCORE_SECOND", 0);
        highScoreLabel.setText("High Score : " + highScore);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);



    }
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //unregister Sensor listener
        sensorManager.unregisterListener(this);
        finish();
    }

    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        if (Math.abs(x) > Math.abs(y)) {
            if (x < 0) {
                textView.setText("RIGHT");
                tilt = RIGHT;
            }
            if (x > 0) {
                textView.setText("LEFT");
                tilt = LEFT;
            }
        } else {
            if (y < 0) {
                textView.setText("UP");
                tilt = UP;
            }
            if (y > 0) {
                textView.setText("DOWN");
                tilt = DOWN;
            }
        }
        if (x > (-2) && x < (2) && y > (-2) && y < (2)) {
            textView.setText("NO TILT");
            tilt = NO;
        }
    }
    public static ImageView getRandom(ImageView[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }

    public void startGame(View view){

        start_flg = true;
        timeCount = 0;
        score = 0;

        startLayout.setVisibility(View.INVISIBLE);

        arrowUp.setY(3000.0f);
        arrowDown.setY(3000.0f);
        arrowRight.setY(3000.0f);
        arrowLeft.setY(3000.0f);

        arrowRight.setVisibility(View.VISIBLE);
        arrowDown.setVisibility(View.VISIBLE);
        arrowLeft.setVisibility(View.VISIBLE);
        arrowUp.setVisibility(View.VISIBLE);

        upY = arrowUp.getY();
        downY = arrowDown.getY();
        rightY = arrowRight.getY();
        leftY = arrowLeft.getY();

        if (frameHeight == 0) {
            frameHeight = danceFrame.getHeight();
            frameWidth = danceFrame.getWidth();
        }

        scoreLabel.setText("Score : 0");

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (start_flg) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            logic();
                        }
                    });
                }
            }
        }, 0, 20);
    }

    private void logic(){

        if(arrow == null)
            arrow = getRandom(arrows);
        arrowX = arrow.getX();
        arrowY = arrow.getY();

        timeCount += 20;
        arrowY += 12;

        float arrowCenterX = arrowX + arrow.getWidth()/2;
        float arrowCenterY = arrowY + arrow.getHeight()/2;

        if (hitCheck(arrowCenterY)){
            arrow = getRandom(arrows);
            arrowY = frameHeight+100;
            arrowX = new Random().nextInt(frameWidth-200)+200;
        }

        arrow.setX(arrowX);
        arrow.setY(arrowY);


        scoreLabel.setText("Score : " + score);

    }
    public boolean hitCheck(float y){
        if(y <= frameHeight && progress.getY() <= y){
            Log.d("second","HITTING CHECK");
            return true;
        }
        return false;
    }


    public void gameOver(){
        timer.cancel();
        timer = null;
        start_flg = false;

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        startLayout.setVisibility(View.VISIBLE);
        arrowRight.setVisibility(View.INVISIBLE);
        arrowDown.setVisibility(View.INVISIBLE);
        arrowLeft.setVisibility(View.INVISIBLE);
        arrowUp.setVisibility(View.INVISIBLE);
    }

    public void quitGame(View view){
        finish();
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
