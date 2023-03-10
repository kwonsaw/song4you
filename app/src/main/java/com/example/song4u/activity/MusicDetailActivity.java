package com.example.song4u.activity;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.song4u.Adapter.PlayuserRecyclerAdapter;
import com.example.song4u.Adapter.RankRecyclerAdapter;
import com.example.song4u.Application.AppAplication;
import com.example.song4u.R;
import com.example.song4u.Util.CommonUtil;
import com.example.song4u.Util.Errorcode;
import com.example.song4u.Util.RoundedCornersTransformation;
import com.example.song4u.Util.SFUAlertDialog;
import com.example.song4u.Util.SFUToast;
import com.example.song4u.databinding.MusicdetailactivityBinding;
import com.example.song4u.network.NetworkManager;
import com.example.song4u.network.resultmodel.GetCheckMusicResultModel;
import com.example.song4u.network.resultmodel.GetMusicInfoDataResultModel;
import com.example.song4u.network.resultmodel.GetMusicInfoResultModel;
import com.example.song4u.network.resultmodel.GetUserInfoDataResultModel;
import com.example.song4u.network.resultmodel.SetMusicLikeResultModel;
import com.example.song4u.network.resultmodel.SetMusicPointResultModel;
import com.example.song4u.network.resultmodel.SetSupportMusicResultModel;
import com.example.song4u.network.streaming.StreamingSaveServiceResultListener;
import com.example.song4u.network.streaming.StreamingSaveUpService;
import com.example.song4u.network.streaming.StreamingSponsorType;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.kakao.sdk.common.util.KakaoCustomTabsClient;
import com.kakao.sdk.share.ShareClient;
import com.kakao.sdk.share.WebSharerClient;
import com.kakao.sdk.template.model.Content;
import com.kakao.sdk.template.model.FeedTemplate;
import com.kakao.sdk.template.model.Link;
import com.kakao.sdk.user.UserApiClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.glide.transformations.BlurTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MusicDetailActivity extends Season2BaseActivity implements StreamingSaveServiceResultListener, View.OnClickListener {

    private MusicdetailactivityBinding binding;

    private GetMusicInfoResultModel getMusicInfoResultModel;
    private SetMusicLikeResultModel setMusicLikeResultModel;
    private SetMusicPointResultModel setMusicPointResultModel;
    private GetCheckMusicResultModel getCheckMusicResultModel;
    private SetSupportMusicResultModel setSupportMusicResultModel;

    private StreamingSaveUpService streamingSaveUpService; //= StreamingSaveUpService.build().resultListener(this).register();
    private String melonScheme = "melonapp://play?cid=%s&ctype=1";
    private String genieScheme = "igeniesns://detail?landingtype=06&landingtarget=%s";
    private StreamingSponsorType sponsorType;// = StreamingSponsorType.MELON;

    private int maxCnt = 1;

    private boolean isadd = false;
    private boolean isremove = false;

    private String playcntText = "1", removeMoneyText = "50";
    private String shareStr, imageUrl;

    private RecyclerView uRecyclerView;
    private PlayuserRecyclerAdapter playuserRecyclerAdapter;
    private ArrayList<GetUserInfoDataResultModel> playUserList;

    private RecyclerView rRecyclerView;
    private RankRecyclerAdapter rankRecyclerAdapter;
    private ArrayList<GetUserInfoDataResultModel> rankList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MusicdetailactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        initActionBar();
        
        getMusicInfo(getIntent().getStringExtra("musicid"),getIntent().getStringExtra("songid") );

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

        binding.likebutton.setOnClickListener(this);
        binding.playmusic.setOnClickListener(this);
        binding.supportmusic.setOnClickListener(this);
        binding.helpimg.setOnClickListener(this);
        binding.replybutton.setOnClickListener(this);
        binding.rText.setOnClickListener(this);
        //binding.add.setOnClickListener(this);
        //binding.remove.setOnClickListener(this);
        binding.add.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                handler1_up.post(runnable1_up);
                Log.e("kwonsaw","onLongClick");
                return false;
            }
        });

        binding.add.setOnTouchListener(new View.OnTouchListener() { //터치 이벤트 리스너 등록(누를때)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) { //눌렀을 때 동작
                    setAddPlay();
                    isremove = false;
                }
                if (event.getAction() == MotionEvent.ACTION_UP) { //뗐을 때 동작
                    handler1_up.removeCallbacks(runnable1_up);
                }
                return false;
            }
        });

        binding.remove.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {    // 길게 누르면 동작
                handler1_down.post(runnable1_down);
                return false;
            }
        });

        binding.remove.setOnTouchListener(new View.OnTouchListener() { //터치 이벤트 리스너 등록(누를때)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) { //눌렀을 때 동작
                    setRemovePlay();
                    isadd = false;
                }
                if (event.getAction() == MotionEvent.ACTION_UP) { //뗐을 때 동작
                    handler1_down.removeCallbacks(runnable1_down);
                }
                return false;
            }
        });

        binding.sharebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(MusicDetailActivity.this)) {
                    kakaoLink();
                } else {
                    webKakaoLink();
                }

            }
        });

    }

    private Handler handler1_up = new Handler();
    private Runnable runnable1_up = new Runnable() {
        @Override
        public void run() {

           if (!isadd) {
               setAddPlay();
               handler1_up.postDelayed(this, 100);
           }
        }
    };

    private Handler handler1_down = new Handler();
    private Runnable runnable1_down = new Runnable() {
        @Override
        public void run() {

            if (!isremove) {
                setRemovePlay();
                handler1_down.postDelayed(this, 100);
            }
        }
    };

    private void initActionBar() {

        mBtn.setVisibility(View.VISIBLE);
        TextCount.setVisibility(View.GONE);
        mTitle.setVisibility(View.VISIBLE);
        alarm.setVisibility(View.GONE);
        //mtxt.setVisibility(View.VISIBLE);
        mIcon.setVisibility(View.GONE);

        //mTitle.setText("음원 등록 (1 / 2)");
        //mtxt.setText("도움말");

        //mtxt.setOnClickListener(this);

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
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.helpimg:

                View view = getLayoutInflater().inflate(R.layout.supporthelpview, null);
                BottomSheetDialog dialog = new BottomSheetDialog(MusicDetailActivity.this, R.style.AppBottomSheetDialogTheme); // Style here
                dialog.setContentView(view);

                Button closeBtn = (Button) view.findViewById(R.id.close);
                closeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

                break;

            case R.id.likebutton:

                setMusicLike(getMusicInfoResultModel.getMusicinfo().get(0).getMusicId());

                break;

            case R.id.replybutton:

            case R.id.rText:

                Intent i = new Intent(MusicDetailActivity.this, ReplyMusicActivity.class);
                i.putExtra("musicid", getMusicInfoResultModel.getMusicinfo().get(0).getMusicId());
                startActivity(i);

                break;

            case R.id.playmusic:

                getCheckMusic();

                break;

            case R.id.supportmusic:

                if (binding.playcntText.getText().equals("0")) {
                    SFUAlertDialog.singleButtonShowAlert(MusicDetailActivity.this, null, "포인트가 부족하여 음원 후원을 할 수 없습니다.", null);
                } else {

                    playcntText = binding.playcntText.getText().toString();
                    int cNum = Integer.parseInt(playcntText);
                    int mNum = cNum*50;
                    removeMoneyText = String.valueOf(mNum);

                    //String message = "[" + getMusicInfoResultModel.getMusicinfo().get(0).getSinger() + "]의 [" +  getMusicInfoResultModel.getMusicinfo().get(0).getTitle() + "]\n음원 " + playcntText + "회 ("+ (50 * Integer.parseInt(playcntText)) +"포인트 차감)를 후원하시겠습니까?";
                    String message = (50 * Integer.parseInt(playcntText))+"포인트를 차감하여 재생횟수 "+playcntText+"회를 후원하시겠습니까?";
                    SFUAlertDialog.doubleButtonShowAlert(MusicDetailActivity.this, null, message, "취소",null, "후원", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            setSupportMusic(getVersion(), playcntText, removeMoneyText);
                        }
                    });



                }

                break;

        }

    }

    public void setAddPlay() {
        int cNum = Integer.parseInt(binding.playcntText.getText().toString());
        String cText = String.valueOf(cNum);

        if(cNum == maxCnt) {
            isadd = true;
            SFUAlertDialog.singleButtonShowAlert(MusicDetailActivity.this, null, "최대 "+cText+"회까지 가능합니다.", null);
        } else if (binding.playcntText.getText().equals("0")) {
            isadd = true;
            SFUAlertDialog.singleButtonShowAlert(MusicDetailActivity.this, null, "포인트가 부족하여 후원할 수 없습니다.", null);
        } else {

            cNum = cNum + 1;
            int mNum = cNum*50;
            removeMoneyText = String.valueOf(mNum);
            binding.playcntText.setText(String.valueOf(cNum));
            binding.mText.setText("-"+ getNumberFormat(mNum)+"P");
            //binding.mText.setText("차감 포인트 : -"+getNumberFormat(mNum)+"P");

        }
    }

    public void setRemovePlay() {
        if (binding.playcntText.getText().equals("1")) {
            isremove = true;
            SFUAlertDialog.singleButtonShowAlert(MusicDetailActivity.this, null, "최소 1회는 필수입니다.", null);
        } else if (binding.playcntText.getText().equals("0")) {
            isremove = true;
            SFUAlertDialog.singleButtonShowAlert(MusicDetailActivity.this, null, "포인트가 부족하여 후원할 수 없습니다.", null);
        } else {
            int cNum = Integer.parseInt(binding.playcntText.getText().toString());
            cNum = cNum - 1;
            int mNum = cNum*50;
            removeMoneyText = String.valueOf(mNum);
            binding.playcntText.setText(String.valueOf(cNum));
            binding.mText.setText("-"+ getNumberFormat(mNum)+"P");
            //binding.mText.setText("차감 포인트 : -"+ getNumberFormat(mNum)+"P");
        }
    }

    public void setMusicPlay() {

        if (streamingSaveUpService == null) {
            streamingSaveUpService = StreamingSaveUpService.build();
            Log.e("kwonsaw","streamingSaveUpService");
        }

        if (streamingSaveUpService.isOn() == true) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(MusicDetailActivity.this);
            builder.setTitle("알림");
            builder.setMessage("음원이 재생 중입니다. 잠시 후 다시 시도해 주세요.");
            builder.setPositiveButton("확인",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

            builder.show();
            return;
        }
        streamingSaveUpService.resultListener(this).register();
        streamingSaveUpService.setTitle(getMusicInfoResultModel.getMusicinfo().get(0).getTitle()).setSinger(getMusicInfoResultModel.getMusicinfo().get(0).getSinger()).setSponsorType(sponsorType);

        //musicId = musicArrayList.get(0).getMusicId();
        //saveMoney = musicArrayList.get(0).getPoint();

        String scheme = null;
        //scheme = String.format(melonScheme, musicArrayList.get(0).getMelon_songid());

        if (sponsorType.getName().equalsIgnoreCase("melon")) {
            scheme = String.format(melonScheme, getMusicInfoResultModel.getMusicinfo().get(0).getSongid());
        } else {
            scheme = String.format(genieScheme, getMusicInfoResultModel.getMusicinfo().get(0).getSongid());
            //Log.e("kwonsaw",""+scheme);
        }

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(scheme));
        try {
            startActivity(i);
            streamingSaveUpService.setComplete(false);
            streamingSaveUpService.notifyStreaming(AppAplication.context.getString(R.string.app_name), AppAplication.context.getString(R.string.streaming_save_up_fail_notify_start));
        } catch (ActivityNotFoundException e) {
            if (sponsorType.getName().equalsIgnoreCase("melon")) {
                SFUAlertDialog.singleButtonShowAlert(MusicDetailActivity.this, null, "멜론 앱 설치 후 다시 이용해주세요.", null);
            } else {
                SFUAlertDialog.singleButtonShowAlert(MusicDetailActivity.this, null, "지니 앱 설치 후 다시 이용해주세요.", null);
            }

        }


    }

    public void setMusicData(List<GetMusicInfoDataResultModel> list) {

        Glide.with(MusicDetailActivity.this)
                .load(list.get(0).getMainimgurl())
                .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(getApplication(),30,0, RoundedCornersTransformation.CornerType.ALL)))
                .transition(new DrawableTransitionOptions().crossFade())
                .error(R.drawable.ic_launcher_foreground)
                .into(binding.mImage);


        Glide.with(MusicDetailActivity.this)
                .load(list.get(0).getMainimgurl())
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(80)))
                .error(R.drawable.ic_launcher_foreground)
                .into(binding.bImage);

        mTitle.setText(list.get(0).getTitle());

        shareStr = list.get(0).getTitle()+" 음악 듣고 포인트 받으세요!";
        imageUrl = list.get(0).getMainimgurl();

        binding.tText.setText(list.get(0).getTitle());
        binding.sText.setText(list.get(0).getSinger());

        Glide.with(MusicDetailActivity.this)
                .load(getMusicInfoResultModel.getProfileimgurl())
                .circleCrop()
                .error(R.drawable.ic_launcher_foreground)
                .into(binding.uImage);

        String ustr = getMusicInfoResultModel.getNickname()+"님의 후원 음원";
        SpannableStringBuilder sb1 = new SpannableStringBuilder(ustr);
        sb1.setSpan(new StyleSpan(Typeface.BOLD), 0,getMusicInfoResultModel.getNickname().length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        sb1.setSpan(new AbsoluteSizeSpan(15, true), 0, getMusicInfoResultModel.getNickname().length()
                , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // Size
        binding.uText.setText(sb1);
        //binding.uText.setText(getMusicInfoResultModel.getNickname()+"님이 등록한 음원입니다.");

        //binding.cText.setText("현재 잔여 재생 : "+getNumberFormat(list.get(0).getPlay_cnt())+"회 · ");


        if (list.get(0).getType().equalsIgnoreCase("melon")) {
            //binding.pText.setText("음원 재생 플랫폼 : 멜론");
            sponsorType = StreamingSponsorType.MELON;

            String str = "잔여재생 "+getNumberFormat(list.get(0).getPlay_cnt())+"회 · 재생앱  멜론";
            //String str = "재생 플레이어 : 멜론";
            SpannableStringBuilder sb = new SpannableStringBuilder(str);
            sb.setSpan(new ForegroundColorSpan(Color.parseColor("#04E632")), str.length()-2, str.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            sb.setSpan(new StyleSpan(Typeface.BOLD), str.length()-2, str.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            Drawable image = getResources().getDrawable(R.drawable.melonlogo);
            image.setBounds(0, 0, 50, 50);
            sb.setSpan(new ImageSpan(image), str.length()-4, str.length()-2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            //binding.pText.setText(sb);
            binding.cText.setText(sb);


        } else {
            //binding.pText.setText("음원 재생 플랫폼 : 지니");
            sponsorType = StreamingSponsorType.GENIE;
            String str = "잔여재생 "+getNumberFormat(list.get(0).getPlay_cnt())+"회 · 재생앱  지니";
            //String str = "재생 플레이어 : 지니";
            SpannableStringBuilder sb = new SpannableStringBuilder(str);
            sb.setSpan(new ForegroundColorSpan(Color.parseColor("#0095FF")), str.length()-2, str.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            sb.setSpan(new StyleSpan(Typeface.BOLD), str.length()-2, str.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            //binding.pText.setText(sb);
            Drawable image = getResources().getDrawable(R.drawable.genielogo);
            image.setBounds(0, 0, 50, 50);
            sb.setSpan(new ImageSpan(image), str.length()-4, str.length()-2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            binding.cText.setText(sb);

        }

        binding.lText.setText(getNumberFormat(list.get(0).getLike_cnt()));
        binding.rText.setText(getNumberFormat(list.get(0).getReply_cnt()));

        if (getMusicInfoResultModel.getLikeyn().equalsIgnoreCase("y")) {
            binding.likebutton.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24);
        } else {
            binding.likebutton.setBackgroundResource(R.drawable.ic_baseline_favorite_border_gray_24);
        }

        String sCnt = getNumberFormat(list.get(0).getPlay_cnt()+list.get(0).getTotalplay_cnt());
        binding.totalcntText.setText("누적 총 후원 "+sCnt+"회");

    }

    public void setPlayuserData(List<GetUserInfoDataResultModel> list) {

        uRecyclerView = binding.uRecyclerView;

        playUserList = new ArrayList<>();
        playUserList.addAll(list);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(MusicDetailActivity.this, 1, GridLayoutManager.HORIZONTAL, false);

        playuserRecyclerAdapter = new PlayuserRecyclerAdapter(MusicDetailActivity.this,playUserList);
        //uRecyclerView.addItemDecoration(new PaddingDividerDecoration(5));
        uRecyclerView.setAdapter(playuserRecyclerAdapter);
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        uRecyclerView.setLayoutManager(gridLayoutManager);

    }

    public void setRankData(List<GetUserInfoDataResultModel> list) {

        rRecyclerView = binding.rRecyclerView;

        rankList = new ArrayList<>();
        rankList.addAll(list);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(MusicDetailActivity.this, 1, GridLayoutManager.VERTICAL, false);

        rankRecyclerAdapter = new RankRecyclerAdapter(MusicDetailActivity.this,rankList);
        //uRecyclerView.addItemDecoration(new PaddingDividerDecoration(5));
        rRecyclerView.setAdapter(rankRecyclerAdapter);
        rRecyclerView.setNestedScrollingEnabled(false);
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        rRecyclerView.setLayoutManager(gridLayoutManager);

    }

    public void kakaoLink() {
        String TAG = "kakaoLink()";
        // 카카오톡으로 카카오링크 공유 가능

        FeedTemplate feedTemplate = new FeedTemplate(new Content("송포유",imageUrl,    //메시지 제목, 이미지 url
                new Link("https://play.google.com/store/apps/details?id=com.example.song4u"),shareStr,                    //메시지 링크, 메시지 설명
                300,300));

        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("userid", "123131232");


        ShareClient.getInstance().shareDefault(MusicDetailActivity.this, feedTemplate, null, (linkResult, error) -> {
            if (error != null) {
                //Log.e("TAG", "카카오링크 보내기 실패", error);
            } else if (linkResult != null) {
                // Log.d(TAG, "카카오링크 보내기 성공 ${linkResult.intent}");
                MusicDetailActivity.this.startActivity(linkResult.getIntent());

                // 카카오링크 보내기에 성공했지만 아래 경고 메시지가 존재할 경우 일부 컨텐츠가 정상 동작하지 않을 수 있습니다.
                //Log.w("TAG", "Warning Msg: " + linkResult.getWarningMsg());
                //Log.w("TAG", "Argument Msg: " + linkResult.getArgumentMsg());
            }
            return null;
        });


    }


    public void webKakaoLink() {
        String TAG = "webKakaoLink()";

        FeedTemplate feedTemplate = new FeedTemplate(new Content("송포유",imageUrl,    //메시지 제목, 이미지 url
                new Link("https://play.google.com/store/apps/details?id=com.example.song4u"),shareStr,                    //메시지 링크, 메시지 설명
                300,300));

        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("userid", "123131232");
        // 카카오톡 미설치: 웹 공유 사용 권장
        // 웹 공유 예시 코드
        Uri sharerUrl = WebSharerClient.getInstance().makeDefaultUrl(feedTemplate);
        //Uri sharerUrl = WebSharerClient.getInstance().makeCustomUrl(89080, map2);

        // CustomTabs으로 웹 브라우저 열기
        // 1. CustomTabs으로 Chrome 브라우저 열기
        try {
            KakaoCustomTabsClient.INSTANCE.openWithDefault(MusicDetailActivity.this, sharerUrl);
        } catch (UnsupportedOperationException e) {
            // Chrome 브라우저가 없을 때 예외처리
        }

        // 2. CustomTabs으로 디바이스 기본 브라우저 열기
        try {
            KakaoCustomTabsClient.INSTANCE.open(MusicDetailActivity.this, sharerUrl);
        } catch (ActivityNotFoundException e) {
            // 인터넷 브라우저가 없을 때 예외처리
        }
    }

    /**
    /* 음원 후원하기
     **/
    public void setMusicSupportData() {

        int numInt = Integer.parseInt(getMusicInfoResultModel.getSavemoney());

        if (numInt < 50) {
            binding.playcntText.setText("0");
            binding.subtext.setText("*포인트가 부족하여 음원 후원을 할 수 없습니다. 최소 50P부터 가능합니다.");

        } else {

            binding.playcntText.setText("1");
            numInt = (numInt / 50) * 50;
            maxCnt = (numInt / 50);

            //후원가능 포인트
            binding.vText.setText(getNumberFormat(numInt) + "P");
            //차감 포인트
            binding.mText.setTextColor(Color.parseColor("#FF0000"));
            binding.mText.setText("-50P");

            String str = "*보유 포인트 "+getNumberFormat(numInt)+"P로 최대 "+getNumberFormat(maxCnt)+"회 후원 가능합니다. 재생횟수 1회당 50P가 차감됩니다.";
            SpannableString spannableString = new SpannableString(str);
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#9873FF")), 8, str.length()-21, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            binding.subtext.setText(spannableString);
        }

        playcntText = binding.playcntText.getText().toString();
    }

    /**
     /* 음원 상세
     **/

    public void getMusicInfo(String musicid, String songid){

        NetworkManager manager = new NetworkManager();
        manager.getMusicInfo(getVersion(), musicid,  getUserId(), songid, new Callback<GetMusicInfoResultModel>() {
            @Override
            public void onResponse(Call<GetMusicInfoResultModel> call, Response<GetMusicInfoResultModel> response) {
                getMusicInfoResultModel = response.body();

                if(response.isSuccessful()) {

                    if (getMusicInfoResultModel.getResultCode().equalsIgnoreCase("200")) {

                        setMusicData(getMusicInfoResultModel.getMusicinfo());
                        setPlayuserData(getMusicInfoResultModel.getPlayerlist());
                        setRankData(getMusicInfoResultModel.getRanklist());
                        setMusicSupportData();

                    } else if (getMusicInfoResultModel.getResultCode().equalsIgnoreCase("826")) {
                        SFUToast.showToast(AppAplication.context, Errorcode.ERROCODE(getMusicInfoResultModel.getResultCode()));
                        CommonUtil.mSharePrefreences(MusicDetailActivity.this, getString(R.string.share_userId), "guest", null);
                        Intent i = new Intent(MusicDetailActivity.this, LoginActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK );
                        startActivity(i);
                    } else {
                        SFUToast.showToast(AppAplication.context, Errorcode.ERROCODE(getMusicInfoResultModel.getResultCode()));
                    }

                } else {
                    SFUToast.showToast(AppAplication.context, Errorcode.ERROCODE("1001"));
                }

            }

            @Override
            public void onFailure(Call<GetMusicInfoResultModel> call, Throwable t) {
                SFUToast.showToast(AppAplication.context, Errorcode.ERROCODE("1001"));
            }
        });

    }

    public static String removeStringNumber(String str) {
        return str.replaceAll("[^0-9]", "");
    }

    /**
     /* 음원 잔여재생, 프리퀀시 확인
     **/

    public void getCheckMusic(){

        NetworkManager manager = new NetworkManager();
        manager.getCheckMusic(getVersion(), getUserId(), getMusicInfoResultModel.getMusicinfo().get(0).getMusicId(), "uMusic", new Callback<GetCheckMusicResultModel>() {
            @Override
            public void onResponse(Call<GetCheckMusicResultModel> call, Response<GetCheckMusicResultModel> response) {
                getCheckMusicResultModel = response.body();

                if(response.isSuccessful()) {

                    if (getCheckMusicResultModel.getResultCode().equalsIgnoreCase("200")) {

                        setMusicPlay();

                    } else {
                        SFUAlertDialog.singleButtonShowAlert(MusicDetailActivity.this, null, Errorcode.ERROCODE(getCheckMusicResultModel.getResultCode()), null);
                        //SFUToast.showToast(AppAplication.context, Errorcode.ERROCODE(getCheckMusicResultModel.getResultCode()));
                    }

                } else {
                    SFUToast.showToast(AppAplication.context, Errorcode.ERROCODE("1001"));
                }

            }

            @Override
            public void onFailure(Call<GetCheckMusicResultModel> call, Throwable t) {
                SFUToast.showToast(AppAplication.context, Errorcode.ERROCODE("1001"));
            }
        });

    }

    /**
     /* 음원 좋아요
     **/

    public void setMusicLike(String musicid){

        NetworkManager manager = new NetworkManager();
        manager.setMusicLike(musicid, getUserId(), new Callback<SetMusicLikeResultModel>() {
            @Override
            public void onResponse(Call<SetMusicLikeResultModel> call, Response<SetMusicLikeResultModel> response) {
                setMusicLikeResultModel = response.body();

                if(response.isSuccessful()) {

                    if (setMusicLikeResultModel.getResultCode().equalsIgnoreCase("200")) {

                        int i =  getMusicInfoResultModel.getMusicinfo().get(0).getLike_cnt();
                        i++;
                        binding.lText.setText(getNumberFormat(i));
                        binding.likebutton.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24);

                    } else {
                        SFUToast.showToast(AppAplication.context, Errorcode.ERROCODE(setMusicLikeResultModel.getResultCode()));
                    }

                } else {
                    SFUToast.showToast(AppAplication.context, Errorcode.ERROCODE("1001"));
                }

            }

            @Override
            public void onFailure(Call<SetMusicLikeResultModel> call, Throwable t) {
                SFUToast.showToast(AppAplication.context, Errorcode.ERROCODE("1001"));
            }
        });

    }

    /**
     /* 회원 후원곡 포인트 적립
     **/

    public void setMusicPoint(String appVersion){

        String uidText = getMusicInfoResultModel.getMusicinfo().get(0).getTitle()+"/"+getMusicInfoResultModel.getMusicinfo().get(0).getSinger();
        String musicid = getMusicInfoResultModel.getMusicinfo().get(0).getMusicId();
        String point = getMusicInfoResultModel.getMusicinfo().get(0).getPoint();

        NetworkManager manager = new NetworkManager();
        manager.setMusicPoint(appVersion, getUserId(), musicid, point, "uMusic", uidText,  new Callback<SetMusicPointResultModel>() {
            @Override
            public void onResponse(Call<SetMusicPointResultModel> call, Response<SetMusicPointResultModel> response) {
                setMusicPointResultModel = response.body();

                if(response.isSuccessful()) {

                    if (setMusicPointResultModel.getResultCode().equalsIgnoreCase("200")) {

                        streamingSaveUpService.notifyStreaming(AppAplication.context.getString(R.string.app_name), AppAplication.context.getString(R.string.streaming_save_up_fail_notify_success));
                        //startActivity(SavePopupView.newIntent(AppAplication.context, null, ""));

                        //getMusicInfo(getIntent().getStringExtra("songid"));

                        Intent intent = new Intent(AppAplication.context, SavePopupView.class);
                        intent.putExtra("point", setMusicPointResultModel.getSavemoney());
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        PendingIntent pendingIntent = PendingIntent.getActivity(AppAplication.context, 0 , intent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                        try{
                            pendingIntent.send();
                        }
                        catch(Exception e){
                            //Toast.makeText(mContext, e.toString(), Toast.LENGTH_LONG);
                        }

                    } else {
                        SFUToast.showToast(AppAplication.context, Errorcode.ERROCODE(setMusicPointResultModel.getResultCode()));
                    }

                } else {
                    SFUToast.showToast(AppAplication.context, Errorcode.ERROCODE("1001"));
                }

            }

            @Override
            public void onFailure(Call<SetMusicPointResultModel> call, Throwable t) {
                SFUToast.showToast(AppAplication.context, Errorcode.ERROCODE("1001"));
            }
        });

    }

    /**
     /* 음원 후원
     **/

    public void setSupportMusic(String appVersion, String cntText, String pointText){

        NetworkManager manager = new NetworkManager();
        manager.setSupportMusic(appVersion, getUserId(), getMusicInfoResultModel.getMusicinfo().get(0).getMusicId(),getMusicInfoResultModel.getMusicinfo().get(0).getSongid(), getMusicInfoResultModel.getMusicinfo().get(0).getTitle(), getMusicInfoResultModel.getMusicinfo().get(0).getSinger(), cntText, pointText,  new Callback<SetSupportMusicResultModel>() {
            @Override
            public void onResponse(Call<SetSupportMusicResultModel> call, Response<SetSupportMusicResultModel> response) {
                setSupportMusicResultModel = response.body();

                if(response.isSuccessful()) {

                    if (setSupportMusicResultModel.getResultCode().equalsIgnoreCase("200")) {
                        SFUToast.showToast(AppAplication.context,"후원이 정상적으로 완료되었습니다.");
                        getMusicInfo(getIntent().getStringExtra("musicid"), getIntent().getStringExtra("songid"));
                    } else {
                        SFUToast.showToast(AppAplication.context, Errorcode.ERROCODE(setSupportMusicResultModel.getResultCode()));
                    }

                } else {
                    SFUToast.showToast(AppAplication.context, Errorcode.ERROCODE("1001"));
                }

            }

            @Override
            public void onFailure(Call<SetSupportMusicResultModel> call, Throwable t) {
                SFUToast.showToast(AppAplication.context, Errorcode.ERROCODE("1001"));
            }
        });

    }

    @Override
    public void complete(Boolean isSuccess, String no, StreamingSponsorType sponsorType) {
        if (isSuccess) {
            setMusicPoint(getVersion());
            Log.e("kwonsaw","complete");
        } else {

        }
    }

}
