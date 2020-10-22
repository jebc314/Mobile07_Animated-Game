package com.cuijeb.animatedgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class DrawView extends View {
    Paint paint = new Paint();
    // Set the initial y position and vertical speed
    int y = 0;
    int dy = 1;

    public DrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Set paint to gray
        paint.setColor(Color.GRAY);
        // Paint background gray
        canvas.drawRect(getLeft(), 0, getRight(), getBottom(), paint);
        // Or
        // canvas.drawRext(0, 0, getWidth, getHeight, paint);
        // Set paint to red
        paint.setColor(Color.RED);
        // Draw red circle
        canvas.drawCircle(getWidth()*.5f, y, getWidth()*.3f, paint);
        // increments y position
        y += dy;
        // redraws screen invokes onDraw()
        invalidate();
    }
}
