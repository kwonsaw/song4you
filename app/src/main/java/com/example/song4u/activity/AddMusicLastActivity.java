package com.example.song4u.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.song4u.Application.AppAplication;
import com.example.song4u.R;
import com.example.song4u.Util.CommonUtil;
import com.example.song4u.Util.Errorcode;
import com.example.song4u.Util.RoundedCornersTransformation;
import com.example.song4u.Util.SFUAlertDialog;
import com.example.song4u.Util.SFUToast;
import com.example.song4u.databinding.AddmusiclastactivityBinding;
import com.example.song4u.network.NetworkManager;
import com.example.song4u.network.resultmodel.GetUserInfoDataResultModel;
import com.example.song4u.network.resultmodel.GetUserInfoResultModel;
import com.example.song4u.network.resultmodel.SetAddMusicResultModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddMusicLastActivity extends Season2BaseActivity implements View.OnClickListener {

    private AddmusiclastactivityBinding binding;

    private HashMap<String, String> musicInfoMap = new HashMap<>();

    private GetUserInfoResultModel getUserInfoResultModel;
    private GetUserInfoDataResultModel getUserInfoDataResultModel;
    private SetAddMusicResultModel setAddMusicResultModel;

    private int maxCnt = 1;

    private boolean isadd = false;
    private boolean isremove = false;

    private String playcntText = "1", removeMoneyText = "50";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AddmusiclastactivityBinding.inflate(getLayoutInflater());
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
        if (musicInfoMap.containsKey("title")) {

            stopAddmusic();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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

        musicInfoMap = (HashMap<String, String>) getIntent().getSerializableExtra("musicinfo");

        getUserInfo(CommonUtil.mSharePrefreences(AddMusicLastActivity.this, getString(R.string.share_userId), null, "guest"));

        Glide.with(AddMusicLastActivity.this)
                .load(musicInfoMap.get("image"))
                .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(getApplication(),30,0, RoundedCornersTransformation.CornerType.ALL)))
                .transition(new DrawableTransitionOptions().crossFade())
                .error(R.drawable.ic_launcher_foreground)
                .into(binding.mImage);

        String ustr = musicInfoMap.get("title")+"\n"+musicInfoMap.get("singer");
        SpannableStringBuilder sb1 = new SpannableStringBuilder(ustr);
        sb1.setSpan(new ForegroundColorSpan(Color.DKGRAY), musicInfoMap.get("title").length(), ustr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb1.setSpan(new StyleSpan(Typeface.BOLD), 0, musicInfoMap.get("title").length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        binding.sText.setText(sb1);

        //binding.sText.setText(musicInfoMap.get("title"));
        //binding.remove.setOnClickListener(this);
        //binding.add.setOnClickListener(this);

        binding.add.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                handler1_up.post(runnable1_up);
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

        binding.addmusic.setOnClickListener(this);
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
        mtxt.setVisibility(View.GONE);
        mIcon.setVisibility(View.GONE);

        mTitle.setText("재생횟수 후원 (2 / 2)");
        mtxt.setText("도움말");

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
                BottomSheetDialog dialog = new BottomSheetDialog(AddMusicLastActivity.this, R.style.AppBottomSheetDialogTheme); // Style here
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

            case R.id.remove:

                if (binding.cText.getText().equals("1")) {
                    SFUAlertDialog.singleButtonShowAlert(AddMusicLastActivity.this, null, "최소 1회는 필수입니다.", null);
                } else if (binding.cText.getText().equals("0")) {
                    SFUAlertDialog.singleButtonShowAlert(AddMusicLastActivity.this, null, "포인트가 부족하여 후원할 수 없습니다.", null);
                } else {
                    int cNum = Integer.parseInt(binding.cText.getText().toString());
                    cNum = cNum - 1;
                    int mNum = cNum*50;
                    removeMoneyText = String.valueOf(mNum);
                    binding.cText.setText(String.valueOf(cNum));
                    binding.mText.setText("-"+ getNumberFormat(mNum)+"P");
                }

                break;

            case R.id.add:

                int cNum = Integer.parseInt(binding.cText.getText().toString());
                String cText = String.valueOf(cNum);

                if(cNum == maxCnt) {
                    SFUAlertDialog.singleButtonShowAlert(AddMusicLastActivity.this, null, "최대 "+cText+"회까지 가능합니다.", null);
                } else if (binding.cText.getText().equals("0")) {
                    SFUAlertDialog.singleButtonShowAlert(AddMusicLastActivity.this, null, "포인트가 부족하여 후원할 수 없습니다.", null);
                } else {

                    cNum = cNum + 1;
                    int mNum = cNum*50;
                    removeMoneyText = String.valueOf(mNum);
                    binding.cText.setText(String.valueOf(cNum));
                    binding.mText.setText("-"+getNumberFormat(mNum)+"P");

                }

                break;

            case R.id.addmusic:

                if (binding.cText.getText().equals("0")) {
                    SFUAlertDialog.singleButtonShowAlert(AddMusicLastActivity.this, null, "포인트가 부족하여 음원 등록을 할 수 없습니다.", null);
                } else {

                    String msg = binding.mText.getText().toString()+" 차감하여 재생횟수 "+binding.cText.getText().toString()+"회가 후원됩니다. 음원등록을 하시겠습니까?";
                    SFUAlertDialog.doubleButtonShowAlert(AddMusicLastActivity.this, "알림", msg, "취소",null, "등록", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            playcntText = binding.cText.getText().toString();
                            setAddMusic(getVersion(), playcntText, removeMoneyText);

                        }
                    });

                }

                break;
        }

    }

    public void setAddPlay() {
        int cNum = Integer.parseInt(binding.cText.getText().toString());
        String cText = String.valueOf(cNum);

        if(cNum == maxCnt) {
            isadd = true;
            SFUAlertDialog.singleButtonShowAlert(AddMusicLastActivity.this, null, "최대 "+cText+"회까지 가능합니다.", null);
        } else if (binding.cText.getText().equals("0")) {
            isadd = true;
            SFUAlertDialog.singleButtonShowAlert(AddMusicLastActivity.this, null, "포인트가 부족하여 후원할 수 없습니다.", null);
        } else {

            cNum = cNum + 1;
            int mNum = cNum*50;
            removeMoneyText = String.valueOf(mNum);
            binding.cText.setText(String.valueOf(cNum));
            binding.mText.setText("-"+getNumberFormat(mNum)+"P");

        }
    }

    public void setRemovePlay() {
        if (binding.cText.getText().equals("1")) {
            isremove = true;
            SFUAlertDialog.singleButtonShowAlert(AddMusicLastActivity.this, null, "최소 1회는 필수입니다.", null);
        } else if (binding.cText.getText().equals("0")) {
            isremove = true;
            SFUAlertDialog.singleButtonShowAlert(AddMusicLastActivity.this, null, "포인트가 부족하여 후원할 수 없습니다.", null);
        } else {
            int cNum = Integer.parseInt(binding.cText.getText().toString());
            cNum = cNum - 1;
            int mNum = cNum*50;
            removeMoneyText = String.valueOf(mNum);
            binding.cText.setText(String.valueOf(cNum));
            binding.mText.setText("-"+ getNumberFormat(mNum)+"P");
        }
    }

    public void stopAddmusic() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("음원 등록을 중단하고 나가시겠습니까?");
        dialog.setPositiveButton("나가기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });

        dialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        dialog.show();

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

                        //setUserData(getUserInfoResultModel.getUserinfo());
                        int numInt = getUserInfoResultModel.getPoint();
                        if (numInt < 50) {

                            AlertDialog.Builder dialog = new AlertDialog.Builder(AddMusicLastActivity.this);
                            dialog.setMessage("포인트가 부족하여 음원 등록을 할 수 없습니다. 최소 50P가 있어야 재생횟수 후원이 가능합니다. 포인트를 쌓은 후에 음원 등록을 진행해주세요.");
                            dialog.setPositiveButton("나가기", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    finish();
                                }
                            });

                            dialog.show();

                            binding.pText.setText("보유 포인트 : " + getNumberFormat(numInt) + "P");

                            binding.vText.setTextColor(Color.parseColor("#FF0000"));
                            binding.vText.setText("포인트가 부족하여 음원 등록을 할 수 없습니다. 최소 50P가 있어야 재생횟수 후원이 가능합니다. 포인트를 쌓은 후에 음원 등록을 진행해주세요.");

                            binding.cText.setTextColor(Color.parseColor("#FF0000"));
                            binding.cText.setText("0");

                        } else {
                            binding.pText.setText("보유 포인트 : " + getNumberFormat(numInt) + "P");

                            numInt = (numInt / 50) * 50;
                            maxCnt = (numInt / 50);

                            binding.vText.setText(getNumberFormat(numInt) + "P");

                            binding.mText.setTextColor(Color.parseColor("#FF0000"));
                            binding.mText.setText("-50P");

                            binding.cText.setText("1");
                        }


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
         /* 음원 등록
         **/

        public void setAddMusic(String appVersion, String play_cnt, String removeMoney){

            NetworkManager manager = new NetworkManager();
            manager.setAddMusic(appVersion, getUserId(), musicInfoMap.get("title"), musicInfoMap.get("singer"),  musicInfoMap.get("image"), musicInfoMap.get("type"), musicInfoMap.get("songid"), play_cnt,removeMoney,new Callback<SetAddMusicResultModel>() {
                @Override
                public void onResponse(Call<SetAddMusicResultModel> call, Response<SetAddMusicResultModel> response) {
                    setAddMusicResultModel = response.body();

                    if (response.isSuccessful()) {

                        if (setAddMusicResultModel.getResultCode().equalsIgnoreCase("200")) {

                            //SFUAlertDialog.singleButtonShowAlert(AddMusicLastActivity.this, "알림", "음원이 정상적으로 등록되었습니다.", null);

                            /*
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddMusicLastActivity.this);
                            builder.setTitle("알림").setMessage("음원이 정상적으로 등록되었습니다.");
                            builder.setPositiveButton("확인", new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int id)
                                {
                                    finish();
                                }
                            });


                            AlertDialog alertDialog = builder.create();
                            alertDialog.setOnShowListener( new DialogInterface.OnShowListener() {
                                @Override
                                public void onShow(DialogInterface arg0) {
                                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(AddMusicLastActivity.this.getResources().getColor(R.color.songcolor));
                                }
                            });
                            alertDialog.show();
                            */

                            SFUToast.showToast(AppAplication.context, "음원이 정상적으로 등록되었습니다.");

                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);

                            finish();

                        } else {

                            SFUAlertDialog.singleButtonShowAlert(AddMusicLastActivity.this, null, Errorcode.ERROCODE(setAddMusicResultModel.getResultCode()), null);
                            //SFUToast.showToast(AppAplication.context, Errorcode.ERROCODE(setAddMusicResultModel.getResultCode()));
                        }

                    } else {
                        SFUToast.showToast(AppAplication.context, Errorcode.ERROCODE("1001"));
                    }

                }

                @Override
                public void onFailure(Call<SetAddMusicResultModel> call, Throwable t) {
                    SFUToast.showToast(AppAplication.context, Errorcode.ERROCODE("1001"));
                }
            });

        }

}
