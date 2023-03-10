package com.example.song4u.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.song4u.Adapter.MusicSearchList_Adapter;
import com.example.song4u.R;
import com.example.song4u.Util.Errorcode;
import com.example.song4u.Util.RoundedCornersTransformation;
import com.example.song4u.Util.SFUToast;
import com.example.song4u.databinding.AddmusicactivityBinding;
import com.example.song4u.network.NetworkManager;
import com.example.song4u.network.resultmodel.GetAddMusicCheckResultModel;
import com.example.song4u.network.resultmodel.GetGenieParserDataResultModel;
import com.example.song4u.network.resultmodel.GetGenieParserResultModel;
import com.example.song4u.network.resultmodel.GetGenieSearchResultModel;
import com.example.song4u.network.resultmodel.GetMelonParserDataResultModel;
import com.example.song4u.network.resultmodel.GetMelonParserResultModel;
import com.example.song4u.network.resultmodel.GetMelonSearchDataResultModel;
import com.example.song4u.network.resultmodel.GetMelonSearchResultModel;
import com.example.song4u.network.resultmodel.GetMusicInfoDataResultModel;
import com.example.song4u.network.resultmodel.GetUserInfoResultModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddMusicActivity extends Season2BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private AddmusicactivityBinding binding;

    private GetUserInfoResultModel getUserInfoResultModel;
    private GetAddMusicCheckResultModel getAddMusicCheckResultModel;

    private GetMelonParserResultModel getMelonParserResultModel;
    private GetMelonSearchResultModel getMelonSearchResultModel;
    private GetGenieParserResultModel getGenieParserResultModel;
    private GetGenieSearchResultModel getGenieSearchResultModel;

    private ArrayList<GetMelonSearchDataResultModel> mArrayList;
    private ListView listView;
    private MusicSearchList_Adapter musicSearchList_Adapter;

    private SearchView searchView;
    private InputMethodManager imm;

    private String musicTitle, musicType, title, singer, mSongid, gSongid, playTime, imageurl, userPoint;
    private WebView webview;
    private String melonSearchUrl = "https://search.melon.com/search/searchMcom.htm?s=%s";
    //private String melonSearchUrl = "https://www.melon.com/search/song/index.htm?q=%s";
    private String melonSongidUrl = "https://m2.melon.com/song/lyrics.htm?songId";

    private boolean isMelonsearch = true;

    private HashMap<String,String> musicInfoMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AddmusicactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        musicTitle = getIntent().getStringExtra("musicTitle");
        musicType = getIntent().getStringExtra("musicType");

        init();
        initActionBar();

        if (musicType.equalsIgnoreCase("melon")) {
            getMelonSearch(musicTitle);
        } else {
            getGenieSearch(musicTitle);
        }


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

        if (musicInfoMap.containsKey("title")) {

            stopAddmusic();
            return;
        }

        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:

                if (musicInfoMap.containsKey("title")) {

                    stopAddmusic();
                    return false;
                } else {
                    finish();
                }

                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public void init() {

        listView = binding.mListView;
        //final View header = getLayoutInflater().inflate(R.layout.list_header_more, null, false) ;
        //listView.addHeaderView(header);

        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        searchView = binding.search;
        searchView.requestFocusFromTouch();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                // 입력받은 문자열 처리
                if (isMelonsearch) {
                    getMelonSearch(s);
                } else {
                    getGenieSearch(s);
                }
                searchView.clearFocus();
                imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                // 입력란의 문자열이 바뀔 때 처리
                return false;
            }
        });


        mArrayList = new ArrayList<>();

    }

    private void initActionBar() {

        mBtn.setVisibility(View.VISIBLE);
        TextCount.setVisibility(View.GONE);
        mTitle.setVisibility(View.VISIBLE);
        alarm.setVisibility(View.GONE);
        mtxt.setVisibility(View.VISIBLE);
        mIcon.setVisibility(View.GONE);

        mTitle.setText("음원 검색 (1 / 2)");
        mtxt.setText("음원신청");

        mtxt.setOnClickListener(this);

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

            case R.id.maincion_btn_txt:
                View view = getLayoutInflater().inflate(R.layout.addmusic_helpview, null);
                BottomSheetDialog dialog = new BottomSheetDialog(AddMusicActivity.this, R.style.AppBottomSheetDialogTheme); // Style here
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {
            case R.id.mListView:

                searchView.clearFocus();
                getAddMusicCheck(mArrayList.get(position).getSongid(), musicType, position);

                break;
        }

    }

    public void stopAddmusic() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("음원 등록을 중단하고 나가시겠습니까?");
        dialog.setPositiveButton("나가기", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                finish();
            }
        });

        dialog.setNegativeButton("취소", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {

            }
        });

        dialog.show();

    }

    public void popupMusicConfirm(List<GetMelonParserDataResultModel> list) {

        View view2 = getLayoutInflater().inflate(R.layout.addmusic_confirm, null);
        Dialog dilaog011= new Dialog(AddMusicActivity.this);
        dilaog011.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dilaog011.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dilaog011.setContentView(view2);

        ImageView mImage = (ImageView) view2.findViewById(R.id.mImage);
        Glide.with(this)
                .load(list.get(0).getImage())
                .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(getApplication(),20,0, RoundedCornersTransformation.CornerType.ALL)))
                //.error(R.drawable.com_facebook_profile_picture_blank_portrait)
                .into(mImage);

        TextView mText = (TextView) view2.findViewById(R.id.mText);
        mText.setText("멜론 음원 확인");

        TextView singerText = (TextView) view2.findViewById(R.id.singer);
        singerText.setText(list.get(0).getSinger());

        TextView titleText = (TextView) view2.findViewById(R.id.title);
        titleText.setText(list.get(0).getTitle());

        Button searchBtn = (Button) view2.findViewById(R.id.search);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                musicInfoMap.put("title", list.get(0).getTitle());
                musicInfoMap.put("singer", list.get(0).getSinger());
                musicInfoMap.put("songid", list.get(0).getSongid());
                musicInfoMap.put("image", list.get(0).getImage());
                musicInfoMap.put("type",musicType);
                Log.e("kwonsaw",""+musicInfoMap);

                Intent i = new Intent(AddMusicActivity.this, AddMusicLastActivity.class);
                i.putExtra("musicinfo", musicInfoMap);
                startActivity(i);

                Intent intent = new Intent();
                setResult(RESULT_OK, intent);

                finish();

                dilaog011.dismiss();

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

    }

    public void popupGenieMusicConfirm(List<GetGenieParserDataResultModel> list) {

        View view2 = getLayoutInflater().inflate(R.layout.addmusic_confirm, null);
        Dialog dilaog011= new Dialog(AddMusicActivity.this);
        dilaog011.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dilaog011.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dilaog011.setContentView(view2);

        ImageView mImage = (ImageView) view2.findViewById(R.id.mImage);
        Glide.with(this)
                .load(list.get(0).getImage())
                .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(getApplication(),20,0, RoundedCornersTransformation.CornerType.ALL)))
                //.error(R.drawable.com_facebook_profile_picture_blank_portrait)
                .into(mImage);

        TextView mText = (TextView) view2.findViewById(R.id.mText);
        mText.setText("지니 음원 확인");

        TextView singerText = (TextView) view2.findViewById(R.id.singer);
        singerText.setText(list.get(0).getSinger());

        TextView titleText = (TextView) view2.findViewById(R.id.title);
        titleText.setText(list.get(0).getTitle());

        Button searchBtn = (Button) view2.findViewById(R.id.search);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                musicInfoMap.put("title", list.get(0).getTitle());
                musicInfoMap.put("singer", list.get(0).getSinger());
                musicInfoMap.put("songid", list.get(0).getSongid());
                musicInfoMap.put("image", list.get(0).getImage());
                musicInfoMap.put("type",musicType);
                Log.e("kwonsaw",""+musicInfoMap);

                Intent i = new Intent(AddMusicActivity.this, AddMusicLastActivity.class);
                i.putExtra("musicinfo", musicInfoMap);
                startActivity(i);
                finish();

                dilaog011.dismiss();

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

    }

    /* 이미 등록된 음원인 경우 해당 곡 정보로 이동 */
    public void popupMusicCheck(List<GetMusicInfoDataResultModel> list) {

        View view2 = getLayoutInflater().inflate(R.layout.addmusic_check, null);
        Dialog dilaog011= new Dialog(AddMusicActivity.this);
        dilaog011.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dilaog011.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dilaog011.setContentView(view2);

        ImageView mImage = (ImageView) view2.findViewById(R.id.mImage);
        Glide.with(this)
                .load(list.get(0).getMainimgurl())
                .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(getApplication(),20,0, RoundedCornersTransformation.CornerType.ALL)))
                //.error(R.drawable.com_facebook_profile_picture_blank_portrait)
                .into(mImage);

        TextView mText = (TextView) view2.findViewById(R.id.mText);
        mText.setText("잠깐만요!");

        TextView dText = (TextView) view2.findViewById(R.id.dText);

        String ustr = "이미 포미 후원곡에 등록되어있는 음원입니다.\n다음을 누르면 해당 곡 정보로 이동하여 음원 후원을 하실 수 있습니다.";
        SpannableStringBuilder sb1 = new SpannableStringBuilder(ustr);
        sb1.setSpan(new ForegroundColorSpan(Color.RED), 0, 24, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        dText.setText(sb1);

        TextView singerText = (TextView) view2.findViewById(R.id.singer);
        singerText.setText(list.get(0).getSinger());

        TextView titleText = (TextView) view2.findViewById(R.id.title);
        titleText.setText(list.get(0).getTitle());

        Button searchBtn = (Button) view2.findViewById(R.id.search);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(AddMusicActivity.this, MusicDetailActivity.class);
                i.putExtra("musicid", list.get(0).getMusicId());
                i.putExtra("songid", list.get(0).getSongid());
                startActivity(i);

                finish();
                dilaog011.dismiss();

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

    }

    /**
     * 검색결과 리스트 완료 후
     */
    public void setMusicSearchData(List<GetMelonSearchDataResultModel> list){

        mArrayList.clear();
        mArrayList.addAll(list);
        musicSearchList_Adapter = new MusicSearchList_Adapter(getApplicationContext(), mArrayList);
        listView.setAdapter(musicSearchList_Adapter);
        listView.setOnItemClickListener(this);

    }

    /**
     /* 멜론 음원 검색
     **/

    public void getMelonSearch(String title){

        NetworkManager manager = new NetworkManager();
        manager.getMelonSearch(title, new Callback<GetMelonSearchResultModel>() {
            @Override
            public void onResponse(Call<GetMelonSearchResultModel> call, Response<GetMelonSearchResultModel> response) {
                getMelonSearchResultModel = response.body();

                if (response.isSuccessful()) {

                    if (getMelonSearchResultModel.getResultCode().equalsIgnoreCase("200")) {
                        setMusicSearchData(getMelonSearchResultModel.getList());
                        binding.tText.setVisibility(View.GONE);
                        binding.tText.setText("아래 검색 결과에서 원하는 곡을 선택해주세요.\n이미 등록되어 있는 곡은 해당 곡 정보로 이동하여 음원 후원을 해주세요.");
                    } else {
                        mArrayList.clear();
                        musicSearchList_Adapter = new MusicSearchList_Adapter(getApplicationContext(), mArrayList);
                        listView.setAdapter(musicSearchList_Adapter);
                        binding.tText.setVisibility(View.VISIBLE);
                        binding.tText.setText("검색 결과가 없습니다. 다른 노래 제목으로 검색해주세요.");
                        //SFUToast.showToast(AddMusicActivity.this, Errorcode.ERROCODE(getMelonSearchResultModel.getResultCode()));
                        searchView.setVisibility(View.VISIBLE);
                        //SFUAlertDialog.singleButtonShowAlert(AddMusicActivity.this, null, "검색 결과가 없습니다. 다른 노래 제목으로 검색해주세요.", null);
                    }

                } else {
                    SFUToast.showToast(AddMusicActivity.this, Errorcode.ERROCODE("1001"));
                }


            }

            @Override
            public void onFailure(Call<GetMelonSearchResultModel> call, Throwable t) {
                SFUToast.showToast(AddMusicActivity.this, Errorcode.ERROCODE("1001"));

            }
        });

    }

    /**
     /* 멜론 음원 확인
     **/

    public void getMelonParser(String songid){

        NetworkManager manager = new NetworkManager();
        manager.getMelonParser(songid, new Callback<GetMelonParserResultModel>() {
            @Override
            public void onResponse(Call<GetMelonParserResultModel> call, Response<GetMelonParserResultModel> response) {
                getMelonParserResultModel = response.body();

                if (response.isSuccessful()) {

                    if (getMelonParserResultModel.getResultCode().equalsIgnoreCase("200")) {
                        popupMusicConfirm(getMelonParserResultModel.getList());
                    } else {
                        SFUToast.showToast(AddMusicActivity.this, Errorcode.ERROCODE(getMelonParserResultModel.getResultCode()));
                    }

                } else {
                    SFUToast.showToast(AddMusicActivity.this, Errorcode.ERROCODE("1001"));
                }


            }

            @Override
            public void onFailure(Call<GetMelonParserResultModel> call, Throwable t) {
                SFUToast.showToast(AddMusicActivity.this, Errorcode.ERROCODE("1001"));
                
            }
        });

    }

    /**
     /* 지니 음원 검색
     **/

    public void getGenieSearch(String title){

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor()).build();

        NetworkManager manager = new NetworkManager();
        manager.getGenieSearch(title, new Callback<GetGenieSearchResultModel>() {
            @Override
            public void onResponse(Call<GetGenieSearchResultModel> call, Response<GetGenieSearchResultModel> response) {
                getGenieSearchResultModel = response.body();

                if (response.isSuccessful()) {

                    if (getGenieSearchResultModel.getResultCode().equalsIgnoreCase("200")) {
                        setMusicSearchData(getGenieSearchResultModel.getList());
                        //mTitle.setText("지니 음원 등록 (2 / 3)");
                        binding.tText.setVisibility(View.GONE);
                        binding.tText.setText("아래 검색 결과에서 원하는 곡을 선택해주세요.\n이미 등록되어 있는 곡은 해당 곡 정보로 이동하여 음원후원을 해주세요.");
                        isMelonsearch = false;

                    } else {
                        mArrayList.clear();
                        musicSearchList_Adapter = new MusicSearchList_Adapter(getApplicationContext(), mArrayList);
                        listView.setAdapter(musicSearchList_Adapter);
                        binding.tText.setVisibility(View.VISIBLE);
                        //mTitle.setText("지니 음원 등록 (2 / 3)");
                        searchView.setVisibility(View.VISIBLE);
                        binding.tText.setText("검색 결과가 없습니다. 다른 노래 제목으로 검색해주세요.");
                        //SFUToast.showToast(AddMusicActivity.this, Errorcode.ERROCODE(getGenieSearchResultModel.getResultCode()));
                    }

                } else {
                    SFUToast.showToast(AddMusicActivity.this, Errorcode.ERROCODE("1001"));
                }
            }

            @Override
            public void onFailure(Call<GetGenieSearchResultModel> call, Throwable t) {
                SFUToast.showToast(AddMusicActivity.this, Errorcode.ERROCODE("1001"));
            }
        });

    }

    /**
     /* 지니 음원 확인
     **/

    public void getGenieParser(String songid){

        NetworkManager manager = new NetworkManager();
        manager.getGenieParser(songid, new Callback<GetGenieParserResultModel>() {
            @Override
            public void onResponse(Call<GetGenieParserResultModel> call, Response<GetGenieParserResultModel> response) {
                getGenieParserResultModel = response.body();

                if (response.isSuccessful()) {

                    if (getGenieParserResultModel.getResultCode().equalsIgnoreCase("200")) {
                        popupGenieMusicConfirm(getGenieParserResultModel.getList());
                    } else {
                        SFUToast.showToast(AddMusicActivity.this, Errorcode.ERROCODE(getGenieParserResultModel.getResultCode()));
                    }

                } else {
                    SFUToast.showToast(AddMusicActivity.this, Errorcode.ERROCODE("1001"));
                }
            }

            @Override
            public void onFailure(Call<GetGenieParserResultModel> call, Throwable t) {
                SFUToast.showToast(AddMusicActivity.this, Errorcode.ERROCODE("1001"));
            }
        });

    }

    /**
     * /* 사용자 정보
     **/

    public void getUserInfo(String userid) {

        NetworkManager manager = new NetworkManager();
        manager.getUserInfo(userid, new Callback<GetUserInfoResultModel>() {
            @Override
            public void onResponse(Call<GetUserInfoResultModel> call, Response<GetUserInfoResultModel> response) {
                getUserInfoResultModel = response.body();

                if (response.isSuccessful()) {

                    if (getUserInfoResultModel.getResultCode().equalsIgnoreCase("200")) {

                        userPoint = String.valueOf(getUserInfoResultModel.getPoint());
                        int numInt = getUserInfoResultModel.getPoint();
                        int maxCnt = (numInt / 50);

                        //String desc = "신청인 아이디: "+getUserId()+"\n보유포인트: "+numInt+"P로 최대"+maxCnt+"회 후원 가능합니다.\n\n"+"· 아래 노래제목과 가수명, 재생횟수 후원(사용포인트 또는 재생횟수), 재생 플레이어(멜론 또는 지니)를 정확히 입력 후 메일을 보내주세요. 음원등록이 완료되면 전달주신 mail주소로 등록완료 메일을 보내드립니다.\n재생횟수 1회당 보유포인트에서 50P가 차감됩니다.\n\n노래제목:\n가수:\n재생횟수 후원:\n재생 플레이어:";
                        String desc = "<p>아래 노래제목과 가수, 재생횟수 (1회당 50포인트 차감), 재생앱 (멜론 또는 지니)을 입력해서 보내주세요. 관리자가 대신 음원을 등록해 드립니다.</p>\n" +
                                "<p><br></p>\n" + "신청인 아이디 : " + getUserId() +"<br>" + "보유 포인트 : " +numInt+"P로 최대"+maxCnt+"회 후원 가능합니다.\n\n" + "<p><br></p>" +
                                "노래제목: \n" + "<br>" +
                                "가수: \n" + "<br>" +
                                "재생횟수: \n" + "<br>" +
                                "재생앱: "+"<br>";

                        Intent Email = new Intent(Intent.ACTION_SEND);
                        Email.setType("text/html");
                        Email.setPackage("com.google.android.gm");
                        Email.putExtra(Intent.EXTRA_EMAIL, new String[] { "song4u2022@gmail.com" });
                        Email.putExtra(Intent.EXTRA_SUBJECT, "음원등록 신청");
                        //Email.putExtra(Intent.EXTRA_TEXT, desc);
                        Email.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(desc));
                        startActivity(Intent.createChooser(Email, "음원등록 신청"));


                    } else {
                        showToastLong(Errorcode.ERROCODE(getUserInfoResultModel.getResultCode()));
                    }

                } else {
                    showToast(Errorcode.ERROCODE("1001"));
                }

                //hideLoding();

            }

            @Override
            public void onFailure(Call<GetUserInfoResultModel> call, Throwable t) {
                showToast(Errorcode.ERROCODE("1001"));
            }
        });

    }

    /**
     * /* 음원등록되어있는지 여부 체크
     **/

    public void getAddMusicCheck(String songid, String type, int pos) {

        NetworkManager manager = new NetworkManager();
        manager.getAddMusicCheck(getVersion(), getUserId(), songid, type, new Callback<GetAddMusicCheckResultModel>() {
            @Override
            public void onResponse(Call<GetAddMusicCheckResultModel> call, Response<GetAddMusicCheckResultModel> response) {
                getAddMusicCheckResultModel = response.body();

                if (response.isSuccessful()) {

                    if (getAddMusicCheckResultModel.getResultCode().equalsIgnoreCase("200")) {

                        if (isMelonsearch) {
                            getMelonParser(mArrayList.get(pos).getSongid());
                        } else {
                            getGenieParser(mArrayList.get(pos).getSongid());
                        }

                    } else if (getAddMusicCheckResultModel.getResultCode().equalsIgnoreCase("709")) {

                        popupMusicCheck(getAddMusicCheckResultModel.getMusicinfo());

                    } else {
                        showToastLong(Errorcode.ERROCODE(getAddMusicCheckResultModel.getResultCode()));
                    }

                } else {
                    showToast(Errorcode.ERROCODE("1001"));
                }

                //hideLoding();

            }

            @Override
            public void onFailure(Call<GetAddMusicCheckResultModel> call, Throwable t) {
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
