package com.example.song4u.activity;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.example.song4u.R;
import com.example.song4u.databinding.ShareactivityBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.kakao.sdk.common.util.KakaoCustomTabsClient;
import com.kakao.sdk.share.ShareClient;
import com.kakao.sdk.share.WebSharerClient;
import com.kakao.sdk.template.model.Content;
import com.kakao.sdk.template.model.FeedTemplate;
import com.kakao.sdk.template.model.Link;
import com.kakao.sdk.user.UserApiClient;

import java.util.HashMap;
import java.util.Map;

public class ShareActivity extends Season2BaseActivity {

    private ShareactivityBinding binding;

    private String shareStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ShareactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
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

        shareStr = "추천인 아이디 : "+getUserId()+"\n나와 친구 모두 100포인트 적립!";
        binding.userid.setText("추천인아이디: "+getUserId());

        /* 공유하기 */
        binding.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View view = getLayoutInflater().inflate(R.layout.shareview, null);

                ImageButton kakaoBtn = (ImageButton) view.findViewById(R.id.kakao);
                ImageButton copyBtn = (ImageButton) view.findViewById(R.id.copy);

                BottomSheetDialog dialog = new BottomSheetDialog(ShareActivity.this,R.style.AppBottomSheetDialogTheme); // Style here
                dialog.setContentView(view);
                dialog.show();

                kakaoBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(ShareActivity.this)) {
                            kakaoLink();
                        } else {
                            webKakaoLink();
                        }
                        dialog.dismiss();
                    }
                });

                copyBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        copy(getUserId());
                        dialog.dismiss();
                    }
                });
            }
        });

    }

    private void initActionBar() {

        TextCount.setVisibility(View.GONE);
        mTitle.setVisibility(View.VISIBLE);
        alarm.setVisibility(View.GONE);
        mtxt.setVisibility(View.GONE);
        mIcon.setVisibility(View.GONE);

        mTitle.setText("친구추천");

        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(mActionBarBackgroundDrawable);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(ActionbarView);
        actionBar.setElevation(0);
    }



    public void kakaoLink() {
        String TAG = "kakaoLink()";
        // 카카오톡으로 카카오링크 공유 가능

        FeedTemplate feedTemplate = new FeedTemplate(new Content("송포유 다운받고 추천인을 등록해주세요.","http://139.150.71.159/project/noticeimg/addfriends.png",    //메시지 제목, 이미지 url
                new Link("https://play.google.com/store/apps/details?id=com.example.song4u"),shareStr,                    //메시지 링크, 메시지 설명
                300,300));

        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("userid", "123131232");

        /*
        ShareClient.getInstance().shareCustom(ShareActivity.this, 89080, map2, (linkResult, error) -> {
            if (error != null) {
                //Log.e("TAG", "카카오링크 보내기 실패", error);
            } else if (linkResult != null) {
                // Log.d(TAG, "카카오링크 보내기 성공 ${linkResult.intent}");
                ShareActivity.this.startActivity(linkResult.getIntent());

                // 카카오링크 보내기에 성공했지만 아래 경고 메시지가 존재할 경우 일부 컨텐츠가 정상 동작하지 않을 수 있습니다.
                //Log.w("TAG", "Warning Msg: " + linkResult.getWarningMsg());
                //Log.w("TAG", "Argument Msg: " + linkResult.getArgumentMsg());
            }
            return null;
        });
         */


        ShareClient.getInstance().shareDefault(ShareActivity.this, feedTemplate, null, (linkResult, error) -> {
            if (error != null) {
                //Log.e("TAG", "카카오링크 보내기 실패", error);
            } else if (linkResult != null) {
               // Log.d(TAG, "카카오링크 보내기 성공 ${linkResult.intent}");
                ShareActivity.this.startActivity(linkResult.getIntent());

                // 카카오링크 보내기에 성공했지만 아래 경고 메시지가 존재할 경우 일부 컨텐츠가 정상 동작하지 않을 수 있습니다.
                //Log.w("TAG", "Warning Msg: " + linkResult.getWarningMsg());
                //Log.w("TAG", "Argument Msg: " + linkResult.getArgumentMsg());
            }
            return null;
        });


    }


    public void webKakaoLink() {
        String TAG = "webKakaoLink()";

        /*
        FeedTemplate feedTemplate = new FeedTemplate(
                new Content("오늘의 디저트",
                        "http://mud-kage.kakao.co.kr/dn/Q2iNx/btqgeRgV54P/VLdBs9cvyn8BJXB3o7N8UK/kakaolink40_original.png",
                        new Link("https://developers.kakao.com",
                                "https://developers.kakao.com"),
                        "#케익 #딸기 #삼평동 #카페 #분위기 #소개팅"
                ),
                new ItemContent("Kakao",
                        "http://mud-kage.kakao.co.kr/dn/Q2iNx/btqgeRgV54P/VLdBs9cvyn8BJXB3o7N8UK/kakaolink40_original.png",
                        "Cheese cake",
                        "http://mud-kage.kakao.co.kr/dn/Q2iNx/btqgeRgV54P/VLdBs9cvyn8BJXB3o7N8UK/kakaolink40_original.png",
                        "Cake",
                        Arrays.asList(new ItemInfo("cake1", "1000원")),
                        "Total",
                        "15000원"
                ),
                new Social(286, 45, 845),
                Arrays.asList(new com.kakao.sdk.template.model.Button("웹으로 보기", new Link("https://developers.kakao.com", "https://developers.kakao.com")))
        );
        */

        FeedTemplate feedTemplate = new FeedTemplate(new Content("송포유 다운받고 추천인을 등록해주세요.","http://139.150.71.159/project/noticeimg/addfriends.png",    //메시지 제목, 이미지 url
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
            KakaoCustomTabsClient.INSTANCE.openWithDefault(ShareActivity.this, sharerUrl);
        } catch (UnsupportedOperationException e) {
            // Chrome 브라우저가 없을 때 예외처리
        }

        // 2. CustomTabs으로 디바이스 기본 브라우저 열기
        try {
            KakaoCustomTabsClient.INSTANCE.open(ShareActivity.this, sharerUrl);
        } catch (ActivityNotFoundException e) {
            // 인터넷 브라우저가 없을 때 예외처리
        }
    }


    public void copy(String getText) {
        // 클립보드 객체 얻기
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        // 클립데이터 생성
        ClipData clipData = ClipData.newPlainText("추천인 아이디", getText); //Test 가 실질적으로 복사되는 Text
        // 클립보드에 추가
        clipboardManager.setPrimaryClip(clipData);

        showToast("추천인 아이디가 복사되었습니다.");

    }
}
