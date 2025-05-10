package com.example.pokemomproj;

        import android.content.Context;
        import android.content.Intent;
        import android.widget.ImageView;

        import java.util.Random;

        public class gift {
            private final int[] images = {
                R.drawable.g_lum_berry,
                R.drawable.g_oran_berry,
                R.drawable.g_persim_berry,
                R.drawable.g_sitrus_berry
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