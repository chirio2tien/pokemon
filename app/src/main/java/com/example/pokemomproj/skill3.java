
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

public class skill3 extends View {
    private static final String TAG = "earthquakeSkill";
    private float earthquakeX;
    private float earthquakeY;
    private float directionX;
    private float directionY;
    private int earthquakeWidth;
    private int earthquakeHeight;
    private AnimationDrawable earthquakeDrawable;
    private AnimatedImageDrawable animatedImageDrawable;
    private BotView botView;

    public skill3(Context context, float startX, float startY, float directionX, float directionY, int width, int height) {
        super(context);
        this.earthquakeX = startX;
        this.earthquakeY = startY;
        this.directionX = directionX;
        this.directionY = directionY;
        this.earthquakeWidth = width;
        this.earthquakeHeight = height;
        Log.d(TAG, "earthquakeSkill created at position: (" + startX + ", " + startY + ") with direction: (" + directionX + ", " + directionY + ")");

        initearthquakeAnimation(context);
        startAnimation();
    }

    public skill3(Context context) {
        super(context);
    }

    private void initearthquakeAnimation(Context context) {
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.skill1); // Replace with your GIF resource name
        if (drawable instanceof AnimatedImageDrawable) {
            animatedImageDrawable = (AnimatedImageDrawable) drawable;
            animatedImageDrawable.start();
        } else if (drawable instanceof AnimationDrawable) {
            earthquakeDrawable = (AnimationDrawable) drawable;
            earthquakeDrawable.start();
        } else {
            Log.e(TAG, "Drawable resource is not an AnimatedImageDrawable or AnimationDrawable");
        }
    }

    private void startAnimation() {
        post(new Runnable() {
            @Override
            public void run() {
                float nextX = earthquakeX + directionX * 10;
                float nextY = earthquakeY + directionY * 10;

                // Lấy kích thước màn hình (map)
                View parent = (View) getParent();
                if (parent != null) {
                    int screenWidth = parent.getWidth();
                    int screenHeight = parent.getHeight();

                    // Kiểm tra nếu viên đạn chạm biên trái/phải
                    if (nextX < 0 || nextX + earthquakeWidth > screenWidth) {
                        Log.d("Skill1", "earthquake hit the horizontal boundary!");
                        removeSelf(); // Xóa skill nếu chạm biên
                        return;
                    }

                    // Kiểm tra nếu viên đạn chạm biên trên/dưới
                    if (nextY < 0 || nextY + earthquakeHeight > screenHeight) {
                        Log.d("Skill1", "earthquake hit the vertical boundary!");
                        removeSelf(); // Xóa skill nếu chạm biên
                        return;
                    }
                }

                // Cập nhật vị trí viên đạn
                earthquakeX = nextX;
                earthquakeY = nextY;

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

            if (botView != null && botView.checkCollision(earthquakeX, earthquakeY, earthquakeWidth, earthquakeHeight)) {
                Log.d("skill1", "Collision detected with bot!");
                // Handle collision (e.g., reduce bot HP)
                botView.reduceHp(30);
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
            canvas.rotate(angle, earthquakeX, earthquakeY);

            int halfWidth = earthquakeWidth / 2;
            int halfHeight = earthquakeHeight / 2;
            int left = (int) (earthquakeX - halfWidth);
            int top = (int) (earthquakeY - halfHeight);
            int right = (int) (earthquakeX + earthquakeWidth / 2);
            int bottom = (int) (earthquakeY + earthquakeHeight / 2);

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
            Log.d("Skill1", "Creating earthquake skill");
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

            int earthquakeWidth = 100;
            int earthquakeHeight = 100;
            skill1 earthquakeSkill = new skill1(characterView.getContext(), startX, startY, directionX, directionY, earthquakeWidth, earthquakeHeight);
            ((ViewGroup) characterView.getParent()).addView(earthquakeSkill);

            characterView.reduceMana((int) (characterView.getMaxMana() * 0.2));
            return earthquakeSkill;
        }
        Log.d("Skill1", "Not enough mana to use Skill 1");
        return null;
    }
    public static skill1 botUseSkill(BotView botView) {git checkout -b
        if (botView.getCurrentMana() >= botView.getMaxMana() * 0.2) {
            Log.d("Skill1", "Bot using earthquake skill");
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

            int earthquakeWidth = 100;
            int earthquakeHeight = 100;
            skill1 earthquakeSkill = new skill1(botView.getContext(), startX, startY, directionX, directionY, earthquakeWidth, earthquakeHeight);
            ((ViewGroup) botView.getParent()).addView(earthquakeSkill);

            botView.reduceMana((int) (botView.getMaxMana() * 0.2));
            return earthquakeSkill;
        }
        Log.d("Skill1", "Not enough mana to use Skill 1");
        return null;
    }
}