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
    public AnimationDrawable animationDrawableUp;
    public AnimationDrawable animationDrawableDown;
    public AnimationDrawable animationDrawableLeft;
    public AnimationDrawable animationDrawableRight;
    public AnimationDrawable animationDrawableUpRight;
    public AnimationDrawable animationDrawableUpLeft;
    public AnimationDrawable animationDrawableDownRight;
    public AnimationDrawable animationDrawableDownLeft;
    private AnimationDrawable animationDrawable;
    private Runnable invalidateRunnable;
    private Handler handler;
    private Random random;
    private float xDirection;
    private float yDirection;
    private int duration;
    private int maxHp = 100;
    private int currentHp = 100;
    private hp_bar hpBar;
    private float botX;
    private float botY;


    private CharacterView characterView;


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


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 5000);
        invalidateRunnable = new Runnable() {
            @Override
            public void run() {

                if (random.nextFloat() < 0.5) { // 25% chance
                    skill1 fireballSkill = skill1.botUseSkill(BotView.this,characterView);
                    if (fireballSkill != null) {
                        fireballSkill.setCharacterView(characterView);
                    }
                }

                handler.postDelayed(this, random.nextInt(500) + 500); // Random delay between 0.5 to 3 seconds
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
        setTranslationZ(1);
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
        }else {
            imageX = newX;
        }

        if (newY < animationDrawable.getIntrinsicHeight() / 2 || newY > getHeight() - animationDrawable.getIntrinsicHeight() / 2) {
            xDirection = (random.nextFloat() - 0.5f) * 2; // Random value between -1 and 1
            yDirection = -yDirection;
        }else {
            imageY = newY;
        }
        botX = imageX;
        botY = imageY;
        invalidate();
    }



    public void setHpBar(hp_bar hpBar) {
        this.hpBar = hpBar;
    }

    public void reduceHp(int percentage) {
        currentHp = Math.max(currentHp - (maxHp * percentage / 100), 0);
        if (hpBar != null) {
            hpBar.setHp(currentHp);
        }
        if (currentHp == 0) {
            Log.d(TAG, "Bot HP is zero. Handling bot defeat.");
            // Handle bot defeat logic here
        }

    }
    public  float getCurrentHp(){
        return currentHp;
    }
    public boolean checkCollision(float x, float y, float width, float height) {
        float botLeft = imageX - animationDrawable.getIntrinsicWidth() / 2;
        float botRight = imageX + animationDrawable.getIntrinsicWidth() / 2;
        float botTop = imageY - animationDrawable.getIntrinsicHeight() / 2;
        float botBottom = imageY + animationDrawable.getIntrinsicHeight() / 2;

        float skillLeft = x-500;
        float skillRight = x-500 + width;
        float skillTop = y-100;
        float skillBottom = y-100 + height;
        Log.d("checkskill", "Fireball position: (" + x + ", " + y + "): "+imageX+" "+imageY);
        return !(skillLeft > botRight || skillRight < botLeft || skillTop > botBottom || skillBottom < botTop);
    }

    private int maxMana = 100;
    private int currentMana = 100;
    private mana_bar manaBar;

    // Add this method to CharacterView.java
    public void setManaBar(mana_bar manaBar) {
        this.manaBar = manaBar;
    }

    // Add this method to CharacterView.java
    public void reduceMana(int amount) {
        currentMana = Math.max(currentMana - amount, 0);

        if (manaBar != null) {
            manaBar.setMana(currentMana);
        }


        if (currentMana == 0) {
            Log.d(TAG, "Mana is depleted. Handling mana depletion.");
            // Handle mana depletion logic here
        }
    }

    // Add this method to CharacterView.java
    public void startManaRegeneration() {
        post(new Runnable() {
            @Override
            public void run() {
                currentMana = Math.min(currentMana + 20, maxMana);
                if (manaBar != null) {
                    manaBar.setMana(currentMana);
                }
                postDelayed(this, 1000); // Regenerate mana every second
            }
        });
    }
    public int getCurrentMana() {
        return currentMana;
    }

    public int getMaxMana() {
        return maxMana;
    }

    public AnimationDrawable getCurrentDrawable() {
        return animationDrawable;
    }


    public float getCharacterX() {
        return  imageX;
    }

    public float getCharacterY() {
        return  imageY;
    }
    public void setCharacterView(CharacterView characterView) {
        this.characterView = characterView;
    }
}