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
        // Set paint to red
        paint.setColor(Color.RED);
        // Draw red circle
        canvas.drawCircle(getWidth()*.5f, getHeight()*.2f, getWidth()*.3f, paint);

    }
}