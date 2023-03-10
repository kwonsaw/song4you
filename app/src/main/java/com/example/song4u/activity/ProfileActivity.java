package com.example.song4u.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.song4u.R;
import com.example.song4u.Util.Errorcode;
import com.example.song4u.databinding.ProfileactivityBinding;
import com.example.song4u.network.NetworkManager;
import com.example.song4u.network.resultmodel.SetUserNicknameResultModel;
import com.example.song4u.network.resultmodel.SetUserProfileResultModel;
import com.google.android.exoplayer2.util.Log;

import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends Season2BaseActivity {

    private ProfileactivityBinding binding;

    private SetUserProfileResultModel setUserProfileResultModel;
    private SetUserNicknameResultModel setUserNicknameResultModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ProfileactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
        super.finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    public void initView() {

        binding.nicknameText.requestFocus();

        Glide.with(this)
                .load(getIntent().getStringExtra("imgUrl"))
                .error(R.drawable.ic_baseline_account_circle_24)
                .into(binding.mImg);

        Glide.with(this)
                .load(R.drawable.ic_baseline_settings_24)
                .error(R.drawable.ic_baseline_settings_24)
                .into(binding.sImg);

        binding.mImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfilePopup();
            }
        });

        binding.sImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfilePopup();
            }
        });

        binding.modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nicknameCheck();
            }
        });

        /* 한글 + 영문 + 숫자만 허용 */
        binding.nicknameText.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                Pattern ps = Pattern.compile("^[a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ\\u318D\\u119E\\u11A2\\u2022\\u2025a\\u00B7\\uFE55]+$");
                if (source.equals("") || ps.matcher(source).matches()) {
                    return source;
                }
                //showToast("한글, 영문, 숫자만 입력 가능합니다.");
                return "";
            }
        },new InputFilter.LengthFilter(12)});

        binding.nicknameText.addTextChangedListener(new TextWatcher() {
            @Override
            //입력하기 전에 호출되는 API
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            //EditText에 변화가 있을 때
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString();
                if (start >= 11) {
                    showToast("닉네임은 최대 12자 이내로 가능합니다.");
                }
                binding.cText.setText(text.length()+" / 12");
            }

            @Override
            //입력이 끝났을 때
            public void afterTextChanged(Editable s) {

            }
        });


    }

    private void initActionBar() {

        mBtn.setVisibility(View.VISIBLE);
        TextCount.setVisibility(View.GONE);
        mTitle.setVisibility(View.VISIBLE);
        alarm.setVisibility(View.GONE);
        mIcon.setVisibility(View.GONE);
        mTitle.setText("닉네임 수정");

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

    /**
     * 닉네임 체크 후 수정
     */

    public void nicknameCheck() {

        if (binding.nicknameText.getText().toString().replace(" ", "").equals("")) {
            showToast("닉네임을 입력해주세요.");
        } else {
            setUserNickname(getUserId(), binding.nicknameText.getText().toString());
        }

    }


    /**
     * 프로필 사진 업로드
     * 사진 , 카메라 선택
     */

    public void ProfilePopup() {


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

               goAlbum();

            } else {

                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            }

        } else {
            goAlbum();
        }


    }

    public void goAlbum() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("프로필 사진 수정");
        dialog.setPositiveButton("사진첩", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                launcher.launch(intent);

            }
        });

        dialog.setNegativeButton("닫기", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {

            }
        });

        dialog.show();


    }

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>()
            {
                @Override
                public void onActivityResult(ActivityResult result)
                {
                    if (result.getResultCode() == RESULT_OK)
                    {

                        Intent intent = result.getData();
                        Uri uri = intent.getData();

                        setUserProfile(uri, getUserId());

                    }
                }
            });



    /**
     /* 프로필 이미지 업로드
     **/

    public void setUserProfile(Uri path, String userid){

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor()).build();

        NetworkManager manager = new NetworkManager();
        manager.setUserProfile(path, userid, client, new Callback<SetUserProfileResultModel>() {
            @Override
            public void onResponse(Call<SetUserProfileResultModel> call, Response<SetUserProfileResultModel> response) {
                setUserProfileResultModel = response.body();

                if(response.isSuccessful()) {

                    if (setUserProfileResultModel.getResultCode().equalsIgnoreCase("200")) {

                        showToast("프로필 사진이 수정되었습니다.");
                        Glide.with(ProfileActivity.this)
                                .load(path)
                                .into(binding.mImg);

                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);

                    } else {
                        showToast(Errorcode.ERROCODE(setUserProfileResultModel.getResultCode()));
                    }

                } else {
                    showToast(Errorcode.ERROCODE(setUserProfileResultModel.getResultCode()));
                }

            }

            @Override
            public void onFailure(Call<SetUserProfileResultModel> call, Throwable t) {
                showToast("이미지 업로드에 실패했습니다. 다른 사진을 선택해주세요.");
                Log.e("kwonsaw",""+t.getMessage());
            }
        });

    }

    /**
     /* 사용자 정보
     **/

    public void setUserNickname(String userid, String nickname){

        NetworkManager manager = new NetworkManager();
        manager.setUserNickname(userid, nickname, new Callback<SetUserNicknameResultModel>() {
            @Override
            public void onResponse(Call<SetUserNicknameResultModel> call, Response<SetUserNicknameResultModel> response) {
                setUserNicknameResultModel = response.body();

                if(response.isSuccessful()) {

                    if (setUserNicknameResultModel.getResultCode().equalsIgnoreCase("200")) {

                        showToast("닉네임이 수정되었습니다.");
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();

                    } else {

                        showToastLong(Errorcode.ERROCODE(setUserNicknameResultModel.getResultCode()));
                    }

                } else {
                    showToast(Errorcode.ERROCODE("1001"));
                }

            }

            @Override
            public void onFailure(Call<SetUserNicknameResultModel> call, Throwable t) {
                showToast(Errorcode.ERROCODE("1001"));
            }
        });

    }

    private HttpLoggingInterceptor httpLoggingInterceptor(){

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                android.util.Log.e("MyGitHubData :", message + "");
            }
        });

        return interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    }
}
