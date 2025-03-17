package com.example.pokemomproj;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieAnimationView;

public class skill2 extends LottieAnimationView {
    private static final String TAG = "HealSkill";
    private CharacterView characterView;
    private int healAmount = 50;  // Lượng máu hồi phục
    private int manaCost = 30;    // Mana tiêu hao
    private static boolean isOnCooldown = false; // Kiểm soát hồi chiêu
    private static final int COOLDOWN_TIME = 1000; // 18 giây (milliseconds)
    private Handler cooldownHandler = new Handler();

    public skill2(Context context, CharacterView characterView) {
        super(context);
        this.characterView = characterView;
        initHealEffect();
    }

    private void initHealEffect() {
        setAnimation(R.raw.heal_effect);
        // Gọi file JSON
        setRepeatCount(0); // Chạy 1 lần rồi dừng
    }

    public void startHealing() {
        if (isOnCooldown) {
            Log.d(TAG, "Skill is on cooldown! Please wait.");
            return;
        }

        if (characterView.getCurrentMana() >= manaCost) {
            characterView.reduceMana(manaCost);
            characterView.healHp(healAmount);
            if (characterView.getParent() instanceof ViewGroup) {
                ((ViewGroup) characterView.getParent()).addView(this);
            } else {
                Log.e(TAG, "characterView has no parent! Cannot add heal effect.");
            }

            Log.d(TAG, "Healing skill activated!");
            playAnimation();

            postDelayed(() -> {
                if (getParent() instanceof ViewGroup) {
                    ((ViewGroup) getParent()).removeView(this);
                } else {
                    Log.e(TAG, "Heal effect has no parent! Cannot remove view.");
                }
            }, 1000);

            // Bắt đầu hồi chiêu
            isOnCooldown = true;
            cooldownHandler.postDelayed(() -> {
                isOnCooldown = false;
                Log.d(TAG, "Skill is ready to use again!");
            }, COOLDOWN_TIME);
        } else {
            Log.d(TAG, "Not enough mana to use Heal Skill!");
        }
    }
}
