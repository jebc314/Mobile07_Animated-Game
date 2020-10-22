package com.cuijeb.animatedgame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class Sprite extends RectF {
    private int dX, dY, color;

    // Constructors
    public Sprite() {
        this(1, 2, Color.RED);
    }
    public Sprite(int dX, int dY, int color) {
        this(1, 1, 11, 11, dX, dY, color);
    }
    public Sprite(float left, float top, float right, float bottom) {
        this(left, top, right, bottom, 1, 2, Color.RED);
    }
    public Sprite(float left, float top, float right, float bottom, int dX, int dY, int color) {
        super(left, top, right, bottom);
        this.dX = dX;
        this.dY = dY;
        this.color = color;
    }

    // Methods
    public void update() {
        // Moves dX to the right and dY downwards
        offset(dX, dY);
    }
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        // Set its color
        paint.setColor(color);
        // Draw circle
        canvas.drawCircle(centerX(), centerY(), width() / 2, paint);
    }
    // Accessor and modifier methods
    public int getdX() {
        return dX;
    }

    public void setdX(int dX) {
        this.dX = dX;
    }

    public int getdY() {
        return dY;
    }

    public void setdY(int dY) {
        this.dY = dY;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
