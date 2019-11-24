package com.example.thegathering;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.example.thegathering.MainThread;


public class FirstGameView extends android.view.SurfaceView implements android.view.SurfaceHolder.Callback  {
    private MainThread thread;

    public FirstGameView(Context context) {
        super(context);

        getHolder().addCallback(this);


        thread = new MainThread(getHolder(), this);
        setFocusable(true);
    }

    public void update(){

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread.setRunning(true);
        thread.start();
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
}
