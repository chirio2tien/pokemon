package com.example.pokemomproj;

        import android.content.Context;
        import android.content.Intent;
        import android.widget.ImageView;

        import java.util.Random;

        public class gift {
            private final int[] images = {
                R.drawable.g_Lum_Berry,
                R.drawable.g_Oran_Berry,
                R.drawable.g_Persim_Berry,
                R.drawable.g_Sitrus_Berry
            };

            private final Context context;

            public gift(Context context) {
                this.context = context;
            }

            public void setRandomImage(ImageView imageView) {
                Random random = new Random();
                int randomIndex = random.nextInt(images.length);
                imageView.setImageResource(images[randomIndex]);
            }

            public void navigateToCharacterSelection() {
                Intent intent = new Intent(context, CharacterSelectionActivity.class);
                context.startActivity(intent);
            }
        }