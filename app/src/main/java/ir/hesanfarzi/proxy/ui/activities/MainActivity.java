package ir.hesanfarzi.proxy.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import ir.hesanfarzi.proxy.databinding.ActivityMainBinding;
import ir.hesanfarzi.proxy.ui.adapter.ProxyAdapter;
import ir.hesanfarzi.proxy.ui.dialog.StatusDialog;
import ir.hesanfarzi.proxy.utility.ProxyViewModel;

public class MainActivity extends AppCompatActivity {

    ProxyAdapter adapter;
    ProxyViewModel viewModel;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /* menu buttons */
        binding.included.setting.setOnClickListener(view ->
                startActivity(new Intent(this, SettingActivity.class)));
        binding.included.refresh.setOnClickListener(view -> getData());
        binding.included.back.setOnClickListener(view -> onBackPressed());

        /* show data in recyclerView */
        adapter = new ProxyAdapter(new ProxyAdapter.ProxyDiff(), ip ->
                new StatusDialog(MainActivity.this, ip));
        binding.recyclerView.setAdapter(adapter);

        /* get data from api */
        viewModel = new ViewModelProvider(this).get(ProxyViewModel.class);
        getData();
    }


    private void getData() {
        viewModel.getProxyList().observe(this, data -> {
            if (data.size() < 1) binding.error.setVisibility(View.VISIBLE);
            else binding.error.setVisibility(View.GONE);
            adapter.submitList(data);
        });
    }


}