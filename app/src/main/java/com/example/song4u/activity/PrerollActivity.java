package com.example.song4u.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.dawin.DawinVideoAd;
import com.example.song4u.R;
import com.example.song4u.Util.Errorcode;
import com.example.song4u.Util.ProgressWheel;
import com.example.song4u.databinding.PrerollactivityBinding;
import com.example.song4u.network.NetworkManager;
import com.example.song4u.network.resultmodel.SetPrerollResultModel;
import com.gomcorp.vrix.ExtensionIconAction;
import com.gomcorp.vrix.VrixAdCallback;
import com.gomcorp.vrix.VrixAdItem;
import com.gomcorp.vrix.VrixInitCallback;
import com.gomcorp.vrix.VrixPlayer;
import com.mmc.man.AdConfig;
import com.mmc.man.AdEvent;
import com.mmc.man.AdListener;
import com.mmc.man.AdResponseCode;
import com.mmc.man.data.AdData;
import com.mmc.man.view.AdManView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrerollActivity extends Season2BaseActivity implements MediaPlayer.OnPreparedListener {

    private SetPrerollResultModel setPrerollResultModel;

    private PrerollactivityBinding binding;

    private Context mContext;

    private VideoView cpvc_video;
    private MediaController controller;
    private RelativeLayout cpvc_playtop;
    private String Domain;
    private String getSaveMoney , getVideoUrl, getLoadingtime;
    private String helpstr = "%sP 적립되었습니다.";
    private String helpstr1 = "[%s]캠페인에 %s원이 기부되고, %sP 적립되었습니다.";
    private TextView SaveAlarm;
    private ProgressWheel cpvc_progress;

    private AdManView movieView = null;
    private RelativeLayout movieArea;
    private LinearLayout rootVideo;
    private Handler mHandler = new Handler();
    //AdVideoPlayer manPlayer = null;
    private boolean bActivate = true;
    private boolean SaveState = false;
    private int mTime;
    private CountDownTimer timer;
    private  int i = 0;
    private boolean isMezzoCpvc = true;

    /* 다윈 */
    private DawinVideoAd mDawinVideoAd;
    private boolean isDawinCpvc = true;

    /* 브릭스 */
    /* 브릭스 2.0 버젼 피카소 라이브러리 오류 (force 2.5.2 하면 해결) */
    private VrixPlayer vrixPlayer;
    private ViewGroup pnlPlayer;

    private String VRIX_URL = "https://ads.vrixon.com/vast/vast_mable.vrix?invenid=OYBNH&gender=1&age=30";
    private String VuserAge, VuserSex;
    //private static final String VRIX_URL = "https://devads.vrixon.com/vast/vast.vrix?invenid=KHLOC";
    //private static final String VRIX_URL = "https://devads.vrixon.com/vast/vast.vrix?invenid=PEFOC";  //광고가 없는경우
    //private static final String VRIX_URL = "https://devads.vrixon.com/vast/vast.vrix?invenid=XXXXXX";  // 잘못된 URL

    private String SourceID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = PrerollactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mContext = getApplicationContext();
        controller = new MediaController(this);

        getSaveMoney = getIntent().getStringExtra("savemoney");
        getVideoUrl = getIntent().getStringExtra("videolink");
        getLoadingtime = getIntent().getStringExtra("loadingtime");

        initActionBar();
        initView();

    }

    public void initView() {

        cpvc_video = (VideoView)findViewById(R.id.cpvc_video);
        //manPlayer = (AdVideoPlayer)findViewById(R.id.ad_player);
        SaveAlarm = (TextView)findViewById(R.id.cpvc_savealarm);
        cpvc_playtop = (RelativeLayout)findViewById(R.id.cpvc_playtop);
        cpvc_progress = (ProgressWheel)findViewById(R.id.cpvc_progress);
        rootVideo = (LinearLayout) findViewById(R.id.layout_gallery);
        //movieArea = (RelativeLayout)findViewById(R.id.movieArea);
        mDawinVideoAd = (DawinVideoAd) findViewById(R.id.player);
        //vrixManager = new VrixManager();
        pnlPlayer = (ViewGroup) findViewById(R.id.pnl_player);

        rootVideo.removeAllViews();

        mezzoStart("801119");
        //dawincpvc();
        //startVrix();
        //perplStart();

    }


    /* 브릭스 */
    private void startVrix() {
        //progress.setVisibility(View.VISIBLE);
        //Log.e("startVrix","startVrix");

        Log.e("kwonsaw","startVrix");

        if (vrixPlayer != null) {
            vrixPlayer.release();
            vrixPlayer = null;
        }

        vrixPlayer = new VrixPlayer(this, pnlPlayer, VRIX_URL);
        vrixPlayer.init(new VrixInitCallback() {
            @Override
            public void onInitialized() {
                //progress.setVisibility(View.GONE);
                pnlPlayer.setVisibility(View.VISIBLE);
                play();
            }

            @Override
            public void onFailed() {
                //progress.setVisibility(View.GONE);
                //Toast.makeText(SampleActivityV2.this, "Init 오류", Toast.LENGTH_SHORT).show();
                pnlPlayer.setVisibility(View.GONE);
                mezzoStart("803366");
                //AdError(true);

            }
        });

    }

    private void play() {


        vrixPlayer.playPreroll(new VrixAdCallback() {
            @Override
            public void onError() {
                // 광고재생실패
                // 메인영상 재생 시작
                //Toast.makeText(SampleActivityV2.this, "재생 실패", Toast.LENGTH_SHORT).show();
                Log.e("kwonsaw","VrixPlayer_onError");
                pnlPlayer.setVisibility(View.GONE);
                //AdError(true);
                if ( timer != null) {
                    timer.cancel();
                    timer = null;
                }
                mezzoStart("803366");
                //Log.e("startVrix","play_onFail");


            }

            @Override
            public void onAdBreakStarted() {
            }

            @Override
            public void onAdStarted(VrixAdItem vrixAdItem) {
                //Toast.makeText(SampleActivityV2.this, vrixAdItem.title + " 재생시작", Toast.LENGTH_SHORT).show();
                SourceID = "brixCPVC";
                pnlPlayer.setVisibility(View.VISIBLE);
                Save5Scheck();

            }

            @Override
            public void onAdCompleted(VrixAdItem vrixAdItem) {
                //Toast.makeText(SampleActivityV2.this, vrixAdItem.title + " 재생완료", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdBreakCompleted() {
                // 광고재생완료
                // 메인영상 재생 시작
                //Toast.makeText(SampleActivityV2.this, "광고 재생 완료", Toast.LENGTH_SHORT).show();
                SourceID = "brixCPVC";
                AdOk();
                pnlPlayer.setVisibility(View.GONE);


            }
            @Override
            public void onAdClicked(VrixAdItem vrixAdItem) {
            }

            @Override
            public void onAdSkipped(VrixAdItem vrixAdItem) {
            }

            @Override
            public void onExtensionIconClick(ExtensionIconAction d) {
                // Noting To do, 무시해주세요.
            }
        });

    }

    /* 메조미디어 */
    public void mezzoStart(String sectionid){

        Log.e("kwonsaw","mezzoStart");
        int num = Integer.parseInt(sectionid);

        /* 테스트 */
        int pub = 100;
        int media = 200;
        int sec = 804330;
        //804822 엔드카드
        //804821

        //1240 30478 num 라이브

        AdData adData = new AdData();
        String appName = "appletree2";
        int width = 320;//(int) MZDisplayUtil.convertPixelsToDp(c, h); 샘플앱에 있는 메서드입니다. 사용하지마시기 바랍니다.
        int height = 240;//(int) MZDisplayUtil.convertPixelsToDp(c, h); 샘플앱에 있는 메서드입니다. 사용하지마시기 바랍니다.
        adData.major("movie", AdConfig.API_MOVIE, 1240, 30478, num, "https://play.google.com/store/apps/details?id=com.example.song4u&hl=ko", "com.example.song4u", appName, width, height);
        //유저 나이 레벨 필수
        adData.setUserAgeLevel(1);
        //광고 설정값의 옵션
        //adData.minor("0", "40", "mezzo", "geon-jin.mun@cj.net");
        //무비광고의 세팅값
        adData.movie(AdConfig.USED, AdConfig.NOT_USED, AdConfig.NOT_USED, AdConfig.NOT_USED, AdConfig.USED, AdConfig.USED, AdConfig.USED);
        //닫기버튼 사용 여부
        adData.setIsCloseShow(AdConfig.USED);
        //퍼미션 사용 여부
        adData.isPermission(AdConfig.NOT_USED, AdConfig.NOT_USED);
        movieView = new AdManView(PrerollActivity.this);
        movieView.setData(adData, new AdListener() {
            @Override
            public void onAdSuccessCode(Object v, String id, final String type, final String status, String jsonDataString) {
                ((Activity) PrerollActivity.this).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (AdResponseCode.Status.SUCCESS.equals(status)) {
                            if(movieView.getParent() != null){
                                ((ViewGroup) movieView.getParent()).removeView(movieView);
                            }
                            movieView.addBannerView(rootVideo);
                            //Log.e("kwonsaw","onAdSuccessCode");
                        }
                    }
                });
            }

            //광고 호출 실패 에러
            @Override
            public void onAdFailCode(Object v, String id, String type, String status, String jsonDataString) {
                if(movieView!=null){
                    movieView.onDestroy();
                    movieView = null;
                }
                if (isMezzoCpvc) {
                    //isMezzoCpvc = false;
                    //mezzoStart("801119");
                    AdError(true);
                    //dawincpm();
                }else{
                    AdError(true);
                }
                //Log.e("kwonsaw","onAdFailCode");
            }
            //광고 호출 실패 에러(웹뷰 에러)
            @Override
            public void onAdErrorCode(Object v, String id, String type, String status, String failingUrl) {
                if(movieView!=null){
                    movieView.onDestroy();
                    movieView = null;
                }
                if (isMezzoCpvc) {
                    //isMezzoCpvc = false;
                    //mezzoStart("801119");
                    AdError(true);
                    //dawincpm();
                }else{
                    AdError(true);
                }
                //Log.e("kwonsaw","onAdErrorCode");
            }
            //광고에서 발생하는 이벤트
            @Override
            public void onAdEvent(Object v, String id, String type, String status, String jsonDataString) {


                if(AdEvent.Type.CLICK.equals(type)){
                    AdError(false);
                }else if(AdEvent.Type.CLOSE.equals(type)){
                    AdError(false);
                }else if(AdEvent.Type.SKIP.equals(type)){
                    AdError(false);
                }else if(AdEvent.Type.ENDED.equals(type)){

                    if(movieView!=null){
                        Log.e("kwonsaw","ENDED");
                        movieView.onDestroy();
                        movieView = null;
                    }
                    rootVideo.removeAllViews();

                    //AdOk();

                }else if(AdEvent.Type.IMP.equals(type)){

                }else if(AdEvent.Type.START.equals(type)){

                    if (isMezzoCpvc) {
                        SourceID = "mezzo";
                    }else{
                        SourceID = "mezzoBumper";
                    }

                    Save5Scheck();


                }else if(AdEvent.Type.FIRSTQ.equals(type)){

                }else if(AdEvent.Type.MIDQ.equals(type)){

                }else if(AdEvent.Type.THIRDQ.equals(type)){

                }else if(AdEvent.Type.COMPLETE.equals(type)){
                    AdOk();
                }else if(AdEvent.Type.ENDCARD_START.equals(type)){
                    //AdError(false);
                }else if(AdEvent.Type.DESTROY.equals(type)){
                    //AdError(false);
                    if(movieView!=null){
                        Log.e("kwonsaw","DESTROY");
                        movieView.onDestroy();
                        movieView = null;
                    }
                    AdError(false);
                    //rootVideo.removeAllViews();
                }
            }
            // 광고 퍼미션(위치) 설정 요청 이벤트 - 사용자 단말기 설정 페이지에 있는 앱 권한을 받기 위한 이벤트입니다.
            @Override
            public void onPermissionSetting(Object v, String id) {
                // isPermission값이 미사용으로 설정한다면 이벤트가 발생하지 않습니다.
            }
        });
        //요청 메서드
        movieView.request(new Handler());

    }


    private void dawincpvc(){

        if (mDawinVideoAd != null) {

            Log.e("kwonsaw","dawincpvc");

            mDawinVideoAd.setVisibility(View.VISIBLE);
            mDawinVideoAd.stopAd();

            String initData = "{adslotid:" + "6T3JW042FPW" + ", userdata:USERDATA, videotimeout:10000}"; //CPVC
            //String initData = "{adslotid:" + "5OCWMSOTE8YO" + ", userdata:USERDATA, videotimeout:10000}"; //통화
            //String initData = "{adslotid:" + "6T3JW042FQ2" + ", userdata:USERDATA, videotimeout:10000}"; //테스트
            //String initData = "{adslotid:" + "6TYPQQKYYO0" + ", userdata:USERDATA, videotimeout:10000}"; //광고없음
            //String initData = "{adslotid:" + "5O9Q75DR6I3F" + ", userdata:USERDATA, videotimeout:10000}"; //장초수 테스트

            String json = "{" +
                    "\"" + "adslotid" + "\":\"6T1LM606D51\"" +
                    ",\"" + "userdata" + "\":\"USERDATA\"" +
                    ",\"" + "videotimeout" + "\":10000" + "}";
            mDawinVideoAd.setVolume(100);

            mDawinVideoAd.initAd(initData, mDawinVideoAdListener);

        }

    }

    private void dawincpm(){

        if (mDawinVideoAd != null) {
            mDawinVideoAd.stopAd();

            Log.e("kwonsaw","dawincpm");
            //mDawinVideoAd.setVisibility(View.VISIBLE);

            String initData = "{adslotid:" + "6T3JW042FPX" + ", userdata:USERDATA, videotimeout:10000}"; //CPM
            //String initData = "{adslotid:" + "6T1LM606D52" + ", userdata:USERDATA, videotimeout:10000}"; //테스트
            //String initData = "{adslotid:" + "6TYPQQKYYO0" + ", userdata:USERDATA, videotimeout:10000}"; //광고없음

            String json = "{" +
                    "\"" + "adslotid" + "\":\"6T1LM606D51\"" +
                    ",\"" + "userdata" + "\":\"USERDATA\"" +
                    ",\"" + "videotimeout" + "\":10000" + "}";
            mDawinVideoAd.setVolume(100);

            mDawinVideoAd.initAd(initData, mDawinVideoAdListener);
        }


    }

    DawinVideoAd.DawinVideoAdListener mDawinVideoAdListener = new DawinVideoAd.DawinVideoAdListener() {

        @Override
        public void onAdVideoStarted(String key) {

            if(isDawinCpvc){

                SourceID = "dawinCPVC";
            } else {

                SourceID = "dawinCPM";
            }

            android.util.Log.i("Dawin","onAdVideoStarted");

            if (!SaveState) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //getpreroll api에 적용된 초수 후 적립
                        Save5Scheck();
                    }
                }, 1000);
            }
                    // 1초 딜레이를 준 후 시작

        }

        @Override
        public void onAdVideoFirstQuartile(String key) {
            if (SaveState) {
                if (mDawinVideoAd != null) {
                    mDawinVideoAd.stopAd();
                    mDawinVideoAd.setVisibility(View.GONE);
                    videoPlay();
                    //showToast("종료");
                }
            }
            android.util.Log.i("Dawin","onAdVideoFirstQuartile : " + key);
        }

        @Override
        public void onAdVideoMidpoint(String key) {
            android.util.Log.i("Dawin","onAdVideoMidpoint : " + key);
        }

        @Override
        public void onAdVideoThirdQuartile(String key) {
            android.util.Log.i("Dawin","onAdVideoThirdQuartile : " + key);
        }

        @Override
        public void onAdVideoComplete(String key) {
            android.util.Log.i("Dawin","onAdVideoComplete : " + key);
            if (!SaveState) {
                AdOk();
            }
            //mHandler.sendEmptyMessage(MESSAGE_VIDEO_COMPLETE);
        }

        @Override
        public void onAdVideoProgress(String key) {
            android.util.Log.i("Dawin","onAdVideoProgress : " + key);
        }

        @Override
        public void onAdStoped(String key) {
            android.util.Log.i("Dawin","onAdStoped");
            AdError(false);
            //Log.e("Dawin", "onAdStoped");
            //mHandler.sendEmptyMessage(MESSAGE_STOP);
        }

        @Override
        public void onAdSkiped(String key) {
            //android.util.Log.i("Dawin","onAdSkiped");
            AdError(false);
            //mHandler.sendEmptyMessage(MESSAGE_SKIP);
        }

        @Override
        public void onAdLoaded(String key) {
            //android.util.Log.i("Dawin","onAdLoaded");
            if (mDawinVideoAd != null) {
                mDawinVideoAd.startAd();
            }
        }

        @Override
        public void onAdError(String Error, String key) {
            //android.util.Log.i("Dawin","onAdError : " + Error);
            Message msg = new Message();
            //msg.what = MESSAGE_ERROR;
            msg.obj = Error;

            //Log.e("Dawin", "onAdError");
            if (!SaveState) {
                if (isDawinCpvc == true) {
                    isDawinCpvc = false;
                    //803366 범퍼
                    //801119 cpm
                    mezzoStart("801119");

                } else {
                    startVrix();
                }
            } else {
                videoPlay();
            }
            //AdError(true);
            //mHandler.sendMessage(msg);
        }

        @Override
        public void onAdClickThru(String click, String key) {
            //android.util.Log.i("Dawin","onAdClickThru : " + click);
            Message msg = new Message();
            //msg.what = MESSAGE_CLICK;
            msg.obj = click;
            //AdError(false);
            //mHandler.sendMessage(msg);
        }

        @Override
        public void onAdSkippableStateChange(String key) {
            //android.util.Log.i("Dawin","onAdSkippableStateChange : " +  key);
        }

    };

    /**
     * 15초후 적립
     * */
    public void
    Save5Scheck(){
        //Log.e("onAdLoaded",""+SourceID);
        if ( timer != null) {
            timer.cancel();
            timer = null;
        }

        if ( timer == null) {
            i = 0;
            timer = new CountDownTimer(Integer.valueOf(getLoadingtime) , 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    i += 1;
                    handler.sendEmptyMessage(i);
                }

                @Override
                public void onFinish() {
                    i += 1;
                    handler.sendEmptyMessage(i);
                    if ( !TextCount.getText().toString().equalsIgnoreCase("적립") && !SaveState){
                        setPreroll();
                    }
                }
            }.start();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if(!bActivate) {
            //if(manPlayer !=null)
            //    manPlayer.onResume();
        }

        if (mDawinVideoAd != null) {
            mDawinVideoAd.onResume();
        }


        if (vrixPlayer != null) {
            vrixPlayer.resume();
        }


    }

    @Override
    public void onPause() {

        super.onPause();

        bActivate = false;
        //if(manPlayer !=null)
        //    manPlayer.onPause();

        if (mDawinVideoAd != null) {
            mDawinVideoAd.onPause();
        }


        if(movieView!=null){
            Log.e("kwonsaw","onPause");
            movieView.onDestroy();
            movieView = null;
        }


        if (vrixPlayer != null) {
            vrixPlayer.pause();
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mDawinVideoAd != null) {
            mDawinVideoAd.onDestroy();
            mDawinVideoAd.setVisibility(View.GONE);
            mDawinVideoAd = null;
        }

        if ( cpvc_video != null){
            cpvc_video = null;
        }

        if ( timer != null)
            timer.cancel();

        cpvc_playtop.setVisibility(View.GONE);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (mDawinVideoAd != null) {
            mDawinVideoAd.stopAd();
        }

        if (vrixPlayer != null) {
            vrixPlayer.stop();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
        }
    }

    @Override
    public void onConfigurationChanged (Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        switch(newConfig.orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                break;

        }
    }

    private void initActionBar() {

        mBtn.setVisibility(View.VISIBLE);
        TextCount.setVisibility(View.VISIBLE);
        mTitle.setVisibility(View.VISIBLE);
        alarm.setVisibility(View.GONE);
        mIcon.setVisibility(View.GONE);
        mTitle.setText("영상 적립");

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
                if (mDawinVideoAd != null) {
                    mDawinVideoAd.onDestroy();
                    Log.e("kwonsaw","SaveMoney_stopAd");
                    //mDawinVideoAd.onDestroy();
                }
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 광고 수신 실패시*/
    public void AdError(boolean show){
        //Log.e("AdError","AdError"+SourceID);
        if (SaveState == true){
            return;
        }
        if ( timer != null)
            timer.cancel();
        //SaveState = true;
        if ( !TextCount.getText().toString().equalsIgnoreCase("적립") )
            TextCount.setVisibility(View.GONE);

        if ( show ) {
            String msg = "참여 가능한 광고 수량이 초과 되었습니다.\n잠시 후 다시 확인해주세요.\n매시간 정시에 더욱 많은 광고적립을 하실 수 있습니다.";
            Toast toast;
            toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
            int offsetX = 0;
            int offsetY = 0;
            toast.setGravity(Gravity.CENTER, offsetX, offsetY);
            toast.show();
        }

        if (mDawinVideoAd != null) {
            mDawinVideoAd.onDestroy();
            mDawinVideoAd.setVisibility(View.GONE);
            mDawinVideoAd = null;
        }

        cpvc_progress.setVisibility(View.VISIBLE);
        cpvc_playtop.setVisibility(View.VISIBLE);


        //Log.e("cpvc_video","cpvc_video");
        if (cpvc_video != null) {

            //String url = getVideoUrl;
            //String url = util.mSharePrefreences(getApplicationContext(), getString(R.string.share_geturl), null, "http://");
            String videoRootPath = "android.resource://" + getPackageName() + "/raw/foodsmiles201711";

            //Log.e("cpvc_video",""+url);
            cpvc_video.setMediaController(controller);
            cpvc_video.setVideoURI(Uri.parse(videoRootPath));
            //cpvc_video.setVideoURI(Uri.parse(getVideoUrl));
            cpvc_video.resume();

            cpvc_video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                // 동영상 재생준비가 완료된후 호출되는 메서드
                @Override
                public void onPrepared(MediaPlayer mp) {
                    // TODO Auto-generated method stub\
                    //Log.e("onPrepared", "onPrepared");
                    cpvc_progress.setVisibility(View.GONE);
                    cpvc_video.start();
                }
            });

            cpvc_video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override

                public void onCompletion(MediaPlayer mp) {

                    cpvc_progress.setVisibility(View.GONE);
                    // TODO Auto-generated method stub

                }

            });

            cpvc_video.setOnErrorListener(new MediaPlayer.OnErrorListener() {

                public boolean onError(MediaPlayer mp, int what, int extra) {

                    try {

                        cpvc_progress.setVisibility(View.GONE);
                        //Log.e("onError", "onError");
                        if (mp.isPlaying()) {
                            mp.stop();
                            mp.release();
                        }

                    } catch (Exception e) {

                        e.printStackTrace();

                    }

                    return true;

                }

            });

        }


    }

    public void videoPlay() {

        cpvc_progress.setVisibility(View.VISIBLE);
        cpvc_playtop.setVisibility(View.VISIBLE);

        //Log.e("cpvc_video","cpvc_video");
        if (cpvc_video != null) {

            String videoRootPath = "android.resource://" + getPackageName() + "/raw/foodsmiles201711";

            cpvc_video.setMediaController(controller);
            cpvc_video.setVideoURI(Uri.parse(videoRootPath));
            //cpvc_video.setVideoURI(Uri.parse(getVideoUrl));
            cpvc_video.resume();

            cpvc_video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                // 동영상 재생준비가 완료된후 호출되는 메서드
                @Override
                public void onPrepared(MediaPlayer mp) {
                    // TODO Auto-generated method stub\
                    //Log.e("onPrepared", "onPrepared");
                    cpvc_progress.setVisibility(View.GONE);
                    cpvc_video.start();
                }
            });

            cpvc_video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override

                public void onCompletion(MediaPlayer mp) {

                    cpvc_progress.setVisibility(View.GONE);
                    // TODO Auto-generated method stub

                }

            });

            cpvc_video.setOnErrorListener(new MediaPlayer.OnErrorListener() {

                public boolean onError(MediaPlayer mp, int what, int extra) {

                    try {

                        cpvc_progress.setVisibility(View.GONE);
                        //Log.e("onError", "onError");
                        if (mp.isPlaying()) {
                            mp.stop();
                            mp.release();
                        }

                    } catch (Exception e) {

                        e.printStackTrace();

                    }

                    return true;

                }

            });

        }
    }

    /**광고 시청완료 후*/
    public void AdOk(){
        //Log.e("AdOk","AdOk");

        if ( timer != null)
            timer.cancel();

        if ( !TextCount.getText().toString().equalsIgnoreCase("적립") && !SaveState){
            // 동영상 시청 완료 후 적립이 안되어 있다면 적립 시도
            setPreroll();
        }

        cpvc_progress.setVisibility(View.VISIBLE);
        cpvc_playtop.setVisibility(View.VISIBLE);


        if (vrixPlayer != null) {
            vrixPlayer.stop();
        }


        if ( cpvc_video != null) {

            //String url = getVideoUrl;
            String videoRootPath = "android.resource://" + getPackageName() + "/raw/foodsmiles201711";
            //Log.e("cpvc_video",""+url);

            cpvc_video.setMediaController(controller);
            cpvc_video.setVideoURI(Uri.parse(videoRootPath));
            //cpvc_video.setVideoURI(Uri.parse(getVideoUrl));
            cpvc_video.resume();

            cpvc_video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                // 동영상 재생준비가 완료된후 호출되는 메서드
                @Override
                public void onPrepared(MediaPlayer mp) {
                    // TODO Auto-generated method stub
                    //Log.e("onPrepared", "onPrepared");
                    cpvc_video.start();
                    cpvc_progress.setVisibility(View.GONE);
                }
            });

            cpvc_video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override

                public void onCompletion(MediaPlayer mp) {

                    cpvc_progress.setVisibility(View.GONE);
                    // TODO Auto-generated method stub

                }

            });


            cpvc_video.setOnErrorListener(new MediaPlayer.OnErrorListener() {

                public boolean onError(MediaPlayer mp, int what, int extra) {

                    try {

                        cpvc_progress.setVisibility(View.GONE);
                        if (mp.isPlaying()) {
                            mp.stop();
                            mp.release();
                        }

                    } catch (Exception e) {

                        e.printStackTrace();

                    }

                    return true;

                }

            });

        }

        //cpvc_video.setOnPreparedListener(this);
        //cpvc_video.start();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                if (mp.isPlaying() && percent >= 0) {
                    cpvc_progress.setVisibility(View.GONE);
                }
            }
        });
    }

    //프리롤 광고 적립
    public void setPreroll(){

        /* 다윈 cpvc 적립 후 한 번 더 호출  */
        /*
        SaveState = true;

        // 다윈 VTR
        if ("dawinCPVC".equals(SourceID)){
            if (mDawinVideoAd != null) {
                mDawinVideoAd.stopAd();
            }
            dawincpvc();
        }
         */

        NetworkManager manager = new NetworkManager();
        manager.setPreroll(getUserId(),  getVersion(), getSaveMoney, SourceID, new Callback<SetPrerollResultModel>() {
            @Override
            public void onResponse(Call<SetPrerollResultModel> call, Response<SetPrerollResultModel> response) {
                setPrerollResultModel = response.body();

                if (response.isSuccessful()) {

                    if (setPrerollResultModel.getResultCode().equalsIgnoreCase("200")) {

                        binding.cpvcSavealarm.setVisibility(View.VISIBLE);
                        binding.cpvcSavealarm.setText(""+setPrerollResultModel.getSavemoney()+"P 적립되었습니다.");
                        //showToast(""+setPrerollResultModel.getSavemoney()+"P 적립되었습니다.");

                        if ( timer != null)
                            timer.cancel();

                        TextCount.setText("적립");
                        TextCount.setTextSize(10);

                    }

                } else {
                   showToast(Errorcode.ERROCODE("1001"));
                }
            }

            @Override
            public void onFailure(Call<SetPrerollResultModel> call, Throwable t) {
                showToast(Errorcode.ERROCODE("1001"));
            }
        });

    }


    Handler handler = new Handler() {
        public void handleMessage(Message message) {
            try {
                TextCount.setText(message.what + "");
                //Log.e("messagewhat","messagewhat"+message.what);

                if ( message.what == mTime ){
                    if ( !TextCount.getText().toString().equalsIgnoreCase("적립") && !SaveState){
                        setPreroll();
                    }
                }
            } catch (Exception e) {}
        }
    };


}