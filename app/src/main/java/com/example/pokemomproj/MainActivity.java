package com.example.pokemomproj;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private CharacterView characterView;
    private boolean isPaused = false;
    private BotView botView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            final WindowInsetsController insetsController = getWindow().getInsetsController();
            if (insetsController != null) {
                insetsController.hide(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
                insetsController.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
            }
        }
        characterView = findViewById(R.id.characterView);
        String characterName = getIntent().getStringExtra("characterName");
        if (characterName != null) {
            characterView.setCharacterName(characterName);
        }

        botView = findViewById(R.id.botView);
        botView.setCharacterView(characterView);
        mana_bar characterManaBar = findViewById(R.id.characterManaBar);
        characterView.setManaBar(characterManaBar);
        characterView.startManaRegeneration();

        mana_bar botManaBar = findViewById(R.id.botManaBar);
        botView.setManaBar(botManaBar);
        botView.startManaRegeneration();

        hp_bar botHpBar = findViewById(R.id.botHpBar);
        botView.setHpBar(botHpBar);

        hp_bar characterHpBar = findViewById(R.id.characterHpBar);
        characterView.setHpBar(characterHpBar);

        Button skillButton1 = findViewById(R.id.skillButton1);
        skillButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skill1 fireballSkill = skill1.createSkill(characterView);
                if(fireballSkill != null) {
                    fireballSkill.setBotView(botView);
                }
            }
        });

        Button btnHealSkill = findViewById(R.id.btnHealSkill);
        btnHealSkill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Skill2", "Button heal skill clicked!");
                characterView.useHealSkill();
            }
        });



        JoystickView joystickView = findViewById(R.id.joystickView);
        joystickView.setJoystickListener(new JoystickView.JoystickListener() {
            @Override
            public void onJoystickMoved(float xPercent, float yPercent) {
                characterView.move(xPercent, yPercent);
            }
        });
    }
}