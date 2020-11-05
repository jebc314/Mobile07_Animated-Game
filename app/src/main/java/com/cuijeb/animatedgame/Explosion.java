package com.cuijeb.animatedgame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import java.util.ArrayList;

public class Explosion extends Sprite {
    private int duration = 100;
    private ArrayList explosions;
    private Bitmap bitmap;
    private static final int BMP_COLUMNS = 5;
    private static final int BMP_ROWS = 5;
    private int currentFrame=0, iconWidth, iconHeight, animationDelay=20;
    public Explosion(RectF r, ArrayList<Explosion> list, Bitmap i){
        super(r);
        explosions=list;
        //adds itself to list of explosions
        explosions.add(this);
        bitmap=i;
    }

    public void update() {
        // duration and self removal
        duration--;
        if(duration<=0) {
            // self removal from list
            explosions.remove(this);
        }
        // increment to next sprite image after delay
        if(animationDelay--<0) {
            // cycles current image with boundary proteciton
            currentFrame = ++currentFrame % BMP_COLUMNS;
            // arbitrary delay before cycling to next image
            animationDelay=20;
        }
    }

    public void draw(Canvas canvas){
        // calculate width of 1 image
        iconWidth = bitmap.getWidth() / BMP_COLUMNS;
        // calculate height of 1 image
        iconHeight = bitmap.getHeight() / BMP_ROWS;
        // set x of source rectangle inside of bitmap based on current frame
        int srcX = currentFrame * iconWidth;
        // set y to row of bitmap based on direction
        int srcY = getAnimationRow() * iconHeight;
        // defines the rectangle inside of heroBmp to displayed
        Rect src = new Rect(srcX, srcY, srcX + iconWidth, srcY + iconHeight);
        //draw an image
        canvas.drawBitmap(bitmap,src, this,null);
    }
    private int getAnimationRow() {
        //int division to get current row
        return currentFrame/BMP_ROWS;
    }
}
