package com.example.pokemomproj;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;

public class gift extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.gift);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewImageItem);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4)); // 4 cột

        SharedPreferences pref = getSharedPreferences("dsitem_pref", MODE_PRIVATE);
        Gson gson = new Gson();

        // Lấy danh sách từ SharedPreferences
        String json = pref.getString("dsitem_data", null);
        ArrayList<ImageItem.item> dsHinhitem;

        if (json != null) {
            Type type = new TypeToken<ArrayList<ImageItem.item>>() {}.getType();
            dsHinhitem = gson.fromJson(json, type);
        } else {
            dsHinhitem = new ArrayList<>();
            dsHinhitem.add(new ImageItem.item("g_aspear_berry", R.drawable.g_aspear_berry));
            dsHinhitem.add(new ImageItem.item("g_berry_sprite", R.drawable.g_berry_sprite));
            dsHinhitem.add(new ImageItem.item("g_leppa_berry", R.drawable.g_leppa_berry));
            dsHinhitem.add(new ImageItem.item("g_lum_berry", R.drawable.g_lum_berry));
            dsHinhitem.add(new ImageItem.item("g_oran_berry", R.drawable.g_oran_berry));
            dsHinhitem.add(new ImageItem.item("g_pecha_berry", R.drawable.g_pecha_berry));
            dsHinhitem.add(new ImageItem.item("g_persim_berry", R.drawable.g_persim_berry));
            dsHinhitem.add(new ImageItem.item("g_petaya_berry", R.drawable.g_petaya_berry));
            dsHinhitem.add(new ImageItem.item("g_sitrus_berry", R.drawable.g_sitrus_berry));
        }

        // Shuffle và lấy 4 item ngẫu nhiên
        Collections.shuffle(dsHinhitem);
        ArrayList<ImageItem.item> displayList = new ArrayList<>(dsHinhitem.subList(0, Math.min(4, dsHinhitem.size())));

        ImageItem adapter = new ImageItem(this, displayList);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(position -> {
            ImageItem.item selectedItem = displayList.get(position);

            // Tăng số lượng của item trong danh sách chính
            for (ImageItem.item item : dsHinhitem) {
                if (item.gettenitem().equals(selectedItem.gettenitem())) {
                    item.tangSoLuong();
                    break;
                }
            }

            // Lưu lại danh sách đã cập nhật vào SharedPreferences
            String newJson = gson.toJson(dsHinhitem);
            pref.edit().putString("dsitem_data", newJson).apply();

            // Gửi item đã chọn sang MainActivity
            selectItem(selectedItem.gettenitem());
        });
    }

    private void selectItem(String characterName) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("characterName", characterName);
        startActivity(intent);
    }

    // Đang xem sex phương thức để clear toàn bộ SharedPreferences
//    public void clearData(View view) {
//        SharedPreferences pref = getSharedPreferences("dsitem_pref", MODE_PRIVATE);
//        SharedPreferences.Editor editor = pref.edit();
//        editor.clear(); // Xóa toàn bộ SharedPreferences
//        editor.apply();
//        Toast.makeText(this, "Data cleared!", Toast.LENGTH_SHORT).show();
//
//      }
}
