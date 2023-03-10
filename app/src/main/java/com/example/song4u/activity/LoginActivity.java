package com.example.song4u.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.song4u.R;
import com.example.song4u.Util.CommonUtil;
import com.example.song4u.Util.Errorcode;
import com.example.song4u.Util.ProgressDialog;
import com.example.song4u.databinding.LoginactivityBinding;
import com.example.song4u.network.NetworkManager;
import com.example.song4u.network.resultmodel.SetUserLoginResultModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.Account;
import com.kakao.sdk.user.model.User;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends Season2BaseActivity implements View.OnClickListener {

    private LoginactivityBinding binding;

    private FirebaseAuth mAuth = null;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    private SignInButton signInButton;

    private SetUserLoginResultModel setUserLoginResultModel;

    private String userIdStr, userImage, userNickname, userLogintype;

    private ProgressDialog dialog = new ProgressDialog(LoginActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = LoginactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        intViewData();

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

    public void intViewData(){

        binding.introGoogl.setOnClickListener(this);
        binding.introKakao.setOnClickListener(this);
        binding.policyTextview.setOnClickListener(this);

        //서비스 이용약관 및 개인정보 처리 방침 underline
        String udata="서비스 이용약관 및 개인정보 처리 방침";
        SpannableString content = new SpannableString(udata);
        content.setSpan(new UnderlineSpan(), 0, udata.length(), 0);
        binding.policyTextview.setText(content);

        // 구글 로그인
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // 앱 접근권한 초기 1회 노출
        if (getAccess().equalsIgnoreCase("0")) {
            Intent i = new Intent(getApplicationContext(), AccessActivity.class);
            i.putExtra("type", "start");
            startActivity(i);
        }

    }

    private void signInGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signInKakaoTalk() {

        if(UserApiClient.getInstance().isKakaoTalkLoginAvailable(this)){

            UserApiClient.getInstance().loginWithKakaoTalk(this, (oAuthToken, error) -> {
                if (error != null) {
                    Log.e("kwonsaw", "로그인 실패 : 카톡 설치되어 있지만 로그인 X", error);
                    signInKakaoAccount();

                } else if (oAuthToken != null) {
                    Log.i("kwonsaw", "로그인 성공(토큰) : " + oAuthToken.getAccessToken());
                    requestMe();
                }
                return null;
            });

        } else {

            signInKakaoAccount();

        }

    }

    private void signInKakaoAccount() {

        UserApiClient.getInstance().loginWithKakaoAccount(this, (oAuthToken, error) -> {

            if (error != null) {
                Log.e("kwonsaw", "로그인 실패 : 카톡 설치 X", error);
                //showToast("카카오톡이 설치되어 있지 않습니다.");
            } else {

                Log.i("kwonsaw", "로그인 성공(토큰) : " + oAuthToken.getAccessToken());
                requestMe();

            }
            return null;
        });

    }

    private void requestMe() {
        // 사용자 정보 요청
        UserApiClient.getInstance().me((user, meError) -> {
            if (meError != null) {

            } else {

                Account kakaoAccount = user.getKakaoAccount();
                updateUIKakao(kakaoAccount, user);
                Log.e("kwonsaw","getId : "+user.getId());
                Log.e("kwonsaw",""+user.getKakaoAccount().getEmail());
                Log.e("kwonsaw",""+user.getKakaoAccount().getProfile().getNickname());
                Log.e("kwonsaw",""+user.getKakaoAccount().getProfile().getProfileImageUrl());
                Log.e("kwonsaw",""+user.getKakaoAccount().getProfile().getThumbnailImageUrl());

            }

            return null;
        });

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.intro_googl:
                signInGoogle();

                break;

            case R.id.intro_kakao:

                signInKakaoTalk();

                break;

            case R.id.policy_textview:

                Intent i = new Intent(getApplicationContext() , PolicyActivity.class);
                startActivity(i);

                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
            }
        } else {
            showToast(Errorcode.ERROCODE("1001"));
            Log.e("kwonsaw",""+requestCode);
            Log.e("kwonsaw",""+resultCode);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUIGoogle(acct, user);
                        } else {
                            showToast(Errorcode.ERROCODE("1001"));
                        }
                    }
                });
    }


    private void updateUIGoogle(GoogleSignInAccount account, FirebaseUser user) {
        if (user != null) {

            setUserLogin(account.getId(), user.getPhotoUrl().toString(), user.getDisplayName(), "google");
        } else {
            showToast(Errorcode.ERROCODE("1001"));
        }
    }

    private void updateUIKakao(Account kakaoAccount ,User user) {
        if (kakaoAccount != null) {
            setUserLogin(String.valueOf(user.getId()), kakaoAccount.getProfile().getThumbnailImageUrl(), kakaoAccount.getProfile().getNickname(), "kakao");
        }else {
            showToast(Errorcode.ERROCODE("1001"));
        }
    }

    /**
    /* 사용자 로그인 체크
    **/
    
    public void setUserLogin(String userid, String image, String nickname, String logintype){

        userIdStr = userid;
        userNickname = nickname;
        userImage = image;
        userLogintype = logintype;

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor()).build();

        NetworkManager manager = new NetworkManager();
        manager.setUserLogin(userid, logintype, client, new Callback<SetUserLoginResultModel>() {
            @Override
            public void onResponse(Call<SetUserLoginResultModel> call, Response<SetUserLoginResultModel> response) {
                setUserLoginResultModel = response.body();

                if(response.isSuccessful()) {

                    //신규 사용자일 때 이용약관으로 이동
                    if (setUserLoginResultModel.getResultCode().equalsIgnoreCase("200")) {

                        Intent i = new Intent(getApplicationContext() , SignupActivity.class);
                        i.putExtra("userid", userIdStr);
                        i.putExtra("nickname", userNickname);
                        i.putExtra("imgurl", userImage);
                        i.putExtra("logintype", userLogintype);
                        startActivity(i);

                        //이미 가입 후 로그인 시
                    } else if (setUserLoginResultModel.getResultCode().equalsIgnoreCase("201")) {

                        showToast("환영합니다. 로그인되었습니다.");
                        CommonUtil.mSharePrefreences(getApplicationContext(), getString(R.string.share_userId), userIdStr, "guest");
                        Intent i = new Intent(getApplicationContext() , MainActivity.class);
                        startActivity(i);
                        finish();

                    } else {

                        showToastLong(Errorcode.ERROCODE(setUserLoginResultModel.getResultCode()));
                    }

                } else {
                    showToast(Errorcode.ERROCODE("1001"));
                }
                


            }

            @Override
            public void onFailure(Call<SetUserLoginResultModel> call, Throwable t) {

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
