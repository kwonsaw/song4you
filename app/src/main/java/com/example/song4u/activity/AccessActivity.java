package com.example.song4u.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.example.song4u.Adapter.AccessListAdapter;
import com.example.song4u.Data.AccessListData;
import com.example.song4u.R;
import com.example.song4u.Util.CommonUtil;
import com.example.song4u.databinding.AccessactivityBinding;

import java.util.ArrayList;

public class AccessActivity extends Season2BaseActivity {

    private AccessactivityBinding binding;

    ArrayList<AccessListData> accessListData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AccessactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        initActionBar();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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

    public void init() {

        InitializeAccessListData();

        ListView mListView = binding.mListView;

        final AccessListAdapter myAdapter = new AccessListAdapter(AccessActivity.this,accessListData);
        mListView.setAdapter(myAdapter);

        binding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonUtil.mSharePrefreences(getApplicationContext(), getString(R.string.share_access), "1", "0");
                finish();
            }
        });




    }

    private void initActionBar() {

        TextCount.setVisibility(View.GONE);
        mTitle.setVisibility(View.VISIBLE);
        alarm.setVisibility(View.GONE);
        mtxt.setVisibility(View.GONE);
        mIcon.setVisibility(View.GONE);

        mTitle.setText("앱 접근 권한 안내");

        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(mActionBarBackgroundDrawable);
        //actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(ActionbarView);
        actionBar.setElevation(0);

        if (getIntent().getStringExtra("type").equalsIgnoreCase("start")) {
            mBtn.setVisibility(View.GONE);
            actionBar.setDisplayHomeAsUpEnabled(false);
        } else {
            mBtn.setVisibility(View.VISIBLE);
            actionBar.setDisplayHomeAsUpEnabled(true);
            binding.close.setVisibility(View.GONE);
        }
    }

    public void InitializeAccessListData()
    {
        accessListData = new ArrayList<AccessListData>();

        accessListData.add(new AccessListData("연락처"));
        accessListData.add(new AccessListData("전화"));
        accessListData.add(new AccessListData("카메라"));
        accessListData.add(new AccessListData("파일 및 미디어"));

    }
}
