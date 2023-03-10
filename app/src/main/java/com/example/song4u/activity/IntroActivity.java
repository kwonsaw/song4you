package com.example.song4u.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.SpannableString;
import android.text.util.Linkify;

import com.example.song4u.R;
import com.example.song4u.Util.Errorcode;
import com.example.song4u.databinding.IntroactivityBinding;
import com.example.song4u.network.NetworkManager;
import com.example.song4u.network.resultmodel.GetAppVersionResultModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IntroActivity extends Season2BaseActivity {

    private IntroactivityBinding binding;

    private GetAppVersionResultModel getAppVersionResultModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = IntroactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        new CountDownTimer(2000 , 1000){
            @Override
            public void onTick(long millisUntilFinished) {}
            @Override
            public void onFinish() {
                getAppVersion();
            }
        }.start();


    }

    public void NextActivity(){

        if (LoginCheck()) {

            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
            finish();

        } else {

            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();

        }


    }

    public boolean LoginCheck(){

        String userId = getUserId();

        if (userId.equals("guest")){
            return true;
        }
        return false;
    }

    //버전 강제 업데이트
    public void VersionUpdate(String msg, String marketurl){

        android.app.AlertDialog.Builder Dialog = new AlertDialog.Builder(this);
        Dialog.setCancelable(false);
        Dialog.setTitle(getString(R.string.app_name));

        SpannableString msgs = new SpannableString(msg.replace("<br>" , "\n"));
        Linkify.addLinks(msgs, Linkify.ALL);
        Dialog.setMessage(msgs);

        Dialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

                try {
                    //구글 마켓 이동
                    Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse(marketurl));
                    startActivity(intent);
                    finish();
                }catch(Exception e){
                    finish();
                }

            }
        });
        Dialog.show();
    }

    /**
     /* 앱 버젼 체크
     **/

    public void getAppVersion(){

        NetworkManager manager = new NetworkManager();
        manager.getAppVersion(getOs(),  getVersion(), new Callback<GetAppVersionResultModel>() {
            @Override
            public void onResponse(Call<GetAppVersionResultModel> call, Response<GetAppVersionResultModel> response) {
                getAppVersionResultModel = response.body();

                if (response.isSuccessful()) {

                    //최신 버젼이 아닐 때
                    if (getAppVersionResultModel.getResultCode().equalsIgnoreCase("200")) {

                        VersionUpdate(getAppVersionResultModel.getResult(), getAppVersionResultModel.getMarketUrl());
                        //최신 버젼일 때
                    } else if (getAppVersionResultModel.getResultCode().equalsIgnoreCase("910")) {

                        NextActivity();

                    } else {

                        showToast(Errorcode.ERROCODE("1001"));
                    }

                } else {
                    showToast(Errorcode.ERROCODE("1001"));
                }
            }


            @Override
            public void onFailure(Call<GetAppVersionResultModel> call, Throwable t) {

                showToast(Errorcode.ERROCODE("1001"));

            }
        });

        }
    }


