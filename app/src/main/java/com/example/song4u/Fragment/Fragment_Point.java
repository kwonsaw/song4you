package com.example.song4u.Fragment;

import static com.example.song4u.activity.Season2BaseActivity.getOs;
import static com.example.song4u.activity.Season2BaseActivity.getVersion;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.buzzvil.buzzad.benefit.core.ad.AdError;
import com.buzzvil.buzzad.benefit.presentation.feed.BuzzAdFeed;
import com.buzzvil.buzzad.benefit.presentation.nativead.NativeAd;
import com.buzzvil.buzzad.benefit.presentation.nativead.NativeAdEventListener;
import com.buzzvil.buzzad.benefit.presentation.nativead.NativeAdLoader;
import com.buzzvil.buzzad.benefit.presentation.nativead.NativeAdLoaderParams;
import com.buzzvil.buzzad.benefit.presentation.reward.RewardResult;
import com.example.song4u.Adapter.CarouselAdapter;
import com.example.song4u.Adapter.Mobifeed_Adapter;
import com.example.song4u.Application.AppAplication;
import com.example.song4u.R;
import com.example.song4u.Util.CarouselItem;
import com.example.song4u.Util.CommonUtil;
import com.example.song4u.Util.Errorcode;
import com.example.song4u.Util.PaddingDividerDecoration;
import com.example.song4u.Util.SFUToast;
import com.example.song4u.activity.LoginActivity;
import com.example.song4u.activity.PointNewsActivity;
import com.example.song4u.activity.PrerollActivity;
import com.example.song4u.activity.Season2BaseActivity;
import com.example.song4u.databinding.FragmentPointBinding;
import com.example.song4u.network.NetworkManager;
import com.example.song4u.network.resultmodel.GetBannerDataResultModel;
import com.example.song4u.network.resultmodel.GetBannerResultModel;
import com.example.song4u.network.resultmodel.GetPopupNoticeResultModel;
import com.example.song4u.network.resultmodel.GetPrerollDataResultModel;
import com.example.song4u.network.resultmodel.GetPrerollResultModel;
import com.example.song4u.network.resultmodel.MobifeedDateResultModel;
import com.example.song4u.network.resultmodel.MobifeedResultModel;
import com.example.song4u.network.resultmodel.SetBannerResultModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.mmc.man.AdConfig;
import com.mmc.man.AdEvent;
import com.mmc.man.AdListener;
import com.mmc.man.AdResponseCode;
import com.mmc.man.data.AdData;
import com.mmc.man.view.AdManView;
import com.tnkfactory.ad.AdItem;
import com.tnkfactory.ad.AdListType;
import com.tnkfactory.ad.BannerAdView;
import com.tnkfactory.ad.TnkSession;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Point extends Fragment implements View.OnClickListener {

    private FragmentPointBinding binding;

    private GetPrerollResultModel getPrerollResultModel;
    private GetPrerollDataResultModel getPrerollDataResultModel;
    private GetPopupNoticeResultModel getPopupNoticeResultModel;
    private SetBannerResultModel setBannerResultModel;
    private GetBannerResultModel getBannerResultModel;
    private MobifeedResultModel mobifeedResultModel;

    private ArrayList<GetPrerollDataResultModel> mArrayList;
    private ArrayList<GetBannerDataResultModel> BannerArray;
    private ArrayList<MobifeedDateResultModel> newsArray;

    /* 모비피드 뉴스 */
    private RecyclerView newsRecyclerView;
    private Mobifeed_Adapter mobifeed_adapter;

    private boolean isMezzo = false;
    private boolean isTnk = false;
    private boolean isWorks = false;

    //Mezzo 광고
    private AdManView bannerView = null;
    private ViewGroup bLayout;
    private LinearLayout mezzoLay;
    Context ct;
    private Handler handler = new Handler();


    /* 버즈빌 캐러셀 */
    private CarouselAdapter adapter;
    private RecyclerView recyclerView;

    private final float itemWidthPercentOfScreen = 0.8f;
    private final int itemPaddingDp = 16;
    // 무한 루프를 사용할지 설정
    private boolean isInfiniteLoopEnabled = true;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //DashboardViewModel dashboardViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(DashboardViewModel.class);

        //ct = container.getContext();

        binding = FragmentPointBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowCustomEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();

        return root;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();

        getPreroll();
        getPointNews();
        initBuzbill();

        getBanner();

        /* 띠배너 호출 */
        initMezzo();
        initTnk();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void onDestoryBanner(final Context c) {
        if (bannerView != null) {
            bannerView.onDestroy();
        }
    }

    private void init() {

        mezzoLay = binding.mezzolay;

        binding.offerwall1.setOnClickListener(this);
        binding.offerwall2.setOnClickListener(this);
        binding.offerwall3.setOnClickListener(this);

        binding.prerollImg.setOnClickListener(this);
        binding.helpimg.setOnClickListener(this);


    }

    @Override
    public void onResume() {
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
    public void onDestroy() {
        super.onDestroy();
    }

    /* 적립 안내 팝업 */

    public void savePopup(String pText) {

        View view = getLayoutInflater().inflate(R.layout.savepopupview, null);
        Dialog dilaog01= new Dialog(getActivity());
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            //버즈빌
            case R.id.offerwall1:

                new BuzzAdFeed.Builder().build().show(getActivity());

                break;

            //TNK
            case R.id.offerwall2:

                //TnkSession.showAdList(getActivity(), "미션참여 적립관");

                TnkSession.showAdListByType(getActivity(),
                        "미션참여 적립관",
                        AdListType.ALL,
                        AdListType.PPI,
                        AdListType.CPS
                );

                /*
                PincruxOfferwall offerwall = PincruxOfferwall.getInstance();
                offerwall.init(getActivity(), "910558", CommonUtil.mSharePrefreences(getActivity(), getString(R.string.share_userId), null, "guest"));

                //offerwall.getInstance().init(getActivity(), "910558", CommonUtil.mSharePrefreences(getActivity(), getString(R.string.share_userId), null, "guest"));
                offerwall.getInstance().setOfferwallType(3);
                //offerwall.getInstance().setTitleVisible(true);
                //offerwall.getInstance().setTabType(true);
                //offerwall.getInstance().setShowMoveTopButton(true);
                offerwall.getInstance().startPincruxOfferwallActivity(getActivity());

                 */

                break;

            //핀크럭스
            case R.id.offerwall3:

               //TnkSession.showAdList(ct, "미션참여 적립관");
               TnkSession.showAdList(getActivity(), "미션참여 적립관");

                break;


            case R.id.prerollImg:

                Intent i = new Intent(getActivity(), PrerollActivity.class);
                i.putExtra("videolink", getPrerollDataResultModel.getClickurl());
                i.putExtra("savemoney", getPrerollDataResultModel.getSavemoney());
                i.putExtra("loadingtime", getPrerollDataResultModel.getLoadingtime());
                startActivity(i);

                break;

            case R.id.helpimg:

                View view = getLayoutInflater().inflate(R.layout.prerollhelpview, null);
                BottomSheetDialog dialog = new BottomSheetDialog(getActivity(), R.style.AppBottomSheetDialogTheme); // Style here
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

        }
    }

    /* Mezzo SDK 띠배너 광고 */
    private void initMezzo() {

        //네이티브 띠배너 상용
        //1240 30478 804851

        //일반 띠배너 상용
        //1240 30478 801679

        //일반 띠배너 테스트
        // 100 200 804312

        bLayout = binding.mezzoAdView;
        bLayout.removeAllViewsInLayout();
        AdData adData = new AdData();
        String appName = "appletree2";
        adData.major("testbanner", AdConfig.API_BANNER, 1240, 30478, 801679, "https://play.google.com/store/apps/details?id=com.example.song4u&hl=ko", "com.example.song4u", appName, 320, 50);
        //adData.minor("0", "40", "mezzo", "geonjin.mun@cj.net");
        adData.setUserAgeLevel(1);
        adData.isPermission(AdConfig.NOT_USED, AdConfig.NOT_USED);
        bannerView = new AdManView(getActivity());
        bannerView.setData(adData, new AdListener() {
            //광고 호출 성공
            @Override
            public void onAdSuccessCode(Object v, String id, final String type, final String status, final String jsonDataString) {

                ((Activity) getActivity()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //API MODE 사용시 통신에 사용된 영역을 제거합니다.
                        if(AdConfig.USED.equals(adData.getApiModule())){

                        }else {
                            ((Activity) getActivity()).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (AdResponseCode.Status.SUCCESS.equals(status)) {

                                        if(bannerView != null) {

                                            isMezzo = true;
                                            //bannerOnhide();
                                            bannerView.addBannerView(bLayout);
                                            mezzoLay.setVisibility(View.VISIBLE);

                                        } else {

                                        }
                                    }
                                }
                            });
                        }
                    }
                });
            }
            //광고 호출 실패 에러
            @Override
            public void onAdFailCode(Object v, String id, String type, String status, String jsonDataString) {
                onDestoryBanner(getActivity());
                isMezzo = false;
                //bannerOnhide();
                mezzoLay.setVisibility(View.GONE);
                bLayout.setVisibility(View.GONE);
            }
            //광고 호출 실패 에러(웹뷰 에러)
            @Override
            public void onAdErrorCode(Object v, String id, String type, String status, String failingUrl) {
                onDestoryBanner(getActivity());
                isMezzo = false;
                //bannerOnhide();
                mezzoLay.setVisibility(View.GONE);
                bLayout.setVisibility(View.GONE);
            }
            //광고에서 발생하는 이벤트
            @Override
            public void onAdEvent(Object v, String id, String type, String status, String jsonDataString) {

                if(AdEvent.Type.CLICK.equals(type)){
                    setBannerSaveMoney("bannermezzo");
                    //onDestoryBanner(getActivity());
                }else if(AdEvent.Type.CLOSE.equals(type)){
                    onDestoryBanner(getActivity());
                }else if(AdEvent.Type.IMP.equals(type)){

                }else if(AdEvent.Type.DESTROY.equals(type)){
                    onDestoryBanner(getActivity());
                }
            }
            // 광고 퍼미션(위치) 설정 요청 이벤트 - 사용자 단말기 설정 페이지에 있는 앱 권한을 받기 위한 이벤트입니다.
            @Override
            public void onPermissionSetting(Object v, String id) {
                // isPermission값이 미사용으로 설정한다면 이벤트가 발생하지 않습니다.
            }
        });
        bannerView.request(new Handler());

    }

    // TNK 띠배너
    public void initTnk() {
/*
        Logger.enableLogging(false);
        // 유저 식별 값 설정
        TnkSession.setUserName(getActivity(), CommonUtil.mSharePrefreences(getActivity(), getString(R.string.share_userId), null, "guest"));
        // 실행형 광고
        TnkSession.applicationStarted(getActivity());
        // COPPA 설정 (true - ON / false - OFF)
        TnkSession.setCOPPA(getActivity(), false);
*/
        LinearLayout bannerAdLayout = (LinearLayout) getActivity().findViewById(R.id.tnkAdView);
        BannerAdView bannerAdView = new BannerAdView(getActivity(), "song4you_banner");
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
            isTnk = false;
            //bannerOnhide();
            binding.tnklay.setVisibility(View.GONE);

        }

        /**
         * 광고 load() 후 광고가 도착하면 호출됨
         *
         * @param adItem 이벤트 대상이되는 AdItem 객체
         */
        @Override
        public void onLoad(AdItem adItem) {
            Log.e("kwonsaw","onLoad");
            binding.tnklay.setVisibility(View.VISIBLE);

        }

        /**
         * 광고 화면이 화면이 나타나는 시점에 호출된다.
         *
         * @param adItem 이벤트 대상이되는 AdItem 객체
         */
        @Override
        public void onShow(AdItem adItem) {
            Log.e("kwonsaw","onShow");
            isTnk = true;
            //bannerOnhide();
            binding.tnklay.setVisibility(View.VISIBLE);

        }


        /**
         * 광고 클릭시 호출됨
         * 광고 화면은 닫히지 않음
         *
         * @param adItem 이벤트 대상이되는 AdItem 객체
         */
        @Override
        public void onClick(AdItem adItem) {

            setBannerSaveMoney("bannertnk");

        }
    };

    /* 띠배너 광고가 모두 없을 때 "배너 광고" 섹션 제거 "*/

    public void bannerOnhide() {
        if (!isMezzo && !isTnk && !isWorks ) {
            binding.bannerLinear.setVisibility(View.GONE);
        } else {
            binding.bannerLinear.setVisibility(View.VISIBLE);
        }
    }

    /* 포인트뉴스 레이아웃 설정 */

    private final LinearLayoutManager newslayoutManager = new LinearLayoutManager(
            getActivity(),
            RecyclerView.HORIZONTAL,
            false
    ) {
        // Carousel 아이템의 폭이 화면을 채우도록 변경합니다.
        @Override
        public boolean checkLayoutParams(final RecyclerView.LayoutParams lp) {
            //lp.width = getWidth();
            lp.width = Math.round(getWidth() * itemWidthPercentOfScreen);
            //lp.height = (2*lp.width/3)+100;
            return true;
        }
    };

    /* 버즈빌 캐러셀 */
    public void initBuzbill () {

        recyclerView = binding.carousel;
        adapter = new CarouselAdapter(isInfiniteLoopEnabled);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new PaddingDividerDecoration(itemPaddingDp));
        recyclerView.setAdapter(adapter);
        new PagerSnapHelper().attachToRecyclerView(recyclerView);

        loadAds();
    }

    /*
    /** 버즈빌 캐러샐 광고
     */
    private final LinearLayoutManager layoutManager = new LinearLayoutManager(
            getActivity(),
            RecyclerView.HORIZONTAL,
            false
    ) {
        // Carousel 아이템의 폭이 화면을 채우도록 변경합니다.
        @Override
        public boolean checkLayoutParams(final RecyclerView.LayoutParams lp) {
            //lp.width = getWidth();
            lp.width = Math.round(getWidth() * itemWidthPercentOfScreen);
            return true;
        }
    };

    private final NativeAdLoader.OnAdsLoadedListener adsLoadedListener = new NativeAdLoader.OnAdsLoadedListener() {

        @Override
        public void onLoadError(final AdError error) {
            onError();
        }

        @Override
        public void onAdsLoaded(final Collection<NativeAd> nativeAds) {
            if (nativeAds.isEmpty()) {
                onEmpty();
            } else {
                Fragment_Point.this.onAdsLoaded(nativeAds);

            }
        }
    };

    private void loadAds() {
        new NativeAdLoader("238037458066853").loadAds(
                adsLoadedListener,
                new NativeAdLoaderParams.Builder()
                        .count(10)
                        .build()
        );
    }

    private void onError() {
        // 오류가 발생하면 지면을 표시하지 않습니다.
        recyclerView.setVisibility(View.GONE);
        //Log.d(TAG, "An error occurred while loading ads");
    }

    private void onEmpty() {
        // 보여줄 광고가 없으면 지면을 표시하지 않습니다.
        recyclerView.setVisibility(View.GONE);
        //Log.d(TAG, "The ad list is empty");
    }


    private void onAdsLoaded(final Collection<NativeAd> nativeAds) {

        recyclerView.setVisibility(View.VISIBLE);
        final List<CarouselItem> items = buildCarouselItems(nativeAds);
        adapter.submitList(items);
        if (isInfiniteLoopEnabled) {
            final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager != null) {
                // item.size(): 첫번째 광고가 보이고, 그 앞에 마지막 아이템이 살짝 보이기 위해 이 값을 기준으로 합니다.
                // 10000: 사용자가 앞으로 계속 스크롤을 하여도 무한루프인 것처럼 느낄 수 있도록 적당히 큰 수를 곱합니다.
                layoutManager.scrollToPosition(items.size() * 10000);
            }
        }
        for (final NativeAd nativeAd : nativeAds) {
            nativeAd.addNativeAdEventListener(nativeAdEventListener);
        }
        //Log.d(TAG, "" + nativeAds.size() + " Ads are loaded");
    }

    private List<CarouselItem> buildCarouselItems(final Collection<NativeAd> nativeAds) {
        List<CarouselItem> items = new ArrayList<>();
        for (final NativeAd nativeAd : nativeAds) {
            items.add(new CarouselItem.NativeAdItem(nativeAd));
        }

        return items;
    }

    private NativeAdEventListener nativeAdEventListener = new NativeAdEventListener() {
        @Override
        public void onImpressed(final NativeAd nativeAd) {
        }

        @Override
        public void onClicked(final NativeAd nativeAd) {
        }

        @Override
        public void onRewardRequested(final NativeAd nativeAd) {
        }

        @Override
        public void onRewarded(
                final NativeAd nativeAd,
                final RewardResult nativeAdRewardResult

        ) {

            final int reward = nativeAd.getAvailableReward();
            final String helpstr = "%sP 적립되었습니다.";

            if (nativeAdRewardResult == nativeAdRewardResult.SUCCESS){

                savePopup(String.valueOf(reward));

            } else {
                Toast.makeText(getContext(), "이미 적립한 광고입니다.", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void onParticipated(final NativeAd nativeAd) {
        }
    };

    /**
     /* 띠배너 광고 초기화
     **/

    public void initBanner() {

        /* 메조 미디어 */
        binding.mezzocontent.setText(BannerArray.get(0).getContent());
        binding.mezzoPoint.setText(""+BannerArray.get(0).getSavemoney()+"P");
        if (BannerArray.get(0).getSaveyn().equalsIgnoreCase("n")) {
            binding.mezzocontent.setTextColor(getResources().getColor(R.color.red));
        } else {
            binding.mezzocontent.setTextColor(getResources().getColor(R.color.black));
        }

        /* TNK */
        binding.tnkcontent.setText(BannerArray.get(1).getContent());
        binding.tnkPoint.setText(""+BannerArray.get(1).getSavemoney()+"P");
        if (BannerArray.get(1).getSaveyn().equalsIgnoreCase("n")) {
            binding.tnkcontent.setTextColor(getResources().getColor(R.color.red));
        } else {
            binding.tnkcontent.setTextColor(getResources().getColor(R.color.black));
        }

    }

    /**
     /* 프리롤 광고 초기화
     **/

    public void initPreroll() {

        mArrayList = new ArrayList<>();
        mArrayList.addAll(getPrerollResultModel.getPrerollList());
        // 데이터가 1개일 때 get(0)으로 받음, 추가 되었을 시 변경
        getPrerollDataResultModel = mArrayList.get(0);

        Glide.with(getActivity())
                .load(getPrerollDataResultModel.getImg())
                //.apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(AppAplication.context, 30, 0, RoundedCornersTransformation.CornerType.ALL)))
                //.error(R.drawable.com_facebook_profile_picture_blank_portrait)
                .into(binding.prerollImg);

        binding.pointTxt.setText(getPrerollDataResultModel.getSavemoney()+"P");

    }

    /**
     /* 프리롤 광고 정보
     **/

    public void getPreroll(){

        NetworkManager manager = new NetworkManager();
        manager.getPreroll(getOs(),  getVersion(), new Callback<GetPrerollResultModel>() {
            @Override
            public void onResponse(Call<GetPrerollResultModel> call, Response<GetPrerollResultModel> response) {
                getPrerollResultModel = response.body();

                if (response.isSuccessful()) {

                    if (getPrerollResultModel.getResultCode().equalsIgnoreCase("200")) {
                        initPreroll();
                    }

                } else {
                    ((Season2BaseActivity) getActivity()).showToast(Errorcode.ERROCODE("1001"));
                }
            }

            @Override
            public void onFailure(Call<GetPrerollResultModel> call, Throwable t) {
                ((Season2BaseActivity) getActivity()).showToast(Errorcode.ERROCODE("1001"));
            }
        });

    }

    /**
     /* 배너 광고 정보
     **/

    public void getBanner(){

        NetworkManager manager = new NetworkManager();
        manager.getBanner(CommonUtil.mSharePrefreences(getActivity(), getString(R.string.share_userId), null, "guest"),  getVersion(), new Callback<GetBannerResultModel>() {
            @Override
            public void onResponse(Call<GetBannerResultModel> call, Response<GetBannerResultModel> response) {
                getBannerResultModel = response.body();

                if (response.isSuccessful()) {

                    if (getBannerResultModel.getResultCode().equalsIgnoreCase("200")) {

                        BannerArray = new ArrayList<>();
                        BannerArray.clear();
                        BannerArray.addAll(getBannerResultModel.getBannerlist());
                        initBanner();

                    }else if (getBannerResultModel.getResultCode().equalsIgnoreCase("826")) {
                        SFUToast.showToast(AppAplication.context, Errorcode.ERROCODE(getBannerResultModel.getResultCode()));
                        CommonUtil.mSharePrefreences(getActivity(), getString(R.string.share_userId), "guest", null);
                        Intent i = new Intent(getActivity(), LoginActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK );
                        startActivity(i);
                    } else {
                        SFUToast.showToast(AppAplication.context, Errorcode.ERROCODE(getBannerResultModel.getResultCode()));
                    }

                } else {

                    ((Season2BaseActivity) getActivity()).showToast(Errorcode.ERROCODE("1001"));
                }
            }

            @Override
            public void onFailure(Call<GetBannerResultModel> call, Throwable t) {
                ((Season2BaseActivity) getActivity()).showToast(Errorcode.ERROCODE("1001"));
            }
        });

    }

    /**
     /* 배너 적립
     **/

    public void setBannerSaveMoney(String getSourceId){

        NetworkManager manager = new NetworkManager();
        manager.setBanner(CommonUtil.mSharePrefreences(getActivity(), getString(R.string.share_userId), null, "guest"),  getVersion(), "1", getSourceId, new Callback<SetBannerResultModel>() {
            @Override
            public void onResponse(Call<SetBannerResultModel> call, Response<SetBannerResultModel> response) {
                setBannerResultModel = response.body();

                if (response.isSuccessful()) {

                    if (setBannerResultModel.getResultCode().equalsIgnoreCase("200")) {

                        savePopup(setBannerResultModel.getSavemoney());
                        getBanner();

                    } else if (setBannerResultModel.getResultCode().equalsIgnoreCase("100")) {

                        ((Season2BaseActivity) getActivity()).showToast("적립 가능 시간이 아닙니다.");

                    } else {

                    }

                } else {

                    ((Season2BaseActivity) getActivity()).showToast(Errorcode.ERROCODE("1001"));
                }
            }

            @Override
            public void onFailure(Call<SetBannerResultModel> call, Throwable t) {
                ((Season2BaseActivity) getActivity()).showToast(Errorcode.ERROCODE("1001"));
            }
        });

    }

    /**
     /* 모비피드 뉴스 리스트
     **/

    public void getPointNews(){

        NetworkManager manager = new NetworkManager();
        manager.getMobFeedLineNews(CommonUtil.mSharePrefreences(getActivity(), getString(R.string.share_userId), null, "guest"),  10, new Callback<MobifeedResultModel>() {
            @Override
            public void onResponse(Call<MobifeedResultModel> call, Response<MobifeedResultModel> response) {
                mobifeedResultModel = response.body();

                if (response.isSuccessful()) {

                    if (mobifeedResultModel.getResultCode().equalsIgnoreCase("0000")) {

                        if (mobifeed_adapter != null) {
                            newsArray.clear();
                            newsArray.addAll(mobifeedResultModel.getNewsList());
                            mobifeed_adapter.notifyDataSetChanged();
                            newsRecyclerView.scrollToPosition(0);
                        } else {
                            newsArray = new ArrayList<>();
                            newsArray.addAll(mobifeedResultModel.getNewsList());
                            newsRecyclerView = binding.newsRecyclerView;
                            mobifeed_adapter = new Mobifeed_Adapter(getActivity(), newsArray);
                            newsRecyclerView.setLayoutManager(newslayoutManager);
                            newsRecyclerView.addItemDecoration(new PaddingDividerDecoration(itemPaddingDp));

                            mobifeed_adapter.setOnItemClickListener(new Mobifeed_Adapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View v, int position) {

                                    Intent i = new Intent(getActivity(), PointNewsActivity.class);
                                    i.putExtra("url", newsArray.get(position).getLink_url());
                                    i.putExtra("title", newsArray.get(position).getNews_title());
                                    i.putExtra("sourceId", newsArray.get(position).getObject_id());
                                    startActivityResult.launch(i);

                                }
                            }) ;

                            newsRecyclerView.setAdapter(mobifeed_adapter);
                            if (newsRecyclerView.getOnFlingListener() == null) {
                                new PagerSnapHelper().attachToRecyclerView(newsRecyclerView);
                            }

                        }
                    } else {
                        ((Season2BaseActivity) getActivity()).showToast(Errorcode.ERROCODE("1001"));
                    }

                } else {

                    ((Season2BaseActivity) getActivity()).showToast(Errorcode.ERROCODE("1001"));
                }
            }

            @Override
            public void onFailure(Call<MobifeedResultModel> call, Throwable t) {
                ((Season2BaseActivity) getActivity()).showToast(Errorcode.ERROCODE("1001"));
            }
        });

    }

    /* 뉴스 보고 돌아왔을 경우 포인트뉴스 호출 */

    ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        getPointNews();
                    }
                }
            });

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
