package com.example.song4u.Fragment;

import static com.example.song4u.activity.Season2BaseActivity.getOs;
import static com.example.song4u.activity.Season2BaseActivity.getVersion;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.ColorUtils;
import androidx.fragment.app.Fragment;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.song4u.Adapter.MusicRankRecyclerAdapter;
import com.example.song4u.Adapter.MusicRecyclerAdapter;
import com.example.song4u.Adapter.NoticeRecyclerAdapter;
import com.example.song4u.Adapter.PopupNoticeAdapter;
import com.example.song4u.Application.AppAplication;
import com.example.song4u.R;
import com.example.song4u.Util.CommonUtil;
import com.example.song4u.Util.Errorcode;
import com.example.song4u.Util.MeasuredViewPager;
import com.example.song4u.Util.RoundedCornersTransformation;
import com.example.song4u.Util.SFUAlertDialog;
import com.example.song4u.Util.SFUToast;
import com.example.song4u.activity.AddMusicActivity;
import com.example.song4u.activity.LoginActivity;
import com.example.song4u.activity.MusicDetailActivity;
import com.example.song4u.activity.NoticeDetailActivity;
import com.example.song4u.activity.RankMusicActivity;
import com.example.song4u.activity.SavePopupView;
import com.example.song4u.activity.Season2BaseActivity;
import com.example.song4u.databinding.FragmentHomeBinding;
import com.example.song4u.network.NetworkManager;
import com.example.song4u.network.resultmodel.GetCheckMusicResultModel;
import com.example.song4u.network.resultmodel.GetMusicDataResultModel;
import com.example.song4u.network.resultmodel.GetMusicListDataResultModel;
import com.example.song4u.network.resultmodel.GetMusicListResultModel;
import com.example.song4u.network.resultmodel.GetMusicResultModel;
import com.example.song4u.network.resultmodel.GetPopupNoticeDataResultModel;
import com.example.song4u.network.resultmodel.GetPopupNoticeResultModel;
import com.example.song4u.network.resultmodel.SetMusicPointResultModel;
import com.example.song4u.network.streaming.StreamingSaveServiceResultListener;
import com.example.song4u.network.streaming.StreamingSaveUpService;
import com.example.song4u.network.streaming.StreamingSponsorType;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.tnkfactory.ad.AdItem;
import com.tnkfactory.ad.BannerAdView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Fragment_Home extends Fragment implements StreamingSaveServiceResultListener , View.OnClickListener{

    private FragmentHomeBinding binding;

    private PopupNoticeAdapter mAdapter;
    private ArrayList<GetPopupNoticeDataResultModel> mArrayList;
    private MeasuredViewPager noticeViewPager;

    private ArrayList<GetMusicDataResultModel> musicArrayList;
    private ArrayList<GetMusicListDataResultModel> musicList;
    private ArrayList<GetMusicListDataResultModel> musicrankList;

    // 회원 후원곡
    private RecyclerView mRecyclerView;
    private MusicRecyclerAdapter musicRecyclerAdapter;

    //재생횟수 랭킹
    private RecyclerView rRecyclerView;
    private MusicRankRecyclerAdapter musicRankRecyclerAdapter;

    // 공지사항
    private RecyclerView nRecyclerView;
    private NoticeRecyclerAdapter noticeRecyclerAdapter;
    private ArrayList<GetPopupNoticeDataResultModel> noticeList;

    private GetCheckMusicResultModel getCheckMusicResultModel;
    private SetMusicPointResultModel setMusicPointResultModel;
    private GetMusicDataResultModel getMusicDataResultModel;
    private GetMusicResultModel getMusicResultModel;
    private GetPopupNoticeResultModel getPopupNoticeResultModel;
    private GetMusicListResultModel getMusicListResultModel;

    private String strSDFormatDay = "0";
    private String musicId, saveMoney;
    private String musicName;
    private Integer cnt= 0;

    /* 음원 등록 음원 플랫폼 */
    private String musicType = "melon";

    private StreamingSaveUpService streamingSaveUpService; //= StreamingSaveUpService.build().resultListener(this).register();
    private String melonScheme = "melonapp://play?cid=%s&ctype=1";
    private String genieScheme = "igeniesns://detail?landingtype=06&landingtarget=%s";
    private StreamingSponsorType sponsorType;// = StreamingSponsorType.MELON;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        getPopupNotice();
        getMusic(getVersion());

        View view = getLayoutInflater().inflate(R.layout.actionbar_home_img, null);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowCustomEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setCustomView(view);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);


        return root;

    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
        initTnk();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void init() {

        mRecyclerView = binding.mRecyclerView;
        nRecyclerView = binding.nRecyclerView;
        rRecyclerView = binding.rRecyclerView;

        binding.helpimg.setOnClickListener(this);
        binding.helpimg1.setOnClickListener(this);
        binding.addmusic.setOnClickListener(this);

        //누적랭킹 전체보기
        binding.moreText.setOnClickListener(this);
        binding.img.setOnClickListener(this);

    }

    // TNK 띠배너
    public void initTnk() {

        LinearLayout bannerAdLayout = (LinearLayout) getActivity().findViewById(R.id.tnkAd);
        BannerAdView bannerAdView = new BannerAdView(getActivity(), "song4you_banner");
        binding.tnkAd.addView(bannerAdView);

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
            binding.tnkAd.setVisibility(View.GONE);

        }

        /**
         * 광고 load() 후 광고가 도착하면 호출됨
         *
         * @param adItem 이벤트 대상이되는 AdItem 객체
         */
        @Override
        public void onLoad(AdItem adItem) {
            Log.e("kwonsaw","onLoad");
            binding.tnkAd.setVisibility(View.VISIBLE);

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
            binding.tnkAd.setVisibility(View.VISIBLE);

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

    public Boolean noticeCheck () {

        long CurrentTime = System.currentTimeMillis(); // 현재 시간을 msec 단위로 얻음
        Date TodayDate = new Date(CurrentTime); // 현재 시간 Date 변수에 저장
        SimpleDateFormat SDFormat = new SimpleDateFormat("dd");
        strSDFormatDay = SDFormat.format(TodayDate); // 'dd' 형태로 포맷 변경

        String strSPreferencesDay = getNoticeCheck();

        if((Integer.parseInt(strSDFormatDay) - Integer.parseInt(strSPreferencesDay)) != 0) {
            return true;
        } else {
            return false;
        }

    }

    public String getNoticeCheck(){

        return CommonUtil.mSharePrefreences(getActivity(), getString(R.string.share_notice), null, "0");

    }

    public void setPopupNotice (List<GetPopupNoticeDataResultModel> list) {

        View view = getLayoutInflater().inflate(R.layout.popupnoticeactivity, null);

        Button closeBtn = (Button) view.findViewById(R.id.close);
        Button closeallBtn = (Button) view.findViewById(R.id.close_all);
        TextView cntText = (TextView) view.findViewById(R.id.cnttext);

        BottomSheetDialog dialog = new BottomSheetDialog(getActivity(),R.style.AppBottomSheetDialogTheme); // Style here
        dialog.setContentView(view);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        closeallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonUtil.mSharePrefreences(getActivity(), getString(R.string.share_notice),  ((Season2BaseActivity) getActivity()).getDate(), "0");
                dialog.dismiss();
            }
        });


        mArrayList = new ArrayList<>();

        mArrayList.addAll(list);
        //GetPopupNoticeDataResultModel content = mArrayList.get(1);
        mAdapter = new PopupNoticeAdapter(getActivity(),mArrayList);
        noticeViewPager = (MeasuredViewPager) view.findViewById(R.id.viewpager);
        noticeViewPager.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new PopupNoticeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

                Intent i = new Intent(getActivity(), NoticeDetailActivity.class);
                i.putExtra("title", mArrayList.get(position).getNoticeTitle());
                i.putExtra("desc", mArrayList.get(position).getNoticedescription());
                startActivity(i);

            }
        }) ;

        noticeViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                cntText.setText(""+(position+1) +" / "+mArrayList.size());
                cnt = position;
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {


            }
        });

        dialog.show();

    }

    public void setNoticeDate (List<GetPopupNoticeDataResultModel> list) {

        noticeList = new ArrayList<>();
        noticeList.addAll(list);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false);

        noticeRecyclerAdapter = new NoticeRecyclerAdapter(getActivity(),noticeList);
        //mRecyclerView.addItemDecoration(new PaddingDividerDecoration(16));
        nRecyclerView.setAdapter(noticeRecyclerAdapter);
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        nRecyclerView.setLayoutManager(gridLayoutManager);

        noticeRecyclerAdapter.setOnItemClickListener(new NoticeRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

                Intent i = new Intent(getActivity(), NoticeDetailActivity.class);
                i.putExtra("title", noticeList.get(position).getNoticeTitle());
                i.putExtra("desc", noticeList.get(position).getNoticedescription());
                startActivity(i);

            }
        }) ;

    }

    public void setMusicData(List<GetMusicDataResultModel> list) {

        musicArrayList = new ArrayList<>();
        musicArrayList.addAll(list);

        // 데이터가 1개일 때 get(0)으로 받음, 추가 되었을 시 변경
        binding.pointTxt.setText(list.get(0).getPoint()+"P");

        Glide.with(getActivity())
                .asBitmap()
                .load(list.get(0).getMainimgurl())
                .into(new BitmapImageViewTarget(binding.mImage) {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        super.onResourceReady(resource, transition);

                        // 파라미터로 넘어오는 Bitmap resource를 사용해서 Palette 생성
                        Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(@Nullable Palette palette) {

                                //Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();
                                Palette.Swatch vibrantSwatch = palette.getSwatches().get(0);
                                if(vibrantSwatch!=null){
                                    binding.bImage.setBackgroundColor(vibrantSwatch.getRgb());

                                    //binding.dText.setTextColor(vibrantSwatch.getTitleTextColor());
                                    //binding.tText.setTextColor(vibrantSwatch.getTitleTextColor());

                                    // 색상 밝기 체크
                                    double rgb = ColorUtils.calculateLuminance(vibrantSwatch.getRgb());

                                    if (rgb < 0.5) {
                                        binding.dText.setTextColor(Color.WHITE);
                                        binding.tText.setTextColor(Color.WHITE);
                                        binding.modify.setColorFilter(getResources().getColor(R.color.white));
                                    } else {
                                        binding.dText.setTextColor(Color.BLACK);
                                        binding.tText.setTextColor(Color.BLACK);
                                        binding.modify.setColorFilter(getResources().getColor(R.color.black));
                                    }



                                } else {
                                    binding.bImage.setBackgroundColor(getResources().getColor(R.color.grey));
                                    binding.dText.setTextColor(Color.BLACK);
                                    binding.tText.setTextColor(Color.BLACK);
                                    binding.modify.setColorFilter(getResources().getColor(R.color.black));
                                }


                            }
                        });
                    }
                });

        binding.dText.setText(list.get(0).getDescription());
        binding.tText.setText(list.get(0).getTitle()+"\n"+list.get(0).getSinger());


        /* 송포유 추천곡 */
        binding.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //palyerPopup();
                getCheckMusic();
            }
        });

    }


    public void setMusicListData(List<GetMusicListDataResultModel> list) {

        musicList = new ArrayList<>();
        musicList.addAll(list);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.HORIZONTAL, false);

        musicRecyclerAdapter = new MusicRecyclerAdapter(getActivity(),musicList);
        //mRecyclerView.addItemDecoration(new PaddingDividerDecoration(16));
        mRecyclerView.setAdapter(musicRecyclerAdapter);
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        mRecyclerView.setLayoutManager(gridLayoutManager);

        musicRecyclerAdapter.setOnItemClickListener(new MusicRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

                if (position == 0){
                    View view2 = getLayoutInflater().inflate(R.layout.addmusic_search, null);
                    Dialog dilaog011= new Dialog(getActivity());
                    dilaog011.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dilaog011.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dilaog011.setContentView(view2);

                    RadioGroup group = (RadioGroup) view2.findViewById(R.id.radioGroup);
                    musicType = "melon";

                    group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            switch (checkedId){
                                case R.id.r_btn1:
                                    musicType = "melon";
                                    break;
                                case R.id.r_btn2:
                                    musicType = "genie";
                                    break;

                            }
                        }
                    });


                    EditText musicText = (EditText) view2.findViewById(R.id.musicText);
                    Button searchBtn = (Button) view2.findViewById(R.id.search);
                    searchBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(musicText.length() == 0) {
                                SFUToast.showToast(getActivity(), "노래 제목을 입력해주세요.");
                            } else {
                                Intent i = new Intent(getActivity(), AddMusicActivity.class);
                                i.putExtra("musicTitle", musicText.getText().toString());
                                i.putExtra("musicType", musicType);
                                Log.e("kwonsaw",""+musicText.getText());
                                startActivityResult.launch(i);
                                //startActivity(i);
                                dilaog011.dismiss();
                            }
                        }
                    });

                    Button closeBtn2 = (Button) view2.findViewById(R.id.close);
                    closeBtn2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dilaog011.dismiss();
                        }
                    });

                    dilaog011.show();
                } else {
                    Intent i = new Intent(getActivity(), MusicDetailActivity.class);
                    i.putExtra("musicid", musicList.get(position - 1).getMusicId());
                    i.putExtra("songid", musicList.get(position - 1).getSongid());
                    startActivity(i);
                }

            }
        }) ;

        mRecyclerView.setOnClickListener(this);

    }

    public void setMusicRankListData(List<GetMusicListDataResultModel> list) {

        musicrankList = new ArrayList<>();
        musicrankList.addAll(list);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);

        musicRankRecyclerAdapter = new MusicRankRecyclerAdapter(getActivity(),musicrankList);
        //mRecyclerView.addItemDecoration(new PaddingDividerDecoration(16));
        rRecyclerView.setAdapter(musicRankRecyclerAdapter);
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        rRecyclerView.setLayoutManager(gridLayoutManager);

        musicRankRecyclerAdapter.setOnItemClickListener(new MusicRankRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

                Intent i = new Intent(getActivity(), MusicDetailActivity.class);
                i.putExtra("musicid", musicrankList.get(position).getMusicId());
                i.putExtra("songid", musicrankList.get(position).getSongid());
                startActivity(i);

            }
        }) ;

        rRecyclerView.setOnClickListener(this);

    }

    public void setMusicPlay() {

        if (streamingSaveUpService == null) {
            streamingSaveUpService = StreamingSaveUpService.build();
        }

        if (streamingSaveUpService.isOn() == true) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
        streamingSaveUpService.setTitle(musicArrayList.get(0).getTitle()).setSinger(musicArrayList.get(0).getSinger()).setPlayTime(musicArrayList.get(0).getPlaytime()).setSponsorType(sponsorType);

        musicId = musicArrayList.get(0).getMusicId();
        saveMoney = musicArrayList.get(0).getPoint();

        String scheme = null;
        //scheme = String.format(melonScheme, musicArrayList.get(0).getMelon_songid());

        if (sponsorType.getName().equalsIgnoreCase("melon")) {
            scheme = String.format(melonScheme, musicArrayList.get(0).getMelon_songid());
        } else {
            scheme = String.format(genieScheme, musicArrayList.get(0).getGenie_songid());
            //Log.e("kwonsaw",""+scheme);
        }

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(scheme));
        try {
            startActivity(i);
            streamingSaveUpService.setComplete(false);
            streamingSaveUpService.notifyStreaming(getActivity().getString(R.string.app_name), getActivity().getString(R.string.streaming_save_up_fail_notify_start));
        } catch (ActivityNotFoundException e) {
            SFUAlertDialog.singleButtonShowAlert(getActivity(), null, "선택하신 스트리밍 앱 설치 후 다시 이용해주세요.", null);
        }


    }

    public void palyerPopup() {

        View view = getLayoutInflater().inflate(R.layout.musicplatformview, null);
        Dialog dilaog01= new Dialog(getActivity());
        dilaog01.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dilaog01.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dilaog01.setContentView(view);

        TextView pTextView = (TextView) view.findViewById(R.id.savetext);
        ImageView melonBtn = (ImageView) view.findViewById(R.id.melon);

        Glide.with(getActivity())
                .load(R.drawable.melonlogo)
                //.circleCrop()
                .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(getActivity(), 15, 0, RoundedCornersTransformation.CornerType.ALL)))
                //.error(R.drawable.gold)
                .into(melonBtn);

        melonBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sponsorType = StreamingSponsorType.MELON;
                setMusicPlay();
                dilaog01.dismiss();
            }
        });

        ImageView genieBtn = (ImageView) view.findViewById(R.id.genie);
        Glide.with(getActivity())
                .load(R.drawable.genielogo)
                //.circleCrop()
                .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(getActivity(), 15, 0, RoundedCornersTransformation.CornerType.ALL)))
                //.error(R.drawable.gold)
                .into(genieBtn);
        genieBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sponsorType = StreamingSponsorType.GENIE;
                setMusicPlay();
                dilaog01.dismiss();
            }
        });

        dilaog01.show();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.moreText:

            case R.id.img:

                Intent i = new Intent(getActivity(), RankMusicActivity.class);
                startActivity(i);

                break;

            case R.id.helpimg:

                View view = getLayoutInflater().inflate(R.layout.musichelpview, null);
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

            case R.id.helpimg1:

                View view1 = getLayoutInflater().inflate(R.layout.musichelpview1, null);
                BottomSheetDialog dialog1 = new BottomSheetDialog(getActivity(), R.style.AppBottomSheetDialogTheme); // Style here
                dialog1.setContentView(view1);

                Button closeBtn1 = (Button) view1.findViewById(R.id.close);
                closeBtn1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog1.dismiss();
                    }
                });

                dialog1.show();

                break;

            case R.id.addmusic:

                //Intent i = new Intent(getActivity(), AddMusicActivity.class);
                //startActivity(i);

                View view2 = getLayoutInflater().inflate(R.layout.addmusic_search, null);
                Dialog dilaog011= new Dialog(getActivity());
                dilaog011.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dilaog011.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dilaog011.setContentView(view2);

                RadioGroup group = (RadioGroup) view2.findViewById(R.id.radioGroup);
                musicType = "melon";

                group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId){
                            case R.id.r_btn1:
                                musicType = "melon";
                                break;
                            case R.id.r_btn2:
                                musicType = "genie";
                                break;

                        }
                    }
                });


                EditText musicText = (EditText) view2.findViewById(R.id.musicText);
                Button searchBtn = (Button) view2.findViewById(R.id.search);
                searchBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(musicText.length() == 0) {
                            SFUToast.showToast(getActivity(), "노래 제목을 입력해주세요.");
                        } else {
                            Intent i = new Intent(getActivity(), AddMusicActivity.class);
                            i.putExtra("musicTitle", musicText.getText().toString());
                            i.putExtra("musicType", musicType);
                            Log.e("kwonsaw",""+musicText.getText());
                            startActivityResult.launch(i);
                            //startActivity(i);
                            dilaog011.dismiss();
                        }
                    }
                });

                Button closeBtn2 = (Button) view2.findViewById(R.id.close);
                closeBtn2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dilaog011.dismiss();
                    }
                });

                dilaog011.show();

                break;


        }
    }

    /**
     /* 팝업공지
     **/

    public void getPopupNotice(){

        NetworkManager manager = new NetworkManager();
        manager.getPopupNotice(getOs(),  getVersion(), new Callback<GetPopupNoticeResultModel>() {
            @Override
            public void onResponse(Call<GetPopupNoticeResultModel> call, Response<GetPopupNoticeResultModel> response) {
                getPopupNoticeResultModel = response.body();

                if (response.isSuccessful()) {

                    if (getPopupNoticeResultModel.getResultCode().equalsIgnoreCase("200")) {
                        //오늘은 그만보기 했는지 체크
                        if(noticeCheck()) {
                            setPopupNotice(getPopupNoticeResultModel.getPopupNoticeList());
                        }

                        setNoticeDate(getPopupNoticeResultModel.getPopupNoticeList());
                    }

                } else {
                    SFUToast.showToast(AppAplication.context, Errorcode.ERROCODE("1001"));
                }
            }

            @Override
            public void onFailure(Call<GetPopupNoticeResultModel> call, Throwable t) {
                SFUToast.showToast(AppAplication.context, Errorcode.ERROCODE("1001"));
            }
        });

    }

    /**
     /* 송포유 추천곡, 회원 후원곡, 재생횟수 랭킹
     **/

    public void getMusic(String appVersion){

        NetworkManager manager = new NetworkManager();
        manager.getMusic(appVersion, (CommonUtil.mSharePrefreences(AppAplication.context, AppAplication.context.getString(R.string.share_userId), null, "guest")), new Callback<GetMusicResultModel>() {
            @Override
            public void onResponse(Call<GetMusicResultModel> call, Response<GetMusicResultModel> response) {
                getMusicResultModel = response.body();

                if(response.isSuccessful()) {

                    if (getMusicResultModel.getResultCode().equalsIgnoreCase("200")) {

                        setMusicData(getMusicResultModel.getMusic());
                        setMusicListData(getMusicResultModel.getMusiclist());
                        setMusicRankListData(getMusicResultModel.getRanklist());

                    } else if (getMusicResultModel.getResultCode().equalsIgnoreCase("826")) {
                        SFUToast.showToast(AppAplication.context, Errorcode.ERROCODE(getMusicResultModel.getResultCode()));
                        CommonUtil.mSharePrefreences(getActivity(), getString(R.string.share_userId), "guest", null);
                        Intent i = new Intent(getActivity() , LoginActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK );
                        startActivity(i);
                    } else {
                        SFUToast.showToast(AppAplication.context, Errorcode.ERROCODE(getMusicResultModel.getResultCode()));
                    }

                } else {
                    SFUToast.showToast(AppAplication.context, Errorcode.ERROCODE("1001"));
                }

            }

            @Override
            public void onFailure(Call<GetMusicResultModel> call, Throwable t) {
                SFUToast.showToast(AppAplication.context, Errorcode.ERROCODE("1001"));
            }
        });

    }

    /**
     /* 송포유 추천곡 포인트 적립
     **/

    public void setMusicPoint(String appVersion){

        //getAESstring(CommonUtil.mSharePrefreences(AppAplication.context, AppAplication.context.getString(R.string.share_userId), null, "guest"))

        String uidText = musicArrayList.get(0).getTitle()+"/"+musicArrayList.get(0).getSinger();

        NetworkManager manager = new NetworkManager();
        manager.setMusicPoint(appVersion, (CommonUtil.mSharePrefreences(AppAplication.context, AppAplication.context.getString(R.string.share_userId), null, "guest")), musicId, saveMoney, "sMusic", uidText,  new Callback<SetMusicPointResultModel>() {
            @Override
            public void onResponse(Call<SetMusicPointResultModel> call, Response<SetMusicPointResultModel> response) {
                setMusicPointResultModel = response.body();

                if(response.isSuccessful()) {

                    if (setMusicPointResultModel.getResultCode().equalsIgnoreCase("200")) {

                        streamingSaveUpService.notifyStreaming(AppAplication.context.getString(R.string.app_name), AppAplication.context.getString(R.string.streaming_save_up_fail_notify_success));
                        //startActivity(SavePopupView.newIntent(AppAplication.context, null, ""));

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
     /* 음원 잔여재생, 프리퀀시 확인
     **/

    public void getCheckMusic(){

        NetworkManager manager = new NetworkManager();
        manager.getCheckMusic(getVersion(), (CommonUtil.mSharePrefreences(AppAplication.context, AppAplication.context.getString(R.string.share_userId), null, "guest")), getMusicResultModel.getMusic().get(0).getMusicId(), "sMusic", new Callback<GetCheckMusicResultModel>() {
            @Override
            public void onResponse(Call<GetCheckMusicResultModel> call, Response<GetCheckMusicResultModel> response) {
                getCheckMusicResultModel = response.body();

                if(response.isSuccessful()) {

                    if (getCheckMusicResultModel.getResultCode().equalsIgnoreCase("200")) {
                        palyerPopup();
                        //setMusicPlay();
                    } else {
                        SFUAlertDialog.singleButtonShowAlert(getActivity(), null, Errorcode.ERROCODE(getCheckMusicResultModel.getResultCode()), null);
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
     /* 회원 후원곡
     **/

    public void getMusicList(String appVersion){

        NetworkManager manager = new NetworkManager();
        manager.getMusicList(appVersion, (CommonUtil.mSharePrefreences(AppAplication.context, AppAplication.context.getString(R.string.share_userId), null, "guest")), new Callback<GetMusicListResultModel>() {
            @Override
            public void onResponse(Call<GetMusicListResultModel> call, Response<GetMusicListResultModel> response) {
                getMusicListResultModel = response.body();

                if(response.isSuccessful()) {

                    if (getMusicListResultModel.getResultCode().equalsIgnoreCase("200")) {

                        //setMusicData(getMusicListResultModel.getMusiclist());

                    } else {
                        SFUToast.showToast(AppAplication.context, Errorcode.ERROCODE(getMusicListResultModel.getResultCode()));
                    }

                } else {
                    SFUToast.showToast(AppAplication.context, Errorcode.ERROCODE("1001"));
                }

            }

            @Override
            public void onFailure(Call<GetMusicListResultModel> call, Throwable t) {
                SFUToast.showToast(AppAplication.context, Errorcode.ERROCODE("1001"));
            }
        });

    }

    /* 음원 등록하고 돌아왔을 경우 */
    ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        getMusic(getVersion());
                    }
                }
            });

    @Override
    public void complete(Boolean isSuccess, String no, StreamingSponsorType sponsorType) {
        if (isSuccess) {
            Log.e("kwonsaw","isSuccess");
            setMusicPoint(getVersion());
            //activeVote(no, sponsorType);
        } else {
            Log.e("kwonsaw","isSuccess_nononono");
            //LoginUtil.build().setUserInfoModelAPI(LoginUtil.build().getUserId(), null);
            //finish();
        }
    }

}
