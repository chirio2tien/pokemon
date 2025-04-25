package com.example.pokemomproj;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;


import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class CharacterSelectionActivity extends AppCompatActivity {


    private  ArrayList<Integer> characterImageViewIds = new ArrayList<>(Arrays.asList(
            R.id.character1, R.id.character2, R.id.character3,
            R.id.character4, R.id.character5, R.id.character6
    ));

    
    private ArrayList<String> characterNames = new ArrayList<>(Arrays.asList(
            "giratina", "gardervoid", "incineroar",
            "urshifu", "pikachu", "psyduck"
    ));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_character_selection);

      
        setupCharacterSelections();
    }

    private void setupCharacterSelections() {
        for (int i = 0; i < characterImageViewIds.size(); i++) {
            String characterName = characterNames.get(i);
            ImageView characterImage = findViewById(characterImageViewIds.get(i));
            int id = i;
            characterImage.setOnClickListener(v -> selectCharacter(characterName, id));
        }

    }



    private void selectCharacter(String characterName, int index) {
        // Kiểm tra nếu index hợp lệ
        if (index >= 0 && index < characterImageViewIds.size()) {
            // Tìm ImageView tương ứng trong layout
            ImageView characterImage = findViewById(characterImageViewIds.get(index));

            // Lấy parent của ImageView để xóa nó khỏi layout
            ViewGroup parent = (ViewGroup) characterImage.getParent();
            if (parent != null) {
                parent.removeView(characterImage); // Xóa ImageView khỏi layout hoàn toàn
            }

            // Cập nhật lại danh sách sau khi xóa
            characterNames.remove(index);
            characterImageViewIds.remove(index);


        }

        // Chuyển Activity sau khi xóa
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("characterName", characterName);
        startActivity(intent);
        finish();
    }

}