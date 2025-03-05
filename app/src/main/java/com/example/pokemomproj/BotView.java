package com.example.pokemomproj;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.core.content.ContextCompat;

import java.util.Objects;
import java.util.Random;

public class BotView extends View {
    private static final String TAG = "BotView";
    private float imageX;
    private float imageY;
    private AnimationDrawable animationDrawableUp;
    private AnimationDrawable animationDrawableDown;
    private AnimationDrawable animationDrawableLeft;
    private AnimationDrawable animationDrawableRight;
    private AnimationDrawable animationDrawableUpRight;
    private AnimationDrawable animationDrawableUpLeft;
    private AnimationDrawable animationDrawableDownRight;
    private AnimationDrawable animationDrawableDownLeft;
    private AnimationDrawable animationDrawable;
    private Runnable invalidateRunnable;
    private Handler handler;
    private Random random;
    private float xDirection;
    private float yDirection;
    private int duration;
    private float botX;
    private float botY;
    private int maxHp = 100;
    private int currentHp = 100;
    private hp_bar hpBar;

    public BotView(Context context, AttributeSet attrs) {
        super(context, attrs);
        imageX = 1200;
        imageY = 600;
        random = new Random();
        handler = new Handler();

        initAnimations(context);
        startAnimation();
        setNewDirectionAndDuration();

        invalidateRunnable = new Runnable() {
            @Override
            public void run() {
                move(xDirection, yDirection);
                invalidate();
                handler.postDelayed(this, 1000 / 60); // 60 FPS
            }
        };
        handler.post(invalidateRunnable);
    }

    private void initAnimations(Context context) {
        String characterName = "giratina"; // Ensure this matches your drawable names

        animationDrawableUp = createAnimationDrawable(context, characterName, "u");
        animationDrawableDown = createAnimationDrawable(context, characterName, "d");
        animationDrawableLeft = createAnimationDrawable(context, characterName, "l");
        animationDrawableRight = createAnimationDrawable(context, characterName, "r");
        animationDrawableUpRight = createAnimationDrawable(context, characterName, "ur");
        animationDrawableUpLeft = createAnimationDrawable(context, characterName, "ul");
        animationDrawableDownRight = createAnimationDrawable(context, characterName, "dr");
        animationDrawableDownLeft = createAnimationDrawable(context, characterName, "dl");

        animationDrawable = animationDrawableDown;
        animationDrawable.setOneShot(false);
        animationDrawable.start();
    }

    private AnimationDrawable createAnimationDrawable(Context context, String characterName, String direction) {
        AnimationDrawable animationDrawable = new AnimationDrawable();
        for (int i = 0; i < 3; i++) {
            @SuppressLint("DiscouragedApi") int resId = context.getResources().getIdentifier(characterName + "_" + direction + "_" + i, "drawable", context.getPackageName());
            if (resId != 0) {
                animationDrawable.addFrame(Objects.requireNonNull(ContextCompat.getDrawable(context, resId)), 100);
            } else {
                Log.e(TAG, "Drawable resource not found: " + characterName + "_" + direction + "_" + i);
            }
        }
        animationDrawable.setOneShot(false);
        return animationDrawable;
    }

    private void startAnimation() {
        post(new Runnable() {
            @Override
            public void run() {
                if (animationDrawable != null) {
                    animationDrawable.start();
                    invalidate();
                    postDelayed(this, 1000 / 60); // 60 FPS
                }
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (animationDrawable != null) {
            canvas.save();
            canvas.translate(imageX - animationDrawable.getIntrinsicWidth() / 2, imageY - animationDrawable.getIntrinsicHeight() / 2);
            animationDrawable.setBounds(0, 0, animationDrawable.getIntrinsicWidth(), animationDrawable.getIntrinsicHeight());
            animationDrawable.draw(canvas);
            canvas.restore();
        }
    }

    private void setNewDirectionAndDuration() {
        xDirection = (random.nextFloat() - 0.5f) * 2; // Random value between -1 and 1
        yDirection = (random.nextFloat() - 0.5f) * 2; // Random value between -1 and 1
        duration = random.nextInt(3000) + 1000;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setNewDirectionAndDuration();
            }
        }, duration);
    }

    public void move(float xPercent, float yPercent) {
        float newX = imageX + xPercent * 12;
        float newY = imageY + yPercent * 12;

        if (Math.abs(xPercent) > Math.abs(yPercent)) {
            if (xPercent > 0) {
                if (yPercent > 0.4) {
                    animationDrawable = animationDrawableDownRight;
                } else if (yPercent < -0.4) {
                    animationDrawable = animationDrawableUpRight;
                } else {
                    animationDrawable = animationDrawableRight;
                }
            } else {
                if (yPercent > 0.4) {
                    animationDrawable = animationDrawableDownLeft;
                } else if (yPercent < -0.4) {
                    animationDrawable = animationDrawableUpLeft;
                } else {
                    animationDrawable = animationDrawableLeft;
                }
            }
        } else {
            if (yPercent > 0) {
                animationDrawable = animationDrawableDown;
            } else if (yPercent < 0) {
                animationDrawable = animationDrawableUp;
            }
        }

        if (newX < animationDrawable.getIntrinsicWidth() / 2 || newX > getWidth() - animationDrawable.getIntrinsicWidth() / 2) {
            xDirection = -xDirection;
            yDirection = (random.nextFloat() - 0.5f) * 2; // Random value between -1 and 1
        } else {
            imageX = newX;
        }

        if (newY < animationDrawable.getIntrinsicHeight() / 2 || newY > getHeight() - animationDrawable.getIntrinsicHeight() / 2) {
            xDirection = (random.nextFloat() - 0.5f) * 2; // Random value between -1 and 1
            yDirection = -yDirection;
        } else {
            imageY = newY;
        }

        invalidate();
    }

    public float getBotX() {
        return botX;
    }

    public float getBotY() {
        return botY;
    }

    public void setHpBar(hp_bar hpBar) {
        this.hpBar = hpBar;
    }

    public void reduceHp(int percentage) {
        currentHp = Math.max(currentHp - (maxHp * percentage / 100), 0);
        if (hpBar != null) {
            hpBar.setHp(currentHp);
        }
    }
}