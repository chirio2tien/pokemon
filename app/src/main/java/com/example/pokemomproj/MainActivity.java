package com.example.pokemomproj;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private CharacterView characterView;

    private BotView botView;
    private int currentHp = 100; // Khởi tạo giá trị HP mặc định
    private int currentMana = 100;
    private int botcurrentHp = 100; // Khởi tạo giá trị HP mặc định
    private int botcurrentMana = 100; // Khởi tạo giá trị Mana mặc định
    private float imageX = 1200;
    private float imageY= 600;
    private hp_bar hpBar;

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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 3000); // Delay 3 giây

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


        // Thiết lập HP về 0
         // Thời gian chờ tùy chọn (2 giây)

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



        Button btnEarthQuakeSkill2 = findViewById(R.id.btnEarthQuakeSkill2);
        btnEarthQuakeSkill2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skill4 earthquakeSkill = new skill4(MainActivity.this, characterView);
                ((ViewGroup) characterView.getParent()).addView(earthquakeSkill);
                earthquakeSkill.setBotView(botView);
                earthquakeSkill.startDamage();
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
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = getSharedPreferences("GameData", MODE_PRIVATE);
        currentHp = prefs.getInt("currentHp", 100);
        imageX = prefs.getFloat("imageX", 1200);
        imageY = prefs.getFloat("imageY", 600);
        botView.setCharacterX(imageX);
        botView.setCharacterY(imageY);

    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences prefs = getSharedPreferences("GameData", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("currentHp", currentHp);
        editor.putFloat("imageX", botView.getCharacterX());
        editor.putFloat("imageY", botView.getCharacterY());
        editor.apply();
    }

}