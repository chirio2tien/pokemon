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

public class skill5 extends View {
    private static final String TAG = "SolarBeamSkill";
    private float skillX;
    private float skillY;
    private float directionX;
    private float directionY;
    private int skillWidth;
    private int skillHeight;
    private AnimationDrawable animationDrawable;
    private AnimatedImageDrawable animatedImageDrawable;
    private BotView botView;
    private CharacterView characterView;

    public skill5(Context context, float startX, float startY, float directionX, float directionY, int width, int height) {
        super(context);
        this.skillX = startX;
        this.skillY = startY;
        this.directionX = directionX;
        this.directionY = directionY;
        this.skillWidth = width;
        this.skillHeight = height;

        Log.d(TAG, "Laze created at: (" + startX + ", " + startY + ") direction: (" + directionX + ", " + directionY + ")");
        initSkillAnimation(context);
        startAnimation();
    }

    private void initSkillAnimation(Context context) {
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.hasa);
        if (drawable instanceof AnimatedImageDrawable) {
            animatedImageDrawable = (AnimatedImageDrawable) drawable;
            animatedImageDrawable.start();
        } else if (drawable instanceof AnimationDrawable) {
            animationDrawable = (AnimationDrawable) drawable;
            animationDrawable.start();
        } else {
            Log.e(TAG, "Skill drawable is not animated.");
        }
    }

    private void startAnimation() {
        post(new Runnable() {
            @Override
            public void run() {
                float nextX = skillX + directionX * 10;
                float nextY = skillY + directionY * 10;

                View parent = (View) getParent();
                if (parent != null) {
                    int screenWidth = parent.getWidth();
                    int screenHeight = parent.getHeight();

                    if (nextX < 0 || nextX + skillWidth > screenWidth || nextY < 0 || nextY + skillHeight > screenHeight) {
                        Log.d(TAG, "Laze out of bounds!");
                        removeSelf();
                        return;
                    }
                }

                skillX = nextX;
                skillY = nextY;

                checkCollision();
                invalidate();
                postDelayed(this, 1000 / 60);
            }
        });
    }

    private void removeSelf() {
        ViewGroup parent = (ViewGroup) getParent();
        if (parent != null) {
            parent.removeView(this);
        }
    }

    private void checkCollision() {
        if (botView != null && botView.checkCollision(skillX, skillY, skillWidth, skillHeight)) {
            Log.d(TAG, "Hit bot with Laze!");
            applyDamageOverTime(botView);
            removeSelf();
        }

        if (characterView != null && characterView.checkCollision(skillX, skillY, skillWidth, skillHeight)) {
            Log.d(TAG, "Player hit by Laze!");
            applyDamageOverTime(characterView);
            removeSelf();
        }
    }

    private void applyDamageOverTime(final Object target) {
        final int damagePerTick = 8;
        final int ticks = 4;
        final int interval = 1000; // 1 giây

        for (int i = 0; i < ticks; i++) {
            final int delay = i * interval;
            postDelayed(() -> {
                if (target instanceof BotView) {
                    ((BotView) target).reduceHp(damagePerTick);
                } else if (target instanceof CharacterView) {
                    ((CharacterView) target).reduceHp(damagePerTick);
                }
            }, delay);
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        if (animatedImageDrawable != null) {
            float angle = (float) Math.toDegrees(Math.atan2(directionY, directionX));
            canvas.rotate(angle, skillX, skillY);

            int left = (int) (skillX - skillWidth / 2);
            int top = (int) (skillY - skillHeight / 2);
            int right = (int) (skillX + skillWidth / 2);
            int bottom = (int) (skillY + skillHeight / 2);

            animatedImageDrawable.setBounds(left, top, right, bottom);
            animatedImageDrawable.draw(canvas);
        }
        canvas.restore();
    }

    public void setBotView(BotView botView) {
        this.botView = botView;
    }

    public void setCharacterView(CharacterView characterView) {
        this.characterView = characterView;
    }

    public static skill5 createSkill(CharacterView characterView) {
        if (characterView.getCurrentMana() >= characterView.getMaxMana() * 0.3) {
            Log.d(TAG, "Casting Laze skill!");
            float dirX = 0;
            float dirY = 0;

            Drawable d = characterView.getCurrentDrawable();
            if (d == characterView.animationDrawableUp) dirY = -1;
            else if (d == characterView.animationDrawableDown) dirY = 1;
            else if (d == characterView.animationDrawableLeft) dirX = -1;
            else if (d == characterView.animationDrawableRight) dirX = 1;
            else if (d == characterView.animationDrawableUpRight) { dirX = 1; dirY = -1; }
            else if (d == characterView.animationDrawableUpLeft) { dirX = -1; dirY = -1; }
            else if (d == characterView.animationDrawableDownRight) { dirX = 1; dirY = 1; }
            else if (d == characterView.animationDrawableDownLeft) { dirX = -1; dirY = 1; }

            int[] location = new int[2];
            characterView.getLocationOnScreen(location);
            float startX = location[0] + characterView.getCharacterX() - 80;
            float startY = location[1] + characterView.getCharacterY();

            skill5 skill = new skill5(characterView.getContext(), startX, startY, dirX, dirY, 120, 120);
            ((ViewGroup) characterView.getParent()).addView(skill);

            characterView.reduceMana((int) (characterView.getMaxMana() * 0.3));
            return skill;
        }
        Log.d(TAG, "Not enough mana for Laze.");
        return null;
    }

    public static skill5 botUseSkill5(BotView botView, CharacterView characterView) {
        if (botView.getCurrentMana() >= botView.getMaxMana() * 0.3) {
            Log.d(TAG, "Bot casting Laze!");

            int[] botLoc = new int[2];
            botView.getLocationOnScreen(botLoc);
            float botX = botLoc[0] + botView.getCharacterX();
            float botY = botLoc[1] + botView.getCharacterY();

            int[] playerLoc = new int[2];
            characterView.getLocationOnScreen(playerLoc);
            float playerX = playerLoc[0] + characterView.getCharacterX();
            float playerY = playerLoc[1] + characterView.getCharacterY();

            float dirX = playerX - botX;
            float dirY = playerY - botY;
            float length = (float) Math.sqrt(dirX * dirX + dirY * dirY);
            if (length != 0) {
                dirX /= length;
                dirY /= length;
            }

            skill5 skill = new skill5(botView.getContext(), botX - 80, botY, dirX, dirY, 120, 120);
            ((ViewGroup) botView.getParent()).addView(skill);
            botView.reduceMana((int) (botView.getMaxMana() * 0.3));
            return skill;
        }
        Log.d(TAG, "Bot not enough mana for Laze.");
        return null;
    }
}
