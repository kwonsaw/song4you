package com.example.song4u.activity;

import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;

import com.example.song4u.R;
import com.example.song4u.Util.GlideImageGetter;
import com.example.song4u.databinding.NoticedetailactivityBinding;

public class NoticeDetailActivity extends Season2BaseActivity {

    private NoticedetailactivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = NoticedetailactivityBinding.inflate(getLayoutInflater());
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

        /* textview에 html img 태그 적용 */
        GlideImageGetter getter = new GlideImageGetter(NoticeDetailActivity.this, binding.mText);
        Spanned htmlSpan = Html.fromHtml(getIntent().getStringExtra("desc"), getter, null);
        binding.mText.setText(htmlSpan);

        binding.mText.setMovementMethod(new ScrollingMovementMethod());

    }

    private void initActionBar() {

        mBtn.setVisibility(View.VISIBLE);
        TextCount.setVisibility(View.GONE);
        mTitle.setVisibility(View.VISIBLE);
        alarm.setVisibility(View.GONE);
        mIcon.setVisibility(View.GONE);

        mTitle.setText(getIntent().getStringExtra("title"));

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
