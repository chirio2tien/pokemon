package com.example.pokemomproj;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

public class pause extends Activity {
    private BotView botView;
    private CharacterView characterView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pause);

        // Khởi tạo các View cần thiết
// CharacterView id trong layout pause.xml

        ImageButton btnResume = findViewById(R.id.btnResume);
        btnResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Khi nhấn Resume
                saveGameState();  // Lưu trạng thái game khi resume
                characterView.resume(); // Tiếp tục game
                botView.resume(); // Tiếp tục Bot



                finish(); // Đóng activity Pause và quay lại game
            }
        });

        // Lưu trạng thái pause nếu cần
        SharedPreferences prefs = getSharedPreferences("GameData", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isPaused", true); // Lưu trạng thái pause
        editor.apply();
    }


    private void saveGameState() {
        SharedPreferences prefs = getSharedPreferences("GameData", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();


        editor.putInt("currentHp", botView.getCurrentHp());
        editor.putFloat("imageX", botView.getCharacterX());
        editor.putFloat("imageY", botView.getCharacterY());
        editor.putString("characterName", botView.getCharacterName()); // Giả sử bạn đã tạo getCharacterName() trong BotView

        editor.apply(); // Áp dụng lưu trữ
    }
}
