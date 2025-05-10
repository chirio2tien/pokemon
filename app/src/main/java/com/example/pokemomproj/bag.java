package com.example.pokemomproj;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import com.google.gson.Gson;

public class bag {

    private static final String PREF_NAME = "dsitem_pref";
    private static final String DATA_KEY = "dsitem_data";

    public static void saveItems(Context context, ArrayList<ImageItem.item> items) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(items); // Convert the list to JSON
        editor.putString(DATA_KEY, json); // Save JSON string
        editor.apply(); // Apply changes
    }

    public static ArrayList<ImageItem.item> loadItems(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(DATA_KEY, null);

        if (json != null) {
            Gson gson = new Gson();
            return gson.fromJson(json, new com.google.gson.reflect.TypeToken<ArrayList<ImageItem.item>>() {}.getType());
        } else {
            return new ArrayList<>(); // Return an empty list if no data is found
        }
    }
}