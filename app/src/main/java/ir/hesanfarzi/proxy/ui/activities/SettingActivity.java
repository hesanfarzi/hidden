package ir.hesanfarzi.proxy.ui.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.Objects;

import ir.hesanfarzi.proxy.R;
import ir.hesanfarzi.proxy.databinding.ActivitySettingBinding;
import ir.hesanfarzi.proxy.utility.App;

public class SettingActivity extends AppCompatActivity {

    int index = 0;
    SharedPreferences sharedPref;
    ActivitySettingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPref = getSharedPreferences("setting_100", Context.MODE_PRIVATE);
        changeServer();
        changeTheme();
    }


    private void changeServer() {
        String[] titles = getResources().getStringArray(R.array.api_title);
        String[] api = getResources().getStringArray(R.array.api_array);
        for (int i = 0; i < api.length; i++) {
            if (Objects.equals(App.api, api[i])) index = i;
        }
        binding.server.setText(titles[index]);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setNegativeButton("cancel", (d, w) -> d.dismiss());
        binding.select.setOnClickListener(view -> {
            builder.setSingleChoiceItems(titles, index, (d, w) -> {
                index = w;
                App.refresh = true;
                App.api = api[w];
                sharedPref.edit().putString("server", api[w]).apply();
                binding.server.setText(titles[w]);
                d.dismiss();
            });
            builder.show();
        });
    }


    private void changeTheme() {
        int nightMode = AppCompatDelegate.getDefaultNightMode();
        binding.theme.setChecked(nightMode == 2);
        binding.theme.setOnCheckedChangeListener((view, isChecked) -> {
            if (isChecked) {
                sharedPref.edit().putInt("theme", 2).apply();
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                sharedPref.edit().putInt("theme", 1).apply();
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });
    }


}