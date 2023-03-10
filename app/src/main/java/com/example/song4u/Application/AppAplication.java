package com.example.song4u.Application;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

import com.buzzvil.buzzad.benefit.BuzzAdBenefit;
import com.buzzvil.buzzad.benefit.BuzzAdBenefitConfig;
import com.buzzvil.buzzad.benefit.presentation.feed.FeedConfig;
import com.example.song4u.R;
import com.kakao.sdk.common.KakaoSdk;

public class AppAplication extends Application {

    public static String androiddid;
    public static String sdkNum;
    public static String osInfo;

    public static AppAplication context;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

    }


    @Override
    public void onCreate() {
        super.onCreate();

        context = this;

        androiddid = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);
        int num = Build.VERSION.SDK_INT;
        sdkNum = String.valueOf(num);
        osInfo = Build.MODEL + "-" + Build.BRAND + "-" + Build.VERSION.SDK_INT;

        /* 카카오 */
        KakaoSdk.init(this, getResources().getString(R.string.kakao_app_key));

        /* 버즈빌 */
        final FeedConfig feedConfig = new FeedConfig.Builder("375356776264846")
                /* 아답터 잠시 삭제 */
                .build();

        final BuzzAdBenefitConfig buzzAdBenefitConfig = new BuzzAdBenefitConfig.Builder(this)
                .setDefaultFeedConfig(feedConfig)
                .build();

        BuzzAdBenefit.init(this, buzzAdBenefitConfig);


    }

    public void globalToast(String message)
    {
            Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
            toast.show();
    }


}
