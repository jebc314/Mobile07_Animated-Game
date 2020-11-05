package com.cuijeb.animatedgame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

public class Sprite extends RectF {
    private static final int BMP_COLUMNS = 4;
    private static final int BMP_ROWS = 4;
    private static final int DOWN = 0, LEFT = 1, RIGHT = 2, UP = 3;
    private int dX, dY, color;
    private Bitmap bitmap;
    private int currentFrame = 0, iconWidth, iconHeight, animationDelay = 20;

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
    public void update(Canvas canvas) {
        // if the next step hits boundary
        if (left + dX < 0 || right + dX > canvas.getWidth())
            // Bounce off left and right boundaries
            dX *= -1;
        // if next step goes out the bottom
        if (top + dY > canvas.getHeight())
            // Top of the screen
            offsetTo(left, -height());
        // goes out the top
        if (bottom + dY < 0)
            offsetTo(left, canvas.getHeight());
        // Moves dX to the right and dY downwards
        offset(dX, dY);
        // increment to next sprite image after delay
        if (animationDelay-- < 0) {
            // Cycles current iamge with boundary protection
            currentFrame = ++currentFrame % BMP_COLUMNS;
            // Arbitrary delay before cycling to next image
            animationDelay = 20;
        }
    }
    public void draw(Canvas canvas) {
        if (bitmap == null) {
            Paint paint = new Paint();
            // Set its color
            paint.setColor(color);
            // Draw circle
            canvas.drawCircle(centerX(), centerY(), width() / 2, paint);
        } else {
            // Calculate width of 1 image
            iconWidth = bitmap.getWidth() / BMP_COLUMNS;
            // Calculate height of 1 image
            iconHeight = bitmap.getHeight() / BMP_ROWS;
            // Set the x of source rectangle inside of bitmap based on current frame
            int srcX = currentFrame * iconWidth;
            // set the y to row of bitmap based on direction
            int srcY = getAnimatedRow() * iconHeight;
            // Defines the rectange inside of heroBmp to displayed
            Rect src = new Rect(srcX, srcY, srcX + iconWidth, srcY + iconHeight);
            // Draw an image
            canvas.drawBitmap(bitmap, src, this, null);
        }
    }

    private int getAnimatedRow() {
        // If magnitude of x is bigger than the magnitude of y
        if (Math.abs(dX) > Math.abs(dY)) {
            // If x is positive return row 2 for right
            if (Math.abs(dX) == dX) return RIGHT;
            // If x is negative return row 1 for left
            else return LEFT;
        // if y is postive return row 0 for up
        } else if (Math.abs(dY) == dY) return DOWN;
        // if y is postive return row 3 for up
        else return UP;
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

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void grow(int i) {
        right = right + i;
        bottom = bottom + i;
    }
}
