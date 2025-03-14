package com.example.pokemomproj;

        import android.content.Intent;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.ImageView;

        import androidx.appcompat.app.AppCompatActivity;

        public class CharacterSelectionActivity extends AppCompatActivity {

            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                getSupportActionBar().hide();
                setContentView(R.layout.activity_character_selection);

                ImageView character1 = findViewById(R.id.character1);
                ImageView character2 = findViewById(R.id.character2);

                character1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectCharacter("giratina");
                    }
                });

                character2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectCharacter("gardervoid");
                    }
                });
            }

            private void selectCharacter(String characterName) {
                Intent intent = new Intent();
                intent.putExtra("characterName", characterName);
                setResult(RESULT_OK, intent);
                finish();
            }
        }