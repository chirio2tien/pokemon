package com.example.pokemomproj;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.ImageButton;

import java.lang.reflect.Type;
import androidx.appcompat.app.AlertDialog;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

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
    private String characterName = null;
    private boolean isPaused = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
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

        Button btnPause = findViewById(R.id.btnPause);
        btnPause.setOnClickListener(v -> {
            pauseGame();
            showItemDialog();
        });


        Button btnHealSkill = findViewById(R.id.btnHealSkill);
        btnHealSkill.setOnClickListener(v -> {
            Log.d("Skill2", "Button heal skill clicked!");
            characterView.useHealSkill();
        });

        Button btnEarthQuakeSkill = findViewById(R.id.btnEarthQuakeSkill);
        btnEarthQuakeSkill.setOnClickListener(v -> {
            skill4 earthquakeSkill = new skill4(MainActivity.this, characterView);
            ((ViewGroup) characterView.getParent()).addView(earthquakeSkill);
            earthquakeSkill.setBotView(botView);
            earthquakeSkill.startDamage();
        });

        Button skillButton3 = findViewById(R.id.btnBeam);
        skillButton3.setOnClickListener(v -> {
            skill3 fireballSkill = skill3.createSkill(characterView);
            if (fireballSkill != null) {
                fireballSkill.setBotView(botView);
            }
        });

        Button skillButton5 = findViewById(R.id.btnSolarBeam);
        skillButton5.setOnClickListener(v -> {
            skill5 fireballSkill = skill5.createSkill(characterView);
            if (fireballSkill != null) {
                fireballSkill.setBotView(botView);
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
    private void pauseGame() {
        characterView.setMovementEnabled(false);
        botView.setMovementEnabled(false);
        botView.setPaused(true);
    }
    private void showItemDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.pause, null);
        RecyclerView recyclerView = dialogView.findViewById(R.id.recyclerViewPopupItem);


        recyclerView.setLayoutManager(new GridLayoutManager(this, 3)); //  3 cột

        SharedPreferences pref = getSharedPreferences("dsitem_pref", MODE_PRIVATE);
        String json = pref.getString("dsitem_data", null);
        Gson gson = new Gson();
        ArrayList<ImageItem.item> dsHinhitem;

        if (json != null) {
            Type type = new TypeToken<ArrayList<ImageItem.item>>() {}.getType();
            dsHinhitem = gson.fromJson(json, type);
        } else {
            dsHinhitem = new ArrayList<>();
        }

        ArrayList<ImageItem.item> usableItems = new ArrayList<>();
        for (ImageItem.item item : dsHinhitem) {
            if (item.getSoLuong() > 0) {
                usableItems.add(item);
            }
        }

        ImageItem adapter = new ImageItem(this, usableItems);
        recyclerView.setAdapter(adapter);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(true)
                .create();

        dialog.setOnDismissListener(d -> resumeGame());


        //  Xử lý chọn item
        adapter.setOnItemClickListener(position -> {
            ImageItem.item selected = usableItems.get(position);
            applyItemEffect(selected.gettenitem());
            selected.setSoLuong(selected.getSoLuong() - 1);

            String newJson = gson.toJson(dsHinhitem);
            pref.edit().putString("dsitem_data", newJson).apply();

            dialog.dismiss();

        });

        dialog.show();
    }

    private void applyItemEffect(String itemName) {
        switch (itemName) {
            case "g_oran_berry":
                characterView.healHp(30); // ví dụ hồi 30 HP
                break;
            case "g_leppa_berry":
                characterView.reduceMana(-20); // tăng mana 20
                break;

        }
    }
    private void resumeGame() {
        characterView.setMovementEnabled(true);
        botView.setMovementEnabled(true);
        characterView.startManaRegeneration();
        botView.startManaRegeneration();
        botView.setPaused(false);
    }



    @Override
    protected void onResume() {
        super.onResume();
        resumeGame();

        SharedPreferences prefs = getSharedPreferences("GameData", MODE_PRIVATE);
        currentHp = prefs.getInt("currentHp", 100);
        imageX = prefs.getFloat("imageX", 1200);
        imageY = prefs.getFloat("imageY", 600);
        int hpbot = prefs.getInt("hpbot", 100);


        if (characterName == null) {
            characterName = prefs.getString("characterName", null);
        }

        // Trì hoãn việc set nhân vật để đảm bảo View đã vẽ xong

            if (characterName != null && characterView != null) {

                Log.d("DEBUG", "Resume: Set character name = " + characterName);
                characterView.setCharacterName(characterName);
            }
            botView.setCurrentHp(hpbot);
            botView.setCharacterX(imageX);
            botView.setCharacterY(imageY);



    }


    @Override
    protected void onPause() {
        super.onPause();
        pauseGame();
        SharedPreferences prefs = getSharedPreferences("GameData", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("currentHp", currentHp);
        editor.putInt("hpbot", botView.getCurrentHp());
        editor.putFloat("imageX", botView.getCharacterX());
        editor.putFloat("imageY", botView.getCharacterY());
        editor.putString("characterName", characterName);
        editor.apply();
    }
}
