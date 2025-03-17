package com.example.pokemomproj;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class CharacterSelectionActivity extends AppCompatActivity {

    private final int[] characterImageViewIds = {
            R.id.character1, R.id.character2, R.id.character3,
            R.id.character4, R.id.character5, R.id.character6
    };

    private final String[] characterNames = {
            "giratina", "gardervoid", "incineroar",
            "urshifu", "pikachu", "psyduck"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_character_selection);

        setupCharacterSelections();
    }

    private void setupCharacterSelections() {
        for (int i = 0; i < characterImageViewIds.length; i++) {
            final String characterName = characterNames[i];
            ImageView characterImage = findViewById(characterImageViewIds[i]);
            characterImage.setOnClickListener(v -> selectCharacter(characterName));
        }
    }

    private void selectCharacter(String characterName) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("characterName", characterName);
        startActivity(intent);
        finish();
    }
}