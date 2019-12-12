package com.example.thegathering.First;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class FirstGamePipeSprite {
    private Bitmap image, image2;
    public int xX, yY;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    public FirstGamePipeSprite (Bitmap bmp, Bitmap bmp2, int x, int y) {
        image = bmp;
        image2 = bmp2;
        yY = y;
        xX = x;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, xX,
                -(FirstGameView.gapHeight / 2) + yY,
                null);
        canvas.drawBitmap(image2, xX,
                ((screenHeight / 2) + (FirstGameView.gapHeight / 2)) + yY,
                null);
    }

    public void update() {
        xX -= FirstGameView.velocity;
    }

}
