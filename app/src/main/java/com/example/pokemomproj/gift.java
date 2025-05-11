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
               RecyclerView recyclerView = findViewById(R.id.recyclerViewImageItem);
               recyclerView.setLayoutManager(new GridLayoutManager(this, 4)); // 4 cột


                final  SharedPreferences pref = getSharedPreferences("dsitem_pref", MODE_PRIVATE);
                String json = pref.getString("dsitem_data", null);

                final  ArrayList<ImageItem.item> dsHinhitem;
                final   Gson gson = new Gson();

                if (json != null) {
                    // Tạo lại danh sách từ chuỗi JSON
                    Type type = new TypeToken<ArrayList<ImageItem.item>>() {}.getType();
                    dsHinhitem = gson.fromJson(json, type);
                } else {
                    // Nếu chưa có dữ liệu, tạo danh sách mặc định
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

                // Xáo trộn danh sách
                Collections.shuffle(dsHinhitem);

// Lấy 4 phần tử đầu tiên
                final ArrayList<ImageItem.item> displayList = new ArrayList<>(dsHinhitem.subList(0, 4));

                ImageItem adapter = new ImageItem(this, displayList);

                recyclerView.setAdapter(adapter);


                adapter.setOnItemClickListener(position -> {
                    ImageItem.item hinhAnh = dsHinhitem.get(position);

                    // 1. Xử lý chọn nhân vật trước
                    selectItem(hinhAnh.gettenitem());

                    // 2. Xoá ảnh khỏi danh sách (sau khi xử lý)


                    // 3. Lưu danh sách ảnh còn lại vào SharedPreferences
                    String newJson = gson.toJson(dsHinhitem); // dùng lại gson đã khai báo ở trên
                    pref.edit().putString("dsitem_data", newJson).apply();

                });


            }





            private void selectItem(String itemName) {
                Intent intent = new Intent(this, MainActivity.class);

                startActivity(intent);

            }


        }