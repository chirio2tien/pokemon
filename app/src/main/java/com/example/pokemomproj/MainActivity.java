package com.example.pokemomproj;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private CharacterView characterView;
    private BotView botView;
    private int currentHp = 100;
    private int currentMana = 100;
    private int botcurrentHp = 100;
    private int botcurrentMana = 100;
    private float imageX = 1200;

    private float imageY= 600;
    private hp_bar hpBar;
     private String Name;
    private String characterName = null; // Biến để lưu tên nhân vật


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        characterView = findViewById(R.id.characterView);
        characterName = getIntent().getStringExtra("characterName");

        if (characterName == null) {
            // Nếu không có dữ liệu mới, lấy từ SharedPreferences
            SharedPreferences prefs = getSharedPreferences("GameData", MODE_PRIVATE);
            characterName = prefs.getString("characterName", null);
        }

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
        skillButton1.setOnClickListener(v -> {
            skill1 fireballSkill = skill1.createSkill(characterView);
            if (fireballSkill != null) {
                fireballSkill.setBotView(botView);
            }
        });

        Button btnHealSkill = findViewById(R.id.btnHealSkill);
        btnHealSkill.setOnClickListener(v -> {
            Log.d("Skill2", "Button heal skill clicked!");
            characterView.useHealSkill();
        });

        Button btnEarthQuakeSkill2 = findViewById(R.id.btnEarthQuakeSkill2);
        btnEarthQuakeSkill2.setOnClickListener(v -> {
            skill4 earthquakeSkill = new skill4(MainActivity.this, characterView);
            ((ViewGroup) characterView.getParent()).addView(earthquakeSkill);
            earthquakeSkill.setBotView(botView);
            earthquakeSkill.startDamage();
        });

        JoystickView joystickView = findViewById(R.id.joystickView);
        joystickView.setJoystickListener((xPercent, yPercent) -> {
            if (!isPaused) {
                characterView.move(xPercent, yPercent);
            }
        });

        // Pause & Resume


//        btnPause.setOnClickListener(v -> {
//            isPaused = true;
//            characterView.setMovementEnabled(false);
//            botView.setMovementEnabled(false);
//            characterView.stopManaRegeneration();
//            botView.stopManaRegeneration();
//
//            btnPause.setVisibility(View.GONE);
//            btnResume.setVisibility(View.VISIBLE);
//        });
//
//        btnResume.setOnClickListener(v -> {
//            isPaused = false;
//            characterView.setMovementEnabled(true);
//            botView.setMovementEnabled(true);
//            characterView.startManaRegeneration();
//            botView.startManaRegeneration();
//
//            btnPause.setVisibility(View.VISIBLE);
//            btnResume.setVisibility(View.GONE);
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = getSharedPreferences("GameData", MODE_PRIVATE);
        currentHp = prefs.getInt("currentHp", 100);
        imageX = prefs.getFloat("imageX", 1200);
        imageY = prefs.getFloat("imageY", 600);


        if (characterName == null) {
            characterName = prefs.getString("characterName", null);
        }

        // Trì hoãn việc set nhân vật để đảm bảo View đã vẽ xong
        new Handler().postDelayed(() -> {
            if (characterName != null && characterView != null) {
                Log.d("DEBUG", "Resume: Set character name = " + characterName);
                characterView.setCharacterName(characterName);
            }
            botView.setCharacterX(imageX);
            botView.setCharacterY(imageY);
        }, 200); // trì hoãn 200ms là đủ an toàn trong hầu hết các trường hợp

    }


    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences prefs = getSharedPreferences("GameData", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("currentHp", currentHp);
        editor.putFloat("imageX", botView.getCharacterX());
        editor.putFloat("imageY", botView.getCharacterY());
        editor.putString("characterName", characterName);
        editor.apply();
    }
}
