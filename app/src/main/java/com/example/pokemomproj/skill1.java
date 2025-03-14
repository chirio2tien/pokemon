
package com.example.pokemomproj;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.AnimatedImageDrawable;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;

public class skill1 extends View {
    private static final String TAG = "FireballSkill";
    private float fireballX;
    private float fireballY;
    private float directionX;
    private float directionY;
    private int fireballWidth;
    private int fireballHeight;
    private AnimationDrawable fireballDrawable;
    private AnimatedImageDrawable animatedImageDrawable;
    private BotView botView;

    public skill1(Context context, float startX, float startY, float directionX, float directionY, int width, int height) {
        super(context);
        this.fireballX = startX;
        this.fireballY = startY;
        this.directionX = directionX;
        this.directionY = directionY;
        this.fireballWidth = width;
        this.fireballHeight = height;
        Log.d(TAG, "FireballSkill created at position: (" + startX + ", " + startY + ") with direction: (" + directionX + ", " + directionY + ")");

        initFireballAnimation(context);
        startAnimation();
    }

    private void initFireballAnimation(Context context) {
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.skill1); // Replace with your GIF resource name
        if (drawable instanceof AnimatedImageDrawable) {
            animatedImageDrawable = (AnimatedImageDrawable) drawable;
            animatedImageDrawable.start();
        } else if (drawable instanceof AnimationDrawable) {
            fireballDrawable = (AnimationDrawable) drawable;
            fireballDrawable.start();
        } else {
            Log.e(TAG, "Drawable resource is not an AnimatedImageDrawable or AnimationDrawable");
        }
    }

    private void startAnimation() {
        post(new Runnable() {
            @Override
            public void run() {
                fireballX += directionX * 10;
                fireballY += directionY * 10;
                checkCollision();
                invalidate();
                postDelayed(this, 1000 / 60); // 60 FPS
            }
        });
    }

        private void checkCollision() {
            if (botView != null) {
                float botX = botView.getBotX();
                float botY = botView.getBotY();
                float botWidth = botView.getWidth();
                float botHeight = botView.getHeight();

                if (fireballX < botX + botWidth && fireballX + fireballWidth > botX &&
                        fireballY < botY + botHeight && fireballY + fireballHeight > botY) {
                    botView.reduceHp(20); // Reduce bot's HP by 20%
                    ((ViewGroup) getParent()).removeView(this); // Remove the fireball after collision
                }
            }
        }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (fireballDrawable != null) {
            float angle = (float) Math.toDegrees(Math.atan2(directionY, directionX));
            canvas.rotate(angle, fireballX, fireballY);
            canvas.save();
            canvas.translate(fireballX, fireballY); // Use fireballX and fireballY for position
            fireballDrawable.setBounds(0, 0, fireballWidth, fireballHeight);
            fireballDrawable.draw(canvas);
            canvas.restore();
        } else if (animatedImageDrawable != null) {
            float angle = (float) Math.toDegrees(Math.atan2(directionY, directionX));
            canvas.rotate(angle, fireballX, fireballY);
            canvas.save();
            canvas.translate(fireballX, fireballY); // Use fireballX and fireballY for position
            animatedImageDrawable.setBounds(0, 0, fireballWidth, fireballHeight);
            animatedImageDrawable.draw(canvas);
            canvas.restore();
        }
    }

    public void setBotView(BotView botView) {
        this.botView = botView;
    }
}