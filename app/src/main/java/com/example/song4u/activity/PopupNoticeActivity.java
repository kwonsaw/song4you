package com.example.song4u.activity;

import android.os.Bundle;

import com.example.song4u.databinding.PopupnoticeactivityBinding;

public class PopupNoticeActivity extends Season2BaseActivity {

    private PopupnoticeactivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = PopupnoticeactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }
}
