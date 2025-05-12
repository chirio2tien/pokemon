package com.example.pokemomproj;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    private boolean isMovable = true;

    private float characterX;
    private float characterY;
    public AnimationDrawable animationDrawableUp;
    public AnimationDrawable animationDrawableDown;
    public AnimationDrawable animationDrawableLeft;
    public AnimationDrawable animationDrawableRight;
    public AnimationDrawable animationDrawableUpRight;
    public AnimationDrawable animationDrawableUpLeft;
    public AnimationDrawable animationDrawableDownRight;
    public AnimationDrawable animationDrawableDownLeft;
    private AnimationDrawable currentDrawable;
    private AnimationDrawable previousDrawable;

    private int maxMana = 100;
    private int currentMana = 100;
    private mana_bar manaBar;
    private hp_bar hpBar;

    private int maxHp = 100;
    private int currentHp = 100;
    private String characterName = "gardervoid";

    private boolean isPaused = false;
    private boolean isDead = false;

    public CharacterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        characterX = 200;
        characterY = 200;

        initAnimations(context, characterName);
        startAnimation();
    }

    private void initAnimations(Context context, String characterName) {
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
                if (!isPaused && currentDrawable != null) {
                    if (!currentDrawable.isRunning()) {
                        currentDrawable.start();
                    }
                    invalidate();
                }
                postDelayed(this, 1000 / 60); // 60 FPS
            }
        });
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        if (isPaused) return;
        super.onDraw(canvas);
        if (currentDrawable != null) {
            canvas.save();
            float drawX = characterX - (float) currentDrawable.getIntrinsicWidth() / 2;
            float drawY = characterY - (float) currentDrawable.getIntrinsicHeight() / 2;
            canvas.translate(drawX, drawY);
            currentDrawable.setBounds(0, 0, currentDrawable.getIntrinsicWidth(), currentDrawable.getIntrinsicHeight());
            currentDrawable.draw(canvas);
            canvas.restore();
        } else {
            Log.e(TAG, "currentDrawable is null in onDraw");
        }
    }


        private boolean isMovementEnabled = true;

        public void setMovementEnabled(boolean enabled) {
            this.isMovementEnabled = enabled;
        }


        public void move(float xPercent, float yPercent) {
            if (!isMovementEnabled) return;
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




        public void setManaBar(mana_bar manaBar) {
            this.manaBar = manaBar;
        }

        public void reduceMana(int amount) {
            currentMana = Math.max(currentMana - amount, 0);
            if (manaBar != null) {
                manaBar.setMana(currentMana);

            if (newY >= (float) currentDrawable.getIntrinsicHeight() / 2 && newY <= getHeight() - (float) currentDrawable.getIntrinsicHeight() / 2) {
                characterY = newY;
            }

            if (currentDrawable != previousDrawable) {
                previousDrawable = currentDrawable;
                currentDrawable.start();
            }

            postInvalidate();
        } else {
            Log.e(TAG, "currentDrawable is null in move");
        }
    }

    public void pause() {
        isPaused = true;
        if (currentDrawable != null) {
            currentDrawable.stop();
        }
    }

    public void resume() {
        isPaused = false;
        if (currentDrawable != null && !currentDrawable.isRunning()) {
            currentDrawable.start();
        }
    }

    public void setManaBar(mana_bar manaBar) {
        this.manaBar = manaBar;
    }

    public void reduceMana(int amount) {
        currentMana = Math.max(currentMana - amount, 0);
        if (manaBar != null) {
            manaBar.setMana(currentMana);
        }
        if (currentMana == 0) {
            Log.d(TAG, "Mana is depleted.");
        }
    }

    public void startManaRegeneration() {
        post(new Runnable() {
            @Override
            public void run() {
                if (!isPaused) {
                    currentMana = Math.min(currentMana + 8, maxMana);
                    if (manaBar != null) {
                        manaBar.setMana(currentMana);
                    }
                }
                postDelayed(this, 1000);
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
        return currentDrawable;
    }

    public float getCharacterX() {
        return characterX;
    }

    public float getCharacterY() {
        return characterY;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
        initAnimations(getContext(), characterName);
        currentDrawable = animationDrawableDown;
        previousDrawable = currentDrawable;
        currentDrawable.start();
        invalidate();
        Log.d("CharacterView", "setCharacterName: " + characterName);
    }

    public void setHpBar(hp_bar hpBar) {
        this.hpBar = hpBar;
    }

    public void reduceHp(int percentage) {
        if (isDead) return;
        currentHp = Math.max(currentHp - (maxHp * percentage / 100), 0);
        if (hpBar != null) {
            hpBar.setHp(currentHp);
        }
        if (currentHp == 0 && !isDead) {
            isDead = true;
            SharedPreferences prefs = getContext().getSharedPreferences("GameData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();

            editor.putInt("currentHp", 0);  // Lưu trạng thái chết
            editor.putString("deadCharacter", characterName);

            editor.apply();
            Intent intent = new Intent(getContext(), CharacterSelectionActivity.class);
            getContext().startActivity(intent);
            if (getContext() instanceof MainActivity) {
                ((MainActivity) getContext()).finish();
            }
        }
    }

    public boolean checkCollision(float x, float y, float width, float height) {
        float Left = characterX - currentDrawable.getIntrinsicWidth() / 2;
        float Right = characterX + currentDrawable.getIntrinsicWidth() / 2;
        float Top = characterY - currentDrawable.getIntrinsicHeight() / 2;
        float Bottom = characterY + currentDrawable.getIntrinsicHeight() / 2;

        float skillLeft = x - 460;
        float skillRight = x - 460 + width;
        float skillTop = y - 130;
        float skillBottom = y - 130 + height;

        return !(skillLeft > Right || skillRight < Left || skillTop > Bottom || skillBottom < Top);
    }

    public void healHp(int amount) {
        currentHp += amount;
        if (currentHp > maxHp) {
            currentHp = maxHp;
        }
        hpBar.setHp(currentHp);
        Log.d("CharacterView", "Healed " + amount + " HP. Current HP: " + currentHp);
    }

    public void useHealSkill() {
        skill2 healSkill = new skill2(getContext(), null, CharacterView.this);
        healSkill.startHealing();
    }

    public Node getPlayerPosition() {
        return new Node((int) (characterX / 50), (int) (characterY / 50));
    }


        public String getCharacterName() {
        return characterName;
        }

        public int getCurrentHp() {
            return currentHp;
        }

    }

    public boolean isMovable() {
        return isMovable;
    }
}
