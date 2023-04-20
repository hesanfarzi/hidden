package ir.hesanfarzi.proxy.utility;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

import ir.hesanfarzi.proxy.R;

public class App extends Application {

    public static String api;
    public static boolean refresh = true;

    @Override
    public void onCreate() {
        super.onCreate();

        SharedPreferences sharedPref = getSharedPreferences("setting_100", Context.MODE_PRIVATE);

        String s = getResources().getStringArray(R.array.api_array)[0];
        api = sharedPref.getString("server", s);

        int theme = sharedPref.getInt("theme", 1);
        AppCompatDelegate.setDefaultNightMode(theme);
    }

}