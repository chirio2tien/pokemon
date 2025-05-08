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
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class gift extends AppCompatActivity {
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                getSupportActionBar().hide();
                setContentView(R.layout.gift);
               RecyclerView recyclerView = findViewById(R.id.recyclerViewImage);recyclerView.setLayoutManager(new GridLayoutManager(this, 4)); // 4 cột


                SharedPreferences pref = getSharedPreferences("dsHinh_pref", MODE_PRIVATE);
                String json = pref.getString("dsHinh_data", null);

                ArrayList<ImageOnlyAdapter.HinhAnh> dsHinh;
                Gson gson = new Gson();

                if (json != null) {
                    // Tạo lại danh sách từ chuỗi JSON
                    Type type = new TypeToken<ArrayList<ImageOnlyAdapter.HinhAnh>>() {}.getType();
                    dsHinh = gson.fromJson(json, type);
                } else {
                    // Nếu chưa có dữ liệu, tạo danh sách mặc định
                    dsHinh = new ArrayList<>();
                    dsHinh.add(new ImageOnlyAdapter.HinhAnh("g_aspear_berry", R.drawable.g_aspear_berry));
                    dsHinh.add(new ImageOnlyAdapter.HinhAnh("g_berry_sprite", R.drawable.g_berry_sprite));
                    dsHinh.add(new ImageOnlyAdapter.HinhAnh("g_leppa_berry", R.drawable.g_leppa_berry));
                    dsHinh.add(new ImageOnlyAdapter.HinhAnh("g_lum_berry", R.drawable.g_lum_berry));
                    dsHinh.add(new ImageOnlyAdapter.HinhAnh("g_oran_berry", R.drawable.g_oran_berry));
                    dsHinh.add(new ImageOnlyAdapter.HinhAnh("g_pecha_berry", R.drawable.g_pecha_berry));
                    dsHinh.add(new ImageOnlyAdapter.HinhAnh("g_persim_berry", R.drawable.g_persim_berry));
                    dsHinh.add(new ImageOnlyAdapter.HinhAnh("g_petaya_berry", R.drawable.g_petaya_berry));
                    dsHinh.add(new ImageOnlyAdapter.HinhAnh("g_sitrus_berry", R.drawable.g_sitrus_berry));

                }

                // Xáo trộn danh sách
                Collections.shuffle(dsHinh);

// Lấy 4 phần tử đầu tiên
                dsHinh = new ArrayList<>(dsHinh.subList(0, 4));

                ImageOnlyAdapter adapter = new ImageOnlyAdapter(this, dsHinh);
//                recyclerView.setAdapter(adapter);


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