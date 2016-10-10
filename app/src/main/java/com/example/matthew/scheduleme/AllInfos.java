package com.example.matthew.scheduleme;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class AllInfos {
    private static AllInfos allInfos;
    private SharedPreferences sharedPreferences;

    public static AllInfos getInstance(Context context) {
        if (allInfos == null) {
            allInfos = new AllInfos(context);
        }
        return allInfos;
    }

    private AllInfos(Context context) {
        sharedPreferences = context.getSharedPreferences("Infos",Context.MODE_PRIVATE);
    }

    public void saveData(String key,String value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString(key, value);
        prefsEditor.commit();
    }

    public String getData(String key) {
        if (sharedPreferences != null) {
            return sharedPreferences.getString(key, "");
        }
        return "";
    }
}
