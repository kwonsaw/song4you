package com.example.song4u.activity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.example.song4u.databinding.SavepopupviewBinding;

public class SavePopupView extends Season2BaseActivity {

    private SavepopupviewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SavepopupviewBinding.inflate(getLayoutInflater());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(binding.getRoot());

        binding.savetext.setText(getIntent().getStringExtra("point")+"P 적립되었습니다");

        binding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }


}
