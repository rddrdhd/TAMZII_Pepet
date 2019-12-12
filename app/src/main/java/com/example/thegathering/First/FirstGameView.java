package com.example.thegathering.First;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.example.thegathering.R;
import com.example.thegathering.Utils.MainThread;
import com.example.thegathering.Utils.Score;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class FirstGameView extends android.view.SurfaceView implements android.view.SurfaceHolder.Callback  {

    private MainThread thread;
    private FirstGameCharSprite characterSprite;
    public FirstGamePipeSprite pipe1, pipe2, pipe3;

    public static int heroSize = 200;
    public static int gapHeight = 500;
    public static int velocity = 8;

    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;


    public FirstGameView(Context context) {
        super(context);

        getHolder().addCallback(this);
        thread = new MainThread(getHolder(), this);
        setFocusable(true);
    }

    public void update(){
        logic();
        characterSprite.update();
        pipe1.update();
        pipe2.update();
        pipe3.update();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        characterSprite.y = characterSprite.y - (characterSprite.yVelocity * 12);

        Log.d("score", Score.firstGame+"");
        Log.d("scoreRound",Score.firstGameRound+"");
        Log.d("scoreRecord",Score.firstGameRecord+"");
        return super.onTouchEvent(event);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread.setRunning(true);
        thread.start();
        Score.firstGame = 0;
        Score.firstGameRound = 0;
        Bitmap bmp, bmp1, bmp2, resized, resized1, resized2;


        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.squarepepe);
        bmp1 = BitmapFactory.decodeResource(getResources(), R.drawable.pipe);
        bmp2 = BitmapFactory.decodeResource(getResources(), R.drawable.pipe);

        //character
        resized =  getResizedBitmap( bmp, heroSize, heroSize);

        //pipes
        resized1 =  getResizedBitmap( bmp1, 300, Resources.getSystem().getDisplayMetrics().heightPixels /-2);
        resized2 =  getResizedBitmap( bmp2, 300, Resources.getSystem().getDisplayMetrics().heightPixels /2);

        characterSprite = new FirstGameCharSprite(resized);
        pipe1 = new FirstGamePipeSprite(resized1, resized2, 0, 0);
        pipe2 = new FirstGamePipeSprite(resized1, resized2, 800, -250);
        pipe3 = new FirstGamePipeSprite(resized1, resized2, 1600, 250);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) { }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }

    }


    @SuppressLint("ResourceAsColor")
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {
            canvas.drawColor(R.color.white);
            characterSprite.draw(canvas);
            pipe1.draw(canvas);
            pipe2.draw(canvas);
            pipe3.draw(canvas);
        }
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap =
                Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    public void logic() {

        List pipes = new ArrayList<>();
        pipes.add(pipe1);
        pipes.add(pipe2);
        pipes.add(pipe3);

        for (int i = 0; i < pipes.size(); i++) {
            FirstGamePipeSprite pipe = (FirstGamePipeSprite) pipes.get(i);
            //Detect if the character is touching one of the pipes
            if (characterSprite.y < pipe.yY + (screenHeight / 2) - (gapHeight / 2)
                    && characterSprite.x + heroSize > pipe.xX
                    && characterSprite.x < pipe.xX + 500) {
                resetLevel();
            } else if (characterSprite.y + heroSize > (screenHeight / 2) + (gapHeight / 2) + pipe.yY
                    && characterSprite.x + heroSize > pipe.xX
                    && characterSprite.x < pipe.xX + 500) {
                resetLevel();
            } else if(characterSprite.x + 5 > pipe.xX && characterSprite.x - 5 < pipe.xX) {
                Score.firstGameRound++;
            }

            //Detect if the pipe has gone off the left of the
            //screen and regenerate further ahead
            if (pipe.xX + 500 < 0) {
                Random r = new Random();
                int value1 = r.nextInt(500);
                int value2 = r.nextInt(500);
                pipe.xX = screenWidth + value1 + 1600;
                pipe.yY = value2 - 250;
            }
        }

        if (characterSprite.y + heroSize < 0) {
            resetLevel(); }
        if (characterSprite.y > screenHeight) {
            resetLevel(); }
    }

    public void resetLevel() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        characterSprite.y = 100;
        pipe1.xX = 2000;
        pipe1.yY = 0;
        pipe2.xX = 4500;
        pipe2.yY = 200;
        pipe3.xX = 3200;
        pipe3.yY = 250;

        Score.firstGame += Score.firstGameRound;
        if(Score.firstGameRound>Score.firstGameRecord) Score.firstGameRecord=Score.firstGameRound;
        Score.firstGameRound = 0;


    }

}
