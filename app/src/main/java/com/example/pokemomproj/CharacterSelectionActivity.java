    package com.example.pokemomproj;

    import android.content.Intent;
    import android.content.SharedPreferences;
    import android.os.Bundle;


import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


    import androidx.appcompat.app.AppCompatActivity;
    import androidx.recyclerview.widget.GridLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import com.google.gson.Gson;
    import com.google.gson.reflect.TypeToken;

    import java.lang.reflect.Type;
    import java.util.ArrayList;
    import java.util.Arrays;
    import java.util.HashSet;
    import java.util.Set;


    public class CharacterSelectionActivity extends AppCompatActivity {






    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );

        setContentView(R.layout.activity_character_selection);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewImage);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3)); // 3 cột


        SharedPreferences gamePrefs = getSharedPreferences("GameData", MODE_PRIVATE);
        String deadCharacter = gamePrefs.getString("deadCharacter", null);

            SharedPreferences pref = getSharedPreferences("dsHinh_pref", MODE_PRIVATE);
            String json = pref.getString("dsHinh_data", null);


        ArrayList<ImageOnlyAdapter.HinhAnh> dsHinh;
        Gson gson = new Gson();

        if (json != null ) {
            // Tạo lại danh sách từ chuỗi JSON
            Type type = new TypeToken<ArrayList<ImageOnlyAdapter.HinhAnh>>() {}.getType();
            dsHinh = gson.fromJson(json, type);
        } else {
            // Nếu chưa có dữ liệu, tạo danh sách mặc định
            dsHinh = new ArrayList<>();
            dsHinh.add(new ImageOnlyAdapter.HinhAnh("psyduck", R.drawable.psyduck));
            dsHinh.add(new ImageOnlyAdapter.HinhAnh("pikachu", R.drawable.pikachu));
            dsHinh.add(new ImageOnlyAdapter.HinhAnh("incineroar", R.drawable.incineroar));
            dsHinh.add(new ImageOnlyAdapter.HinhAnh("gardervoid", R.drawable.gardervoid));
            dsHinh.add(new ImageOnlyAdapter.HinhAnh("giratina", R.drawable.giratina));
            dsHinh.add(new ImageOnlyAdapter.HinhAnh("urshifu", R.drawable.urshifu));
        }



        if (deadCharacter != null) {
            for (int i = 0; i < dsHinh.size(); i++) {
                if (dsHinh.get(i).getTenHinh().equals(deadCharacter)) {
                    dsHinh.remove(i);
                    break;
                }
            }

            // Cập nhật lại dữ liệu SharedPreferences
            String updatedJson = gson.toJson(dsHinh);
            pref.edit().putString("dsHinh_data", updatedJson).apply();

            // Xoá flag để tránh xóa lại lần nữa
            gamePrefs.edit().remove("deadCharacter").apply();
        }

        if (dsHinh.isEmpty()) {
            showGameOverScreen();
            return;
        }

        ImageOnlyAdapter adapter = new ImageOnlyAdapter(this, dsHinh);
        recyclerView.setAdapter(adapter);


            if (json != null ) {
                // Tạo lại danh sách từ chuỗi JSON
                Type type = new TypeToken<ArrayList<ImageOnlyAdapter.HinhAnh>>() {}.getType();
                dsHinh = gson.fromJson(json, type);
            } else {
                // Nếu chưa có dữ liệu, tạo danh sách mặc định
                dsHinh = new ArrayList<>();
                dsHinh.add(new ImageOnlyAdapter.HinhAnh("psyduck", R.drawable.psyduck));
                dsHinh.add(new ImageOnlyAdapter.HinhAnh("pikachu", R.drawable.pikachu));
                dsHinh.add(new ImageOnlyAdapter.HinhAnh("incineroar", R.drawable.incineroar));
                dsHinh.add(new ImageOnlyAdapter.HinhAnh("gardervoid", R.drawable.gardervoid));
                dsHinh.add(new ImageOnlyAdapter.HinhAnh("giratina", R.drawable.giratina));
                dsHinh.add(new ImageOnlyAdapter.HinhAnh("urshifu", R.drawable.urshifu));
            }


            ImageOnlyAdapter adapter = new ImageOnlyAdapter(this, dsHinh);
            recyclerView.setAdapter(adapter);

            adapter.setOnItemClickListener(position -> {
                ImageOnlyAdapter.HinhAnh hinhAnh = dsHinh.get(position);

                // 1. Xử lý chọn nhân vật trước
                selectCharacter(hinhAnh.getTenHinh());

                // 2. Xoá ảnh khỏi danh sách (sau khi xử lý)
                dsHinh.remove(position);
                adapter.notifyItemRemoved(position);

                // 3. Lưu danh sách ảnh còn lại vào SharedPreferences
                String newJson = gson.toJson(dsHinh); // dùng lại gson đã khai báo ở trên
                pref.edit().putString("dsHinh_data", newJson).apply();


            });

    private void showGameOverScreen() {
        setContentView(R.layout.game_over_layout);

        // Hiển thị số bot đã giết
        SharedPreferences botPrefs = getSharedPreferences("bot_stats", MODE_PRIVATE);
        int deathCount = botPrefs.getInt("deathCount", 0);

        TextView txtGameOver = findViewById(R.id.txtGameOver);
        txtGameOver.setText("Game Over\nSố bot đã giết: " + deathCount);

        Button btnRestart = findViewById(R.id.btnRestart);
        btnRestart.setOnClickListener(v -> resetGame());
    }
    private void resetGame() {
        // Khôi phục dsHinh mặc định
        ArrayList<ImageOnlyAdapter.HinhAnh> dsHinh = new ArrayList<>();
        dsHinh.add(new ImageOnlyAdapter.HinhAnh("psyduck", R.drawable.psyduck));
        dsHinh.add(new ImageOnlyAdapter.HinhAnh("pikachu", R.drawable.pikachu));
        dsHinh.add(new ImageOnlyAdapter.HinhAnh("incineroar", R.drawable.incineroar));
        dsHinh.add(new ImageOnlyAdapter.HinhAnh("gardervoid", R.drawable.gardervoid));
        dsHinh.add(new ImageOnlyAdapter.HinhAnh("giratina", R.drawable.giratina));
        dsHinh.add(new ImageOnlyAdapter.HinhAnh("urshifu", R.drawable.urshifu));

        SharedPreferences pref = getSharedPreferences("dsHinh_pref", MODE_PRIVATE);
        String json = new Gson().toJson(dsHinh);
        pref.edit().putString("dsHinh_data", json).apply();

        // Xoá item
        getSharedPreferences("dsitem_pref", MODE_PRIVATE).edit().clear().apply();

        // Reset chỉ số bot
        getSharedPreferences("bot_stats", MODE_PRIVATE).edit().clear().apply();

        // Reset trạng thái GameData
        getSharedPreferences("GameData", MODE_PRIVATE).edit().clear().apply();

        // Quay lại giao diện chọn nhân vật
        recreate(); // hoặc startActivity lại chính nó
    }



        }

        private void selectCharacter(String characterName) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("characterName", characterName);
            startActivity(intent);

        }


    }