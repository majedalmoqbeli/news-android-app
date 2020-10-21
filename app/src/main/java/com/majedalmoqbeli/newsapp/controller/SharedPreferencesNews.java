package com.majedalmoqbeli.newsapp.controller;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.majedalmoqbeli.newsapp.constants.AppKey;
import com.majedalmoqbeli.newsapp.models.NewsData;


import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.majedalmoqbeli.newsapp.constants.AppKey.DEFAULT_VALUE;


public class SharedPreferencesNews {
    private static SharedPreferencesNews obj;

    private static final String MyPREFERENCES = "MyNewsPrf";
    private Context context;
    private SharedPreferences sharedPreferences;

    private SharedPreferencesNews(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
    }

    public static SharedPreferencesNews getInstance(Context context) {
        if (obj == null) {
            obj = new SharedPreferencesNews(context);
        }
        return obj;
    }


    public void SaveData(ArrayList<NewsData> myObject) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(myObject);
        prefsEditor.putString(AppKey.NEWS_KEY , json);
        prefsEditor.apply();
    }


    public ArrayList<NewsData> LoadData() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(
                AppKey.NEWS_KEY , DEFAULT_VALUE);

        if (!json.equals(DEFAULT_VALUE)) {

            ArrayList<NewsData> list =
                    gson.fromJson(json, new
                            TypeToken<ArrayList<NewsData>>() {
                            }.getType());
            if (list.size() > 0) {
                return list;
            } else {
                return null;
            }
        }
        return null;
    }


    private void deleteKey(String key) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

}
