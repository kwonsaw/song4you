package com.example.song4u.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.song4u.Application.AppAplication;
import com.example.song4u.R;
import com.example.song4u.Util.Errorcode;
import com.example.song4u.Util.SFUToast;
import com.example.song4u.databinding.ReplywriteactivityBinding;
import com.example.song4u.network.NetworkManager;
import com.example.song4u.network.resultmodel.SetReplyMusicResultModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ReplyWriteActivity extends Season2BaseActivity implements View.OnClickListener{

    private ReplywriteactivityBinding binding;

    private SetReplyMusicResultModel setReplyMusicResultModel;

    private String getMusicid;
    private boolean isKeyboard = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ReplywriteactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getMusicid = getIntent().getStringExtra("musicid");

        initActionBar();
        initView();

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
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        hideKeyboard();
        super.finish();

    }

    @Override
    public void onBackPressed() {
        hideKeyboard();
        super.onBackPressed();

    }

    public void initView() {


        binding.getRoot().setOnTouchListener((v, event) -> {
            if (isKeyboard) {
                hideKeyboard();
            } else {
                showKeyboard();
            }
            return false;
        });

        if (getIntent().getStringExtra("type").equalsIgnoreCase("update")) {
            binding.replyText.setText(getIntent().getStringExtra("desc"));
        }

        binding.replyText.requestFocus();

        binding.replyText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 입력난에 변화가 있을 시 조치
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 입력이 끝났을 때 조치
                binding.cnttext.setText(s.length()+"/100");
                if (s.length() == 100) {
                    SFUToast.showToast(AppAplication.context,"최대 100자까지 입력 가능합니다.");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 입력하기 전에 조치
            }
        });

        binding.cnttext.setText(binding.replyText.getText().length()+"/100");

    }

    private void initActionBar() {

        mBtn.setVisibility(View.VISIBLE);
        TextCount.setVisibility(View.GONE);
        mTitle.setVisibility(View.VISIBLE);
        alarm.setVisibility(View.GONE);
        mtxt.setVisibility(View.VISIBLE);
        mIcon.setVisibility(View.GONE);

        mTitle.setText("글쓰기");
        mtxt.setText("등록");

        mtxt.setOnClickListener(this);

        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(mActionBarBackgroundDrawable);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(ActionbarView);
        actionBar.setElevation(0);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.maincion_btn_txt:

                if (binding.replyText.length() == 0) {
                    showToastTop("댓글을 입력해주세요.");
                }else {
                    if (getIntent().getStringExtra("type").equalsIgnoreCase("update")) {
                        setReplyMusic(getVersion(), "update", getIntent().getStringExtra("replyid"));
                    } else {
                        setReplyMusic(getVersion(), "add", "null");
                    }

                }

                break;

        }

    }

    void hideKeyboard()
    {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(binding.replyText.getWindowToken(), 0);
        //InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        //inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        isKeyboard = false;
    }

    void showKeyboard()
    {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(binding.replyText, InputMethodManager.SHOW_FORCED);
        isKeyboard = true;
    }

    /**
     /* 댓글 작성
     **/

    public void setReplyMusic(String appVersion, String type, String replyid){

        NetworkManager manager = new NetworkManager();
        manager.setReplyMusic(appVersion, getUserId(), getMusicid, replyid, binding.replyText.getText().toString(), type, new Callback<SetReplyMusicResultModel>() {
            @Override
            public void onResponse(Call<SetReplyMusicResultModel> call, Response<SetReplyMusicResultModel> response) {
                setReplyMusicResultModel = response.body();

                if(response.isSuccessful()) {

                    if (setReplyMusicResultModel.getResultCode().equalsIgnoreCase("200")) {
                        SFUToast.showToast(AppAplication.context,"등록되었습니다.");
                        hideKeyboard();
                        finish();
                    } else if (setReplyMusicResultModel.getResultCode().equalsIgnoreCase("705")) {
                        SFUToast.showToast(AppAplication.context,"수정되었습니다.");
                        hideKeyboard();
                        finish();
                    } else {
                        SFUToast.showToast(AppAplication.context, Errorcode.ERROCODE(setReplyMusicResultModel.getResultCode()));
                    }

                } else {
                    SFUToast.showToast(AppAplication.context, Errorcode.ERROCODE("1001"));
                }

            }

            @Override
            public void onFailure(Call<SetReplyMusicResultModel> call, Throwable t) {
                SFUToast.showToast(AppAplication.context, Errorcode.ERROCODE("1001"));
            }
        });

    }

}
