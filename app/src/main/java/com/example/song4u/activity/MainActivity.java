package com.example.song4u.activity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.buzzvil.buzzad.benefit.BuzzAdBenefit;
import com.buzzvil.buzzad.benefit.core.models.UserProfile;
import com.example.song4u.Adapter.PopupNoticeAdapter;
import com.example.song4u.R;
import com.example.song4u.Util.CommonUtil;
import com.example.song4u.Util.MeasuredViewPager;
import com.example.song4u.databinding.ActivityMainBinding;
import com.example.song4u.network.resultmodel.GetPopupNoticeDataResultModel;
import com.example.song4u.network.resultmodel.GetPopupNoticeResultModel;
import com.example.song4u.network.resultmodel.GetUserInfoResultModel;
import com.example.song4u.network.streaming.StreamingSaveUpService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tnkfactory.ad.Logger;
import com.tnkfactory.ad.TnkSession;

import java.util.ArrayList;

public class MainActivity extends Season2BaseActivity {

    private PopupNoticeAdapter mAdapter;
    private ArrayList<GetPopupNoticeDataResultModel> mArrayList;
    private MeasuredViewPager noticeViewPager;

    private ActivityMainBinding binding;

    private GetUserInfoResultModel getUserInfoResultModel;
    private GetPopupNoticeResultModel getPopupNoticeResultModel;

    final static CommonUtil util = new CommonUtil();

    // 백버튼 2번 누르면 종료
    private long backpressedTime = 0;

    private StreamingSaveUpService streamingSaveUpService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_all, R.id.navigation_user, R.id.navigation_home, R.id.navigation_point, R.id.navigation_store)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        initBuzbill();
        initTnk();
        initStreaming();

        Drawable dr = getResources().getDrawable(R.drawable.horiicon);
        Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
        Drawable d = new BitmapDrawable(getResources(),
                Bitmap.createScaledBitmap(bitmap, 127, 32, true));


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
        if (streamingSaveUpService.isOn()) {
            streamingSaveUpService.forcedStreamingVote();
        }
        if (System.currentTimeMillis() > backpressedTime + 2000) {
            backpressedTime = System.currentTimeMillis();
            Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
        } else if (System.currentTimeMillis() <= backpressedTime + 2000) {
            super.finish();
        }
        //super.finish();
    }

    public void initBuzbill() {

        final UserProfile.Builder builder = new UserProfile.Builder(BuzzAdBenefit.getUserProfile());
        final UserProfile userProfile = builder
                .userId(getUserId())
                .gender(UserProfile.Gender.MALE) //남성 사용자
                .birthYear(1985)
                .build();
        BuzzAdBenefit.setUserProfile(userProfile);

    }


    public void initTnk() {

        Logger.enableLogging(false);
        // 유저 식별 값 설정
        TnkSession.setUserName(this, CommonUtil.mSharePrefreences(this, getString(R.string.share_userId), null, "guest"));
        // 실행형 광고
        TnkSession.applicationStarted(this);
        // COPPA 설정 (true - ON / false - OFF)
        TnkSession.setCOPPA(this, false);

    }

    public void initStreaming() {

        //if (streamingSaveUpService == null) {
            streamingSaveUpService = StreamingSaveUpService.build();
        //}

    }

}