package com.example.pokemomproj;

import android.content.Context;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatImageView;

public class skill2 extends AppCompatImageView {
    private static final String TAG = "HealSkill";
    private CharacterView characterView;
    private int healAmount = 50;  // Lượng máu hồi phục
    private int manaCost = 65;    // Mana tiêu hao



    public skill2(Context context, CharacterView characterView) {
        super(context);
        this.characterView = characterView;
        initHealEffect();
    }

   private void initHealEffect() {
        setImageResource(R.drawable.heal); // Load heal.png
        setVisibility(View.INVISIBLE); // Initially invisible

        // Get character's X and Y coordinates
        float characterX = characterView.getCharacterX();
        float characterY = characterView.getCharacterY();

        // Get the width of the heal.png image
        int healImageWidth = getDrawable().getIntrinsicWidth();

        // Calculate the position below the character's feet
        float skillX = characterX - 190;
        float skillY = characterY - 72 - characterView.getHeight() / 2;

        // Set the position of the skill effect
        setX(skillX);
        setY(skillY);

        // Scale down the skill effect
        setScaleX(0.25f);
        setScaleY(0.25f);

       setVisibility(View.VISIBLE);
       postDelayed(() -> setVisibility(View.INVISIBLE), 250);
    }

    public void startHealing() {


        if (characterView.getCurrentMana() >= manaCost) {
            characterView.reduceMana(manaCost);
            characterView.healHp(healAmount);
            if (characterView.getParent() instanceof ViewGroup) {
                ViewGroup parent = (ViewGroup) characterView.getParent();
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                params.addRule(RelativeLayout.BELOW, characterView.getId());
                parent.addView(this, params);
            } else {
                Log.e(TAG, "characterView has no parent! Cannot add heal effect.");
            }

            Log.d(TAG, "Healing skill activated!");
            setVisibility(View.VISIBLE);

            postDelayed(() -> {
                setVisibility(View.INVISIBLE);
                if (getParent() instanceof ViewGroup) {
                    ((ViewGroup) getParent()).removeView(this);
                } else {
                    Log.e(TAG, "Heal effect has no parent! Cannot remove view.");
                }
            }, 1000);


        } else {
            Log.d(TAG, "Not enough mana to use Heal Skill!");
        }
    }
}