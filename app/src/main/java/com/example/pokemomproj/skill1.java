
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
                float nextX = fireballX + directionX * 10;
                float nextY = fireballY + directionY * 10;

                // Lấy kích thước màn hình (map)
                View parent = (View) getParent();
                if (parent != null) {
                    int screenWidth = parent.getWidth();
                    int screenHeight = parent.getHeight();

                    // Kiểm tra nếu viên đạn chạm biên trái/phải
                    if (nextX < 0 || nextX + fireballWidth > screenWidth) {
                        Log.d("Skill1", "Fireball hit the horizontal boundary!");
                        removeSelf(); // Xóa skill nếu chạm biên
                        return;
                    }

                    // Kiểm tra nếu viên đạn chạm biên trên/dưới
                    if (nextY < 0 || nextY + fireballHeight > screenHeight) {
                        Log.d("Skill1", "Fireball hit the vertical boundary!");
                        removeSelf(); // Xóa skill nếu chạm biên
                        return;
                    }
                }

                // Cập nhật vị trí viên đạn
                fireballX = nextX;
                fireballY = nextY;

                checkCollision();
                invalidate();
                postDelayed(this, 1000 / 60); // 60 FPS
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
        if (botView != null) {

            if (botView != null && botView.checkCollision(fireballX, fireballY, fireballWidth, fireballHeight)) {
                Log.d("skill1", "Collision detected with bot!");
                // Handle collision (e.g., reduce bot HP)
                botView.reduceHp(20);
                // Optionally, remove the skill view after collision
                if (getParent() != null) {
                    ((ViewGroup) getParent()).removeView(this);
                }

            }


        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        if (animatedImageDrawable != null) {
            float angle = (float) Math.toDegrees(Math.atan2(directionY, directionX));
            canvas.rotate(angle, fireballX, fireballY);

            int halfWidth = fireballWidth / 2;
            int halfHeight = fireballHeight / 2;
            int left = (int) (fireballX - halfWidth);
            int top = (int) (fireballY - halfHeight);
            int right = (int) (fireballX + fireballWidth / 2);
            int bottom = (int) (fireballY + fireballHeight / 2);

            animatedImageDrawable.setBounds(left, top, right, bottom);
            animatedImageDrawable.draw(canvas);
        }

        canvas.restore();

    }

    public void setBotView(BotView botView) {
        this.botView = botView;
    }
    public static skill1 createSkill(CharacterView characterView) {
        if (characterView.getCurrentMana() >= characterView.getMaxMana() * 0.2) {
            Log.d("Skill1", "Creating fireball skill");
            float directionX = 0;
            float directionY = 0;

            if (characterView.getCurrentDrawable() == characterView.animationDrawableUp) {
                directionY = -1;
            } else if (characterView.getCurrentDrawable() == characterView.animationDrawableDown) {
                directionY = 1;
            } else if (characterView.getCurrentDrawable() == characterView.animationDrawableLeft) {
                directionX = -1;
            } else if (characterView.getCurrentDrawable() == characterView.animationDrawableRight) {
                directionX = 1;
            } else if (characterView.getCurrentDrawable() == characterView.animationDrawableUpRight) {
                directionX = 1;
                directionY = -1;
            } else if (characterView.getCurrentDrawable() == characterView.animationDrawableUpLeft) {
                directionX = -1;
                directionY = -1;
            } else if (characterView.getCurrentDrawable() == characterView.animationDrawableDownRight) {
                directionX = 1;
                directionY = 1;
            } else if (characterView.getCurrentDrawable() == characterView.animationDrawableDownLeft) {
                directionX = -1;
                directionY = 1;
            }

            int[] location = new int[2];
            characterView.getLocationOnScreen(location);
            float startX = location[0] + characterView.getCharacterX() - 80;
            float startY = location[1] + characterView.getCharacterY();

            int fireballWidth = 100;
            int fireballHeight = 100;
            skill1 fireballSkill = new skill1(characterView.getContext(), startX, startY, directionX, directionY, fireballWidth, fireballHeight);
            ((ViewGroup) characterView.getParent()).addView(fireballSkill);

            characterView.reduceMana((int) (characterView.getMaxMana() * 0.2));
            return fireballSkill;
        }
        Log.d("Skill1", "Not enough mana to use Skill 1");
        return null;
    }
    public static skill1 botUseSkill(BotView botView) {
        if (botView.getCurrentMana() >= botView.getMaxMana() * 0.2) {
            Log.d("Skill1", "Bot using fireball skill");
            float directionX = 0;
            float directionY = 0;

            if (botView.getCurrentDrawable() == botView.animationDrawableUp) {
                directionY = -1;
            } else if (botView.getCurrentDrawable() == botView.animationDrawableDown) {
                directionY = 1;
            } else if (botView.getCurrentDrawable() == botView.animationDrawableLeft) {
                directionX = -1;
            } else if (botView.getCurrentDrawable() == botView.animationDrawableRight) {
                directionX = 1;
            } else if (botView.getCurrentDrawable() == botView.animationDrawableUpRight) {
                directionX = 1;
                directionY = -1;
            } else if (botView.getCurrentDrawable() == botView.animationDrawableUpLeft) {
                directionX = -1;
                directionY = -1;
            } else if (botView.getCurrentDrawable() == botView.animationDrawableDownRight) {
                directionX = 1;
                directionY = 1;
            } else if (botView.getCurrentDrawable() == botView.animationDrawableDownLeft) {
                directionX = -1;
                directionY = 1;
            }

            int[] location = new int[2];
            botView.getLocationOnScreen(location);
            float startX = location[0] + botView.getCharacterX() - 80;
            float startY = location[1] + botView.getCharacterY();

            int fireballWidth = 100;
            int fireballHeight = 100;
            skill1 fireballSkill = new skill1(botView.getContext(), startX, startY, directionX, directionY, fireballWidth, fireballHeight);
            ((ViewGroup) botView.getParent()).addView(fireballSkill);

            botView.reduceMana((int) (botView.getMaxMana() * 0.2));
            return fireballSkill;
        }
        Log.d("Skill1", "Not enough mana to use Skill 1");
        return null;
    }
}