package com.cuijeb.animatedgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class DrawView extends View {
    Paint paint = new Paint();
    Sprite sprite = new Sprite();
    Sprite foodSprite;

    public DrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        // getHeight() and getWidth now have the dimensions
        foodSprite = generateSprite();
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
        // updates sprite and foodSprite
        sprite.update(canvas);
        foodSprite.update(canvas);
        // Checks if they collided
        if (RectF.intersects(sprite, foodSprite)) {
            foodSprite = generateSprite();
            sprite.grow(10);
        }

        // Sprite draws itself
        sprite.draw(canvas);
        // food sprite draws itself
        foodSprite.draw(canvas);
        // redraws screen invokes onDraw()
        invalidate();
    }

    // Generates a sprite
    private Sprite generateSprite() {
        // position
        float x = (float)(Math.random() * getWidth() - 0.1 * getWidth());
        float y = (float)(Math.random() * getHeight() - 0.1 * getHeight());
        // velocity
        int dX = (int)(Math.random() * 11 - 5);
        int dY = (int)(Math.random() * 11 - 5);
        // returns the sprite it makes
        return new Sprite(x, y, x + 0.1f * getWidth(), y + 0.1f * getWidth(), dX, dY, Color.MAGENTA);

    }
}
