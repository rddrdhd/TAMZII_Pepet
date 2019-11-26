package com.example.thegathering.First;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class FirstGameCharSprite {
    private Bitmap image;
    public int x, y;
    private int xVelocity = 10;
    public int yVelocity = 5;
    public FirstGameCharSprite(Bitmap bmp){
        image = bmp;
        x = 100;
        y = 100;
    }
    public void draw (Canvas canvas) {
        canvas.drawBitmap(image, x, y, null);
    }

    public void update(){
        y += yVelocity;
    }

}
