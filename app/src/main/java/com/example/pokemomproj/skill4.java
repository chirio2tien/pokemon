package com.example.pokemomproj;

import android.content.Context;

import android.util.Log;


import androidx.appcompat.widget.AppCompatImageView;

public class skill4 extends AppCompatImageView {
    private static final String TAG = "earthquakeSkill";
    private CharacterView characterView;
    private int dmg = 500;  // Lượng máu hồi phục
    private int mana = 10;    // Mana tiêu hao
    private BotView botView;



    public skill4(Context context, CharacterView characterView) {
        super(context);
        this.characterView = characterView;
        EarthQuakeEffect();
    }
   private void EarthQuakeEffect() {
        setImageResource(R.drawable.earthquake); // Load earthquake.png

        // Đặt z-order để đảm bảo nó nằm dưới các phần tử giao diện khác
        // Giá trị âm để đặt nó dưới các view khác

        // Tạo Handler để xóa hình ảnh sau 0.5 giây
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setImageResource(0); // Xóa hình ảnh
            }
        }, 500); // 500 milliseconds = 0.5 giây

        float characterX = characterView.getCharacterX();
        float characterY = characterView.getCharacterY();

        // Tính toán vị trí bên dưới chân nhân vật
        float skillX = characterX;
        float skillY = characterY - 100;

        // Đặt vị trí của hiệu ứng kỹ năng
        setX(skillX);
        setY(skillY);
    }
    public void setBotView(BotView botView) {
        this.botView = botView;
    }

    public void startDamage() {
        float characterX = characterView.getCharacterX()-80;
        float characterY = characterView.getCharacterY();
        float damageRadius = 300; // Define the radius of the circular damage area

        float enemyX = botView.getCharacterX()-80;
        float enemyY = botView.getCharacterY();

        // Calculate the Euclidean distance between the character and the enemy
        float distance = (float) Math.sqrt(Math.pow(characterX - enemyX, 2) + Math.pow(characterY - enemyY, 2));

        // Check if the enemy is within the circular damage radius
        if (distance <= damageRadius) {
            botView.reduceHp(dmg); // Apply 50 damage to the enemy
            Log.d("skill4", "xy play + " + characterX + " " + characterY+" enemy + " + enemyX + " " + enemyY);
        } else {
            Log.d(TAG, "Bot is out of range. No damage applied.");
        }
    }
}