package com.example.thegathering;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;


public class FirstGameView extends android.view.SurfaceView implements android.view.SurfaceHolder.Callback  {
    private MainThread thread;

    private FirstGameCharSprite charSprite;
    public FirstGameView(Context context) {
        super(context);

        getHolder().addCallback(this);


        thread = new MainThread(getHolder(), this);
        setFocusable(true);
    }

    public void update(){
        charSprite.update();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread.setRunning(true);
        thread.start();
        charSprite = new FirstGameCharSprite(BitmapFactory.decodeResource(getResources(),R.drawable.happyicon));
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
            canvas.drawColor(R.color.colorPrimaryDark);
           // Paint paint = new Paint();
           // paint.setColor(Color.rgb(250, 0, 0));
           // canvas.drawRect(100, 100, 200, 200, paint);
            charSprite.draw(canvas);
        }
    }
}
