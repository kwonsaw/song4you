package com.example.song4u.activity;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.song4u.R;
import com.example.song4u.Util.Errorcode;
import com.example.song4u.databinding.PointnewsactivityBinding;
import com.example.song4u.network.NetworkManager;
import com.example.song4u.network.resultmodel.SetPointNewsResultModel;
import com.tnkfactory.ad.AdItem;
import com.tnkfactory.ad.BannerAdView;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PointNewsActivity extends Season2BaseActivity {

    private PointnewsactivityBinding binding;

    private SetPointNewsResultModel setPointNewsResultModel;

    private String mUrl, title, sourceId;
    private WebView webview;
    private WebSettings mWebSettings;
    private ProgressBar Progress;

    private CountDownTimer DownTimer;
    private NewsReadTimer newsReadTimer;

    private boolean TimerState = false;

    private long setTime = 16000;

    private boolean isWebViewFinish = false;
    private boolean isFivetime = false;
    private boolean saveYn = false;
    private boolean isScroll = false;

    public static final String INTENT_PROTOCOL_START = "intent:";
    public static final String INTENT_PROTOCOL_INTENT = "#Intent;";
    public static final String INTENT_PROTOCOL_END = ";end;";
    public static final String GOOGLE_PLAY_STORE_PREFIX = "market://details?id=";
    public static final String COUPANG_PREFIX = "coupang://home?src=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = PointnewsactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mUrl = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");
        sourceId = getIntent().getStringExtra("sourceId");
        init();
        initActionBar();
        iniWebview();
        initTnk();
        initWebViewListener();
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

        if (DownTimer != null){
            DownTimer.cancel();
            DownTimer = null;
        }

        super.onDestroy();

    }

    @Override
    public void finish() {
        if (!saveYn) {
            show();
        } else if(webview.canGoBack()) {
            webview.goBack();
        } else {
            isWebViewFinish = false;
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            super.finish();
        }

    }

    public void finish1() {

        isWebViewFinish = false;
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        super.finish();

    }

    /**
     * 뒤로가기 종료
     */
    @Override
    public void onBackPressed() {
        if(webview.canGoBack()){
            webview.goBack();
        }else{
            isWebViewFinish = false;
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                if (!saveYn) {
                    show();
                } else if(webview.canGoBack()) {
                    webview.goBack();
                } else {
                    isWebViewFinish = false;
                    finish();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void show()
    {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("잠깐만요!");
        builder.setMessage("스크롤하여 기사 내용을 확인해야 적립이 완료됩니다.");
        builder.setPositiveButton("적립받기",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.setNegativeButton("다음에 할래요",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        finish1();

                    }
                });
        builder.show();

    }

    private void init(){

        DownTimer =new CountDownTimer(setTime , 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int value = (int)millisUntilFinished/1000;
                Progress.setProgress(100 - ((100 * (value*1000))/ Long.valueOf(setTime).intValue()));

            }
            @Override
            public void onFinish() {

                isFivetime = true;
                if (isWebViewFinish && isScroll && !saveYn) {
                    //setpointnewsmoney();
                    setPointNews(sourceId,"mobfeed");
                }
                binding.pointnewsDetailTop.setVisibility(View.GONE);//로딩 후 프로그래스바 사라짐
            }
        };

    }

    private void initActionBar() {

        mBtn.setVisibility(View.VISIBLE);
        TextCount.setVisibility(View.GONE);
        mTitle.setVisibility(View.VISIBLE);
        alarm.setVisibility(View.GONE);
        mIcon.setVisibility(View.GONE);
        mTitle.setText("뉴스 적립");

        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(mActionBarBackgroundDrawable);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(ActionbarView);
        actionBar.setElevation(0);
    }

    // TNK 띠배너
    public void initTnk() {

        LinearLayout bannerAdLayout = (LinearLayout) findViewById(R.id.tnkAdView);
        BannerAdView bannerAdView = new BannerAdView(PointNewsActivity.this, "song4you_banner");
        bannerAdLayout.addView(bannerAdView);

        bannerAdView.setListener(adListener);

        // 배너 광고 로드
        bannerAdView.load();

    }

    private com.tnkfactory.ad.AdListener adListener = new com.tnkfactory.ad.AdListener() {
        /**
         * 광고 처리중 오류 발생시 호출됨
         *
         * @param adItem 이벤트 대상이되는 AdItem 객체
         * @param error AdError
         */
        @Override
        public void onError(AdItem adItem, com.tnkfactory.ad.AdError error) {

            Log.e("kwonsaw","onError");

            //bannerOnhide();
            binding.tnkAdView.setVisibility(View.GONE);

        }

        /**
         * 광고 load() 후 광고가 도착하면 호출됨
         *
         * @param adItem 이벤트 대상이되는 AdItem 객체
         */
        @Override
        public void onLoad(AdItem adItem) {
            Log.e("kwonsaw","onLoad");
            binding.tnkAdView.setVisibility(View.VISIBLE);

        }

        /**
         * 광고 화면이 화면이 나타나는 시점에 호출된다.
         *
         * @param adItem 이벤트 대상이되는 AdItem 객체
         */
        @Override
        public void onShow(AdItem adItem) {
            Log.e("kwonsaw","onShow");

            //bannerOnhide();
            binding.tnkAdView.setVisibility(View.VISIBLE);

        }


        /**
         * 광고 클릭시 호출됨
         * 광고 화면은 닫히지 않음
         *
         * @param adItem 이벤트 대상이되는 AdItem 객체
         */
        @Override
        public void onClick(AdItem adItem) {


        }
    };

    /* 적립 안내 팝업 */

    public void savePopup(String pText) {

        View view = getLayoutInflater().inflate(R.layout.savepopupview, null);
        Dialog dilaog01= new Dialog(PointNewsActivity.this);
        dilaog01.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dilaog01.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dilaog01.setContentView(view);

        TextView pTextView = (TextView) view.findViewById(R.id.savetext);
        pTextView.setText(pText+"P 적립되었습니다.");
        Button closeBtn = (Button) view.findViewById(R.id.close);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dilaog01.dismiss();
            }
        });

        dilaog01.show();

    }

    public void iniWebview() {

        Progress = binding.pointnewsDetailProgress;
        Progress.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.purple_200), PorterDuff.Mode.SRC_IN);
        webview = binding.pointnewsDetailWeb;


        webview.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
                return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
            }
        });

        mWebSettings = webview.getSettings(); //세부 세팅 등록
        mWebSettings.setJavaScriptEnabled(true); // 웹페이지 자바스클비트 허용 여부
        mWebSettings.setSupportMultipleWindows(false); // 새창 띄우기 허용 여부
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(false); // 자바스크립트 새창 띄우기(멀티뷰) 허용 여부
        mWebSettings.setLoadWithOverviewMode(true); // 메타태그 허용 여부
        mWebSettings.setUseWideViewPort(true); // 화면 사이즈 맞추기 허용 여부
        mWebSettings.setSupportZoom(false); // 화면 줌 허용 여부
        mWebSettings.setBuiltInZoomControls(false); // 화면 확대 축소 허용 여부
        mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); // 컨텐츠 사이즈 맞추기
        mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 브라우저 캐시 허용 여부
        mWebSettings.setDomStorageEnabled(true); // 로컬저장소 허용 여부
        mWebSettings.setTextZoom(100);

        webview.clearCache(true);
        webview.clearHistory();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.setAcceptThirdPartyCookies(webview, true);
        }

        webview.loadUrl(mUrl);

    }

    private void initWebViewListener() {

        webview.setWebViewClient(new WebViewClient() {

                                     public boolean shouldOverrideUrlLoading(WebView view, String url) {

                                         if (url.startsWith(INTENT_PROTOCOL_START)) {
                                             final int customUrlStartIndex = INTENT_PROTOCOL_START.length();
                                             final int customUrlEndIndex = url.indexOf(INTENT_PROTOCOL_INTENT);
                                             if (customUrlEndIndex < 0) {
                                                 return false;
                                             } else {
                                                 final String customUrl = url.substring(customUrlStartIndex, customUrlEndIndex);
                                                 try {
                                                     getApplicationContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(customUrl)).addFlags(FLAG_ACTIVITY_NEW_TASK));
                                                 } catch (ActivityNotFoundException e) {
                                                     showToast("앱이 설치되어 있지 않습니다.");
                                                 }
                                                 return true;
                                             }
                                         } else if (url.startsWith(GOOGLE_PLAY_STORE_PREFIX)|| url.startsWith(COUPANG_PREFIX)) {
                                             try {
                                                 getApplicationContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)).addFlags(FLAG_ACTIVITY_NEW_TASK));
                                             } catch (ActivityNotFoundException e) {
                                                 //showToast("앱이 설치되어 있지 않습니다.");
                                             }
                                             return true;
                                         } else if (url.startsWith("naversearchapp://")) { // 백버튼 클릭시 err_url 오류
                                             try {
                                                 getApplicationContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)).addFlags(FLAG_ACTIVITY_NEW_TASK));
                                             } catch (ActivityNotFoundException e) {
                                                 //showToast("앱이 설치되어 있지 않습니다.");
                                             }
                                             return true;
                                         }else {
                                             view.loadUrl(url);
                                             return false;
                                         }
                                     }

                                     public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon) {
                                         super.onPageStarted(view, url, favicon);
                                         newsReadTimer = new NewsReadTimer(3000, 1000);
                                         newsReadTimer.start();

                                     }

                                     public void onPageFinished(WebView view, String url) {
                                         super.onPageFinished(view, url);
                                         isWebViewFinish = true;
                                     }

                                 }
        );

        webview.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {

                int scrollY = webview.getScrollY(); // For ScrollView
                int scrollX = webview.getScrollX(); // For HorizontalScrollView

                float contentHeight = webview.getContentHeight() * webview.getScaleY();
                float total = contentHeight * getResources().getDisplayMetrics().density - webview.getHeight();
                // on some devices just 1dp was missing to the bottom when scroll stopped, so we subtract it to reach 1
                float percent = Math.min(scrollY / (total - getResources().getDisplayMetrics().density), 1);


                if (isWebViewFinish && percent > 0.05 && !TimerState) {
                    TimerState = true;
                    isScroll = true;
                    if (DownTimer != null) {
                        DownTimer.start();
                    }

                }

            }
        });

        webview.setWebChromeClient(new WebChromeClient() {//웹뷰에 webchromeclient를 지정

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if ( Progress != null) {
                    //Progress.setProgress(newProgress);
                }
            }

        });

    }

    private class NewsReadTimer extends CountDownTimer {
        public NewsReadTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            isWebViewFinish = true;
        }
    }


    /**
     /* 뉴스 적립
     **/

    public void setPointNews(String getSourceId, String getUid){
        saveYn = true;

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor()).build();

        NetworkManager manager = new NetworkManager();
        manager.setPointNews(getUserId(),  getVersion(), getUid, getSourceId,client, new Callback<SetPointNewsResultModel>() {
            @Override
            public void onResponse(Call<SetPointNewsResultModel> call, Response<SetPointNewsResultModel> response) {
                setPointNewsResultModel = response.body();

                if (response.isSuccessful()) {

                    if (setPointNewsResultModel.getResultCode().equalsIgnoreCase("200")) {

                        savePopup(setPointNewsResultModel.getSavemoney());

                    } else if (setPointNewsResultModel.getResultCode().equalsIgnoreCase("100")) {

                        showToast("적립 가능 시간이 아닙니다.");

                    } else {

                    }

                } else {

                    showToast(Errorcode.ERROCODE("1001"));
                }
            }

            @Override
            public void onFailure(Call<SetPointNewsResultModel> call, Throwable t) {
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
