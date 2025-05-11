package com.example.pokemomproj;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
        setContentView(R.layout.activity_character_selection);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewImage);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3)); // 3 cột


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


    }





    private void selectCharacter(String characterName) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("characterName", characterName);
        startActivity(intent);

    }


}