package com.example.thegathering;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import java.util.Random;


public class FirstGameView
        extends android.view.SurfaceView
        implements android.view.SurfaceHolder.Callback  {

    private MainThread thread;
    private FirstGameCharSprite characterSprite;
    public FirstGamePipeSprite pipe1, pipe2, pipe3;

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
        characterSprite.y = characterSprite.y - (characterSprite.yVelocity * 10);
        return super.onTouchEvent(event);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread.setRunning(true);
        thread.start();
        Bitmap bmp, bmp1, bmp2, resized, resized1, resized2;

        int y;
        int x;

        bmp = BitmapFactory.decodeResource(getResources(),R.drawable.squarepepe);
        bmp1 = BitmapFactory.decodeResource(getResources(), R.drawable.wall);
        bmp2 = BitmapFactory.decodeResource(getResources(), R.drawable.wall);

        resized =  getResizedBitmap( bmp, 250, 250);
        resized1 =  getResizedBitmap( bmp1, 300, Resources.getSystem().getDisplayMetrics().heightPixels /-2);
        resized2 =  getResizedBitmap( bmp2, 300, Resources.getSystem().getDisplayMetrics().heightPixels /2);

        characterSprite = new FirstGameCharSprite(resized);
        pipe1 = new FirstGamePipeSprite(resized1, resized2, 0, 0);
        pipe2 = new FirstGamePipeSprite(resized1, resized2, 800, -250);
        pipe3 = new FirstGamePipeSprite(resized1, resized2, 1600, 250);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

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
           // Paint paint = new Paint();
           // paint.setColor(Color.rgb(250, 0, 0));
           // canvas.drawRect(100, 100, 200, 200, paint);
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

        //Detect if the character is touching one of the pipes
        if (characterSprite.y < pipe1.yY + (screenHeight / 2) - (gapHeight / 2)
                && characterSprite.x + 300 > pipe1.xX && characterSprite.x < pipe1.xX + 500)
        { resetLevel(); }

        if (characterSprite.y < pipe2.yY + (screenHeight / 2) - (gapHeight / 2)
                && characterSprite.x + 300 > pipe2.xX && characterSprite.x < pipe2.xX + 500)
        { resetLevel(); }

        if (characterSprite.y < pipe3.yY + (screenHeight / 2) - (gapHeight / 2)
                && characterSprite.x + 300 > pipe3.xX && characterSprite.x < pipe3.xX + 500)
        { resetLevel(); }

        if (characterSprite.y + 240 > (screenHeight / 2) + (gapHeight / 2) + pipe1.yY
                && characterSprite.x + 300 > pipe1.xX && characterSprite.x < pipe1.xX + 500)
        { resetLevel(); }

        if (characterSprite.y + 240 > (screenHeight / 2) + (gapHeight / 2) + pipe2.yY
                && characterSprite.x + 300 > pipe2.xX && characterSprite.x < pipe2.xX + 500)
        { resetLevel(); }

        if (characterSprite.y + 240 > (screenHeight / 2) + (gapHeight / 2) + pipe3.yY
                && characterSprite.x + 300 > pipe3.xX && characterSprite.x < pipe3.xX + 500)
        { resetLevel(); }

        //Detect if the character has gone off the
        //bottom or top of the screen
        if (characterSprite.y + 240 < 0) {
            resetLevel(); }
        if (characterSprite.y > screenHeight) {
            resetLevel(); }


        //If the pipe goes off the left of the screen,
        //put it forward at a randomized distance and height
        if (pipe1.xX + 500 < 0) {
            Random r = new Random();
            int value1 = r.nextInt(500);
            int value2 = r.nextInt(500);
            pipe1.xX = screenWidth + value1 + 1000;
            pipe1.yY = value2 - 250;
        }

        if (pipe2.xX + 500 < 0) {
            Random r = new Random();
            int value1 = r.nextInt(500);
            int value2 = r.nextInt(500);
            pipe2.xX = screenWidth + value1 + 1000;
            pipe2.yY = value2 - 250;
        }

        if (pipe3.xX + 500 < 0) {
            Random r = new Random();
            int value1 = r.nextInt(500);
            int value2 = r.nextInt(500);
            pipe3.xX = screenWidth + value1 + 1000;
            pipe3.yY = value2 - 250;
        }
    }

    public void resetLevel() {
        characterSprite.y = 100;
        pipe1.xX = 2000;
        pipe1.yY = 0;
        pipe2.xX = 4500;
        pipe2.yY = 200;
        pipe3.xX = 3200;
        pipe3.yY = 250;

    }
}
