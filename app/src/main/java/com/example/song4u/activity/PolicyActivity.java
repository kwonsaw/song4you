package com.example.song4u.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.song4u.R;
import com.example.song4u.databinding.PolicyactivityBinding;

public class PolicyActivity extends Season2BaseActivity{

    private PolicyactivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = PolicyactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initActionBar();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initActionBar() {

        mBtn.setVisibility(View.VISIBLE);
        TextCount.setVisibility(View.GONE);
        mTitle.setVisibility(View.VISIBLE);
        alarm.setVisibility(View.GONE);
        mIcon.setVisibility(View.GONE);

        if ("policy1".equalsIgnoreCase(getIntent().getStringExtra("type"))) {
            binding.policy2.setVisibility(View.GONE);
            binding.tpolicy1.setVisibility(View.GONE);
            mTitle.setText("서비스 이용 약관");
        } else if ("policy2".equalsIgnoreCase(getIntent().getStringExtra("type"))) {
            binding.policy1.setVisibility(View.GONE);
            binding.tpolicy2.setVisibility(View.GONE);
            mTitle.setText("개인정보 처리방침");
        } else {
            mTitle.setText("서비스 이용약관 및 개인정보 처리방침");
        }

        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(mActionBarBackgroundDrawable);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(ActionbarView);
        actionBar.setElevation(0);
    }

}
