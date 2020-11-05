package com.cuijeb.animatedgame;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class DrawView extends View {
    Paint paint = new Paint();
    Sprite sprite = new Sprite();
    Sprite foodSprite, badSprite;

    public DrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        // getHeight() and getWidth now have the dimensions
        foodSprite = generateSprite();
        badSprite = generateSprite();
        badSprite.setColor(Color.GREEN);
        sprite.grow(100);
        sprite.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bluejeans));
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
        // updates sprite and foodSprite and badSprite
        sprite.update(canvas);
        foodSprite.update(canvas);
        badSprite.update(canvas);
        // Checks if they collided
        if (RectF.intersects(sprite, foodSprite)) {
            foodSprite = generateSprite();
            sprite.grow(10);
        }

        // Checks if the sprite collide with the bad Sprite
        if (RectF.intersects(sprite, badSprite)) {
            badSprite = generateSprite();
            badSprite.setColor(Color.GREEN);
            sprite.grow(-5);
        }

        // if food and bad sprite interacts
        if (RectF.intersects(foodSprite, badSprite)) {
            // Shrink food
            foodSprite.grow((int)(-foodSprite.width()*.1));
            // recreate the badsprite
            badSprite = generateSprite();
            badSprite.setColor(Color.GREEN);
        }

        // Sprite draws itself
        sprite.draw(canvas);
        // food sprite draws itself
        foodSprite.draw(canvas);
        // the bad sprite draws itself
        badSprite.draw(canvas);
        // redraws screen invokes onDraw()
        invalidate();
    }

    // Generates a sprite
    private Sprite generateSprite() {
        // position
        float x = (float)(Math.random() * (getWidth() - 0.1 * getWidth()));
        float y = (float)(Math.random() * (getHeight() - 0.1 * getHeight()));
        // velocity
        int dX = (int)(Math.random() * 11 - 5);
        int dY = (int)(Math.random() * 11 - 5);
        // returns the sprite it makes
        return new Sprite(x, y, x + 0.1f * getWidth(), y + 0.1f * getWidth(), dX, dY, Color.MAGENTA);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (badSprite.contains(event.getX(), event.getY())) {
                badSprite = generateSprite();
                badSprite.setColor(Color.GREEN);
            }
        }
        return true;
    }
}
