package com.cuijeb.animatedgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.transition.Explode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DrawView extends SurfaceView {
    SurfaceHolder surface;
    Paint paint = new Paint();
    Sprite sprite = new Sprite();
    Sprite foodSprite, badSprite;
    ArrayList<Explosion> explosions = new ArrayList<>();
    Bitmap explosionBMP = BitmapFactory.decodeResource(getResources(), R.drawable.explosion);
    Canvas canvas;
    boolean isRunning = true;
    int frames = 0;
    private static final int MAX_STREAMS=100;
    private int soundIdExplosion;
    private int soundIdBackground;
    private boolean soundPoolLoaded;
    private SoundPool soundPool;

    public DrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        surface = getHolder();
        new Thread(() -> {
            // Get current time to the nanosecond
            long lastTime = System.nanoTime();
            // Set the number of updates per second
            double amountOfTicks = 60;
            // This determines how many times we can divide 60 into 1e9 of nanoseconds or about 1 second
            double ns = 1000000000 / amountOfTicks;
            // Get the current time
            long timer = System.currentTimeMillis();
            // Set frame varaible
            int updates = 0;
            while(true) {
                // Get Current Time in nanoseconds during current loop
                long now = System.nanoTime();
                // if running fast
                if (now - lastTime < ns) {
                    try{
                        // Pause until time for next update
                        Thread.sleep((long)((ns - (now - lastTime))/1000000));
                    }catch(Exception e){}
                }
                // Set lastTime to current time to mark beginning of next loop
                lastTime = System.nanoTime();
                if (isRunning) {
                    if (!surface.getSurface().isValid()) continue;
                    // Lock canvas
                    canvas = surface.lockCanvas();
                    synchronized (getHolder()) {
                        update(canvas);
                        onDraw(canvas);
                    }
                    // unlocks the canvas
                    surface.unlockCanvasAndPost(canvas);
                }
                // Note that a frame has passed
                updates++;
                // if one second has passed
                if (System.currentTimeMillis() - timer > 1000) {
                    // Add a thousand to our timer for next time
                    timer += 1000;
                    // Print out how many frames have happened in the last second
                    System.out.println("UPS: " +updates +" FPS: "+frames);
                    // reset the update count for the next second
                    updates = 0;
                    // Reset the frame count for the next second
                    frames = 0;
                }
            }
        }).start();
        sprite.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bluejeans));
        sprite.grow(100);
        // Preload sounds
        initSoundPool();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        // getHeight() and getWidth now have the dimensions
        foodSprite = generateSprite();
        badSprite = generateSprite();
        badSprite.setColor(Color.GREEN);
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
        // Sprite draws itself
        sprite.draw(canvas);
        // food sprite draws itself
        foodSprite.draw(canvas);
        // the bad sprite draws itself
        badSprite.draw(canvas);
        // Draw each explosion
        for (Explosion e : explosions) {
            e.draw(canvas);
        }
        frames++;
    }
        public void update(Canvas canvas) {
            if (canvas == null) return;

        // updates sprite and foodSprite and badSprite
        sprite.update(canvas);
        foodSprite.update(canvas);
        badSprite.update(canvas);
        for (int i = explosions.size() - 1; i >= 0; i--) {
            explosions.get(i).update();
        }
        // Checks if they collided
        if (RectF.intersects(sprite, foodSprite)) {
            // On collision generate an explosion and add itself to list
            new Explosion(foodSprite, explosions, explosionBMP);
            playSoundExplosion();
            foodSprite = generateSprite();
            sprite.grow(10);
        }

        // Checks if the sprite collide with the bad Sprite
        if (RectF.intersects(sprite, badSprite)) {
            new Explosion(badSprite, explosions, explosionBMP);
            playSoundExplosion();
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

    // pause - resume
    public void pause(){
        isRunning = !isRunning;
    }

    private void initSoundPool()  {
        // With Android API >= 21.
        if (Build.VERSION.SDK_INT >= 21 ) {
            AudioAttributes audioAttrib = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            SoundPool.Builder builder= new SoundPool.Builder();
            builder.setAudioAttributes(audioAttrib).setMaxStreams(MAX_STREAMS);
            this.soundPool = builder.build();
        }
        // With Android API < 21
        else {
            // SoundPool(int maxStreams, int streamType, int srcQuality)
            this.soundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        }
        // When SoundPool load complete.
        this.soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                soundPoolLoaded = true;
                // Playing background sound.
                playSoundBackground();
            }
        });
        // Load the sound background.mp3 into SoundPool
        soundIdBackground= soundPool.load(this.getContext(), R.raw.background,1);
        // Load the sound explosion.wav into SoundPool
        soundIdExplosion = soundPool.load(this.getContext(), R.raw.explosion,1);
    }
    public void playSoundExplosion()  {
        if(soundPoolLoaded) {
            float leftVolumn = 0.8f;
            float rightVolumn =  0.8f;
            // Play sound explosion.wav
            int streamId = this.soundPool.play(this.soundIdExplosion,leftVolumn, rightVolumn, 1, 0, 1f);
        }
    }
    public void playSoundBackground()  {
        if(soundPoolLoaded) {
            float leftVolumn = 0.8f;
            float rightVolumn =  0.8f;
            // Play sound background.mp3
            int streamId = this.soundPool.play(this.soundIdBackground,leftVolumn, rightVolumn, 1, -1, 1f);
        }
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
