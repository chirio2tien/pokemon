package com.example.pokemomproj;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.graphics.Paint;
import android.graphics.Color;


import androidx.core.content.ContextCompat;

import java.util.Objects;
import java.util.Random;

// phần đầu giữ nguyên...

public class BotView extends View {
    private static final String TAG = "BotView";

    private float imageX = 1200;
    private float imageY = 600;
    private float xDirection, yDirection;
    private int duration;

    private AnimationDrawable animationDrawable;
    public AnimationDrawable animationDrawableUp, animationDrawableDown, animationDrawableLeft, animationDrawableRight;
    public AnimationDrawable animationDrawableUpRight, animationDrawableUpLeft, animationDrawableDownRight, animationDrawableDownLeft;

    private final Handler handler = new Handler();
    private final Random random = new Random();
    private String characterName;
    private final String[] characterNames = {
            "giratina", "gardervoid", "incineroar", "urshifu", "pikachu", "psyduck"
    };

    private int maxHp = 100;
    private int currentHp = 100;
    private int maxMana = 100;
    private int currentMana = 100;
    private boolean hasGivenGift = false;
    private boolean isPaused = false;

    private hp_bar hpBar;
    private mana_bar manaBar;
    private CharacterView characterView;
    private int deathCount = 0;


    public BotView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAnimations(context);
        startAnimation();
        setNewDirectionAndDuration();
        loadStats(); // Load stats từ file khi bot được tạo

        handler.post(frameUpdateRunnable);
        handler.post(logicRunnable);
    }

    private void initAnimations(Context context) {
        int index = random.nextInt(characterNames.length);
        characterName = characterNames[index];

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
        AnimationDrawable anim = new AnimationDrawable();
        for (int i = 0; i < 3; i++) {
            @SuppressLint("DiscouragedApi")
            int resId = context.getResources().getIdentifier(characterName + "_" + direction + "_" + i, "drawable", context.getPackageName());
            if (resId != 0) {
                anim.addFrame(Objects.requireNonNull(ContextCompat.getDrawable(context, resId)), 100);
            }
        }
        anim.setOneShot(false);
        return anim;
    }

    private final Runnable frameUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            if (!isPaused ) {
                move(xDirection, yDirection);
                invalidate();
            }
            handler.postDelayed(this, 1000 / 60); // 60 FPS
        }
    };

    private final Runnable logicRunnable = new Runnable() {
        @Override
        public void run() {

            if (isPaused  || characterView == null || characterView.getCurrentHp() <= 0) {
                handler.postDelayed(this, 1000); // kiểm tra lại sau 1s nếu bị pause
                return;
            }

            if (random.nextFloat() < 0.5) {
                skill1 fireball = skill1.botUseSkill1(BotView.this, characterView);
                if (fireball != null) {
                    fireball.setCharacterView(characterView);
                    Log.d(TAG, "Bot used fireball skill.");

                }


                if (random.nextFloat() < 0.25) {
                    skill3 beam = skill3.botUseSkill3(BotView.this, characterView);
                    if (beam != null) {
                        beam.setCharacterView(characterView);
                        Log.d(TAG, "Bot used beam skill.");
                    }
                }

            if (random.nextFloat() < 0.25 && getCurrentMana() >= 65 && currentHp < maxHp) {
                skill2 healing = new skill2(getContext(), BotView.this, null);
                healing.BotstartHealing(BotView.this);
            }


             
                if (random.nextFloat() < 0.25 && getCurrentMana() >= 80) {
                    skill5 s5 = skill5.botUseSkill5(BotView.this, characterView);
                    if (s5 != null) {
                        s5.setCharacterView(characterView);
                        Log.d(TAG, "Bot used skill5.");
                    }

                }
            }
            handler.postDelayed(this, random.nextInt(500) + 1500);
        }

    };

    private void setNewDirectionAndDuration() {
        xDirection = (random.nextFloat() - 0.5f) * 2;
        yDirection = (random.nextFloat() - 0.5f) * 2;
        duration = random.nextInt(3000) + 1000;

        handler.postDelayed(this::setNewDirectionAndDuration, duration);
    }

    private void startAnimation() {
        post(new Runnable() {
            @Override
            public void run() {
                if (!isPaused && animationDrawable != null) {
                    animationDrawable.start();
                    invalidate();
                }
                postDelayed(this, 1000 / 60);
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(50);
        canvas.drawText("da giet: " + deathCount, 50, 100, textPaint);

        if (animationDrawable != null) {
            canvas.save();
            canvas.translate(imageX - animationDrawable.getIntrinsicWidth() / 2, imageY - animationDrawable.getIntrinsicHeight() / 2);
            animationDrawable.setBounds(0, 0, animationDrawable.getIntrinsicWidth(), animationDrawable.getIntrinsicHeight());
            animationDrawable.draw(canvas);
            canvas.restore();
        }
    }

    private boolean isMovementEnabled = true;

    public void setMovementEnabled(boolean enabled) {
        this.isMovementEnabled = enabled;
    }




    public void move(float xPercent, float yPercent) {
        // Trong logic di chuyển của bot:
        if (!isMovementEnabled) return;

        float newX = imageX + xPercent * 12;
        float newY = imageY + yPercent * 12;

        if (Math.abs(xPercent) > Math.abs(yPercent)) {
            if (xPercent > 0) {
                animationDrawable = (yPercent > 0.4) ? animationDrawableDownRight :
                        (yPercent < -0.4) ? animationDrawableUpRight : animationDrawableRight;
            } else {
                animationDrawable = (yPercent > 0.4) ? animationDrawableDownLeft :
                        (yPercent < -0.4) ? animationDrawableUpLeft : animationDrawableLeft;
            }
        } else {
            animationDrawable = (yPercent > 0) ? animationDrawableDown : animationDrawableUp;
        }

        if (newX < animationDrawable.getIntrinsicWidth() / 2 || newX > getWidth() - animationDrawable.getIntrinsicWidth() / 2) {
            xDirection = -xDirection;
        } else {
            imageX = newX;
        }

        if (newY < animationDrawable.getIntrinsicHeight() / 2 || newY > getHeight() - animationDrawable.getIntrinsicHeight() / 2) {
            yDirection = -yDirection;
        } else {
            imageY = newY;
        }
    }

    public void reduceHp(int percentage) {
        currentHp = Math.max(currentHp - (maxHp * percentage / 100), 0);
        if (hpBar != null) hpBar.setHp(currentHp);
        if (currentHp == 0 && !hasGivenGift) {
            deathCount++;
            saveStats();
            giveGift();
        }
    }




   


    public boolean checkCollision(float x, float y, float width, float height) {
        float botLeft = imageX - animationDrawable.getIntrinsicWidth() / 2;
        float botRight = imageX + animationDrawable.getIntrinsicWidth() / 2;
        float botTop = imageY - animationDrawable.getIntrinsicHeight() / 2;
        float botBottom = imageY + animationDrawable.getIntrinsicHeight() / 2;

        float skillLeft = x - 500;
        float skillRight = skillLeft + width;
        float skillTop = y - 100;
        float skillBottom = skillTop + height;

        return !(skillLeft > botRight || skillRight < botLeft || skillTop > botBottom || skillBottom < botTop);
    }

    public void healHp(int amount) {
        currentHp = Math.min(currentHp + amount, maxHp);
        if (hpBar != null) hpBar.setHp(currentHp);
    }

    public void reduceMana(int amount) {
        currentMana = Math.max(currentMana - amount, 0);
        if (manaBar != null) manaBar.setMana(currentMana);
    }

    public void startManaRegeneration() {
        post(new Runnable() {
            @Override
            public void run() {
                if (!isPaused) {
                    currentMana = Math.min(currentMana + 20, maxMana);
                    if (manaBar != null) manaBar.setMana(currentMana);
                }
                postDelayed(this, 1000);
            }
        });
    }

    // Getters/Setters
    public int getCurrentMana() { return currentMana; }
    public int getMaxMana() { return maxMana; }
    public int getCurrentHp() { return currentHp; }
    public void setCurrentHp(int currentHp) { this.currentHp = currentHp; }
    public void setHpBar(hp_bar hpBar) { this.hpBar = hpBar; }
    public void setManaBar(mana_bar manaBar) { this.manaBar = manaBar; }
    public void setCharacterView(CharacterView characterView) { this.characterView = characterView; }
    public float getCharacterX() { return imageX; }
    public float getCharacterY() { return imageY; }
    public void setCharacterX(float x) { this.imageX = x; }
    public void setCharacterY(float y) { this.imageY = y; }

    public String getCharacterName() { return characterName; }




    private void giveGift() {
        hasGivenGift = true;

        // Tăng 10% chỉ số và lưu lại
        maxHp = (int)(maxHp * 1.1);
        maxMana = (int)(maxMana * 1.1);
        saveStats();

        SharedPreferences prefs = getContext().getSharedPreferences("gifts", Context.MODE_PRIVATE);
        int current = prefs.getInt("item_fireball", 0);
        prefs.edit().putInt("item_fireball", current + 1).apply();

        Intent intent = new Intent(getContext(), gift.class);
        getContext().startActivity(intent);
        Log.d(TAG, "Bot defeated, gift given and stats increased.");
    }

    private void saveStats() {
        SharedPreferences prefs = getContext().getSharedPreferences("bot_stats", Context.MODE_PRIVATE);
        prefs.edit()
                .putInt("maxHp", maxHp)
                .putInt("maxMana", maxMana)
                .putInt("deathCount", deathCount)
                .apply();

    }

    private void loadStats() {
        SharedPreferences prefs = getContext().getSharedPreferences("bot_stats", Context.MODE_PRIVATE);
        maxHp = prefs.getInt("maxHp", 100);
        maxMana = prefs.getInt("maxMana", 100);
        deathCount = prefs.getInt("deathCount", 0);

        currentHp = maxHp;
        currentMana = maxMana;
    }



    private boolean isPaused = false;

    public void setPaused(boolean paused) {
        this.isPaused = paused;
    }


}
