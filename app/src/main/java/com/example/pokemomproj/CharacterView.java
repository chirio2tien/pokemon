package com.example.pokemomproj;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.util.Objects;

public class CharacterView extends View {

    private static final String TAG = "CharacterView";
    private float characterX;
    private float characterY;
    private AnimationDrawable animationDrawableUp;
    private AnimationDrawable animationDrawableDown;
    private AnimationDrawable animationDrawableLeft;
    private AnimationDrawable animationDrawableRight;
    private AnimationDrawable animationDrawableUpRight;
    private AnimationDrawable animationDrawableUpLeft;
    private AnimationDrawable animationDrawableDownRight;
    private AnimationDrawable animationDrawableDownLeft;
    private AnimationDrawable currentDrawable;
    private AnimationDrawable previousDrawable;

    public CharacterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        characterX = 200;
        characterY = 200;

        initAnimations(context);
        startAnimation();
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

        currentDrawable = animationDrawableDown;
        previousDrawable = currentDrawable;
        currentDrawable.setOneShot(false);
        currentDrawable.start();
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
                if (currentDrawable != null) {
                    currentDrawable.start();
                    invalidate();
                    postDelayed(this, 1000 / 60); // 60 FPS
                }
            }
        });
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        if (currentDrawable != null) {
            canvas.save();
            canvas.translate(characterX - (float) currentDrawable.getIntrinsicWidth() / 2, characterY - (float) currentDrawable.getIntrinsicHeight() / 2);
            currentDrawable.setBounds(0, 0, currentDrawable.getIntrinsicWidth(), currentDrawable.getIntrinsicHeight());
            currentDrawable.draw(canvas);
            canvas.restore();
        } else {
            Log.e(TAG, "currentDrawable is null in onDraw");
        }
    }
    public void setPlayerPosition(float x, float y) {
        this.characterX = x;
        this.characterY = y;
        postInvalidate();
    }
    public void move(float xPercent, float yPercent) {
        float newX = characterX + xPercent * 10;
        float newY = characterY + yPercent * 10;

        if (Math.abs(xPercent) > Math.abs(yPercent)) {
            if (xPercent > 0) {
                if (yPercent > 0.4) {
                    currentDrawable = animationDrawableDownRight;
                } else if (yPercent < -0.4) {
                    currentDrawable = animationDrawableUpRight;
                } else {
                    currentDrawable = animationDrawableRight;
                }
            } else {
                if (yPercent > 0.4) {
                    currentDrawable = animationDrawableDownLeft;
                } else if (yPercent < -0.4) {
                    currentDrawable = animationDrawableUpLeft;
                } else {
                    currentDrawable = animationDrawableLeft;
                }
            }
        } else {
            if (yPercent > 0) {
                currentDrawable = animationDrawableDown;
            } else if (yPercent < 0) {
                currentDrawable = animationDrawableUp;
            }
        }

        // Ensure character does not move out of bounds
        if (currentDrawable != null) {
            if (newX >= (float) currentDrawable.getIntrinsicWidth() / 2 && newX <= getWidth() - (float) currentDrawable.getIntrinsicWidth() / 2) {
                characterX = newX;
            }
            if (newY >= (float) currentDrawable.getIntrinsicHeight() / 2 && newY <= getHeight() - (float) currentDrawable.getIntrinsicHeight() / 2) {
                characterY = newY;
            }

            // Only update the drawable if it has changed
            if (currentDrawable != previousDrawable) {
                previousDrawable = currentDrawable;
                currentDrawable.start();
                Log.d(TAG, "Animation started: " + currentDrawable);
            }

            postInvalidate();
        } else {
            Log.e(TAG, "currentDrawable is null in move");
        }
    }
    public Node getPlayerPosition() {
        return new Node((int) (characterX / 50), (int) (characterY / 50));
    }
}