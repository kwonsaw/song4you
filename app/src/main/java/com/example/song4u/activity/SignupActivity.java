package com.example.song4u.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;

import com.example.song4u.Application.AppAplication;
import com.example.song4u.R;
import com.example.song4u.Util.CommonUtil;
import com.example.song4u.Util.Errorcode;
import com.example.song4u.databinding.SignupactivityBinding;
import com.example.song4u.network.NetworkManager;
import com.example.song4u.network.resultmodel.SetUserLoginResultModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends Season2BaseActivity implements View.OnClickListener {

    private SetUserLoginResultModel setUserLoginResultModel;

    private SignupactivityBinding binding;
    private CheckBox check1, check2, check3;

    private String getUserId, getNickname, getImgurl, getLogintype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SignupactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        intViewData();
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

    public void intViewData() {

        binding.arrow1.setOnClickListener(this);
        binding.arrow2.setOnClickListener(this);
        binding.success.setOnClickListener(this);

        getUserId = getIntent().getStringExtra("userid");
        getNickname = getIntent().getStringExtra("nickname");
        getImgurl = getIntent().getStringExtra("imgurl");
        getLogintype = getIntent().getStringExtra("logintype");


    }

    private void initActionBar() {

        mTitle.setVisibility(View.VISIBLE);
        mBtn.setVisibility(View.GONE);
        mIcon.setVisibility(View.GONE);

        //mTitle.setText("이용약관");

        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(mActionBarBackgroundDrawable);
        actionBar.setDisplayHomeAsUpEnabled(true); //왼쪽 뒤로가기 버튼
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setHomeAsUpIndicator(R.mipmap.close);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(ActionbarView);
        actionBar.setElevation(0);

    }


    @Override
    public void onClick(View view) {

        Intent i = new Intent(getApplicationContext(), PolicyActivity.class);

        switch (view.getId()) {

            case R.id.arrow1:

                i.putExtra("type", "policy1");
                startActivity(i);

                break;

            case R.id.arrow2:

                i.putExtra("type", "policy2");
                startActivity(i);

                break;

            case R.id.success:

                if (!binding.check1.isChecked()) {
                    showToastLong("서비스 이용약관 동의가 필요합니다.");
                } else if (!binding.check2.isChecked()) {
                    showToastLong("개인정보 수집 및 처리방침 동의가 필요합니다.");
                } else if (!binding.check3.isChecked()) {
                    showToastLong("만 14세 이상 동의가 필요합니다.");
                } else {
                    setUserInfo(getUserId,getImgurl,getNickname,getLogintype);
                }

                break;


        }
    }

    /**
     /* 회원가입
     **/

    public void setUserInfo(String userid, String image, String nickname, String logintype){

        NetworkManager manager = new NetworkManager();
        manager.setUserInfo(userid, AppAplication.androiddid, getVersion(), "001", nickname, image, logintype, AppAplication.osInfo, new Callback<SetUserLoginResultModel>() {
            @Override
            public void onResponse(Call<SetUserLoginResultModel> call, Response<SetUserLoginResultModel> response) {
                setUserLoginResultModel = response.body();

                if(response.isSuccessful()) {

                    //최초 회원가입 성공 시
                    if (setUserLoginResultModel.getResultCode().equalsIgnoreCase("200")) {

                        showToast("환영합니다. 회원가입 후 로그인되었습니다.");
                        CommonUtil.mSharePrefreences(getApplicationContext(), getString(R.string.share_userId), getUserId, "guest");
                        Intent i = new Intent(getApplicationContext() , MainActivity.class);
                        startActivity(i);
                        finish();

                        //이미 가입 후 로그인 시
                    } else if (setUserLoginResultModel.getResultCode().equalsIgnoreCase("201")) {

                        showToast("환영합니다. 로그인되었습니다.");
                        Intent i = new Intent(getApplicationContext() , MainActivity.class);
                        startActivity(i);
                        finish();

                    } else {

                        showToastLong(Errorcode.ERROCODE(setUserLoginResultModel.getResultCode()));
                    }

                } else {
                    showToast(Errorcode.ERROCODE("1001"));
                }

                //hideLoding();

            }

            @Override
            public void onFailure(Call<SetUserLoginResultModel> call, Throwable t) {

                showToast(Errorcode.ERROCODE("1001"));

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

}
