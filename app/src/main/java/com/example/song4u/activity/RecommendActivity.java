package com.example.song4u.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.song4u.R;
import com.example.song4u.Util.Errorcode;
import com.example.song4u.databinding.RecommendactivityBinding;
import com.example.song4u.network.NetworkManager;
import com.example.song4u.network.resultmodel.SetRecommendResultModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecommendActivity  extends Season2BaseActivity {

    private RecommendactivityBinding binding;

    private SetRecommendResultModel setRecommendResultModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = RecommendactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initActionBar();
        initView();


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

    public void initView() {

        binding.recommendText.requestFocus();

        /* 추천인 등록 */
        binding.recommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (binding.recommendText.getText().length() == 0) {
                    showToast("추천인 아이디를 입력해주세요.");
                } else {
                    setRecommend(getUserId(), binding.recommendText.getText().toString());
                }

            }
        });
    }

    private void initActionBar() {

        mBtn.setVisibility(View.VISIBLE);
        TextCount.setVisibility(View.GONE);
        mTitle.setVisibility(View.VISIBLE);
        alarm.setVisibility(View.GONE);
        alarm.setImageResource(R.drawable.ic_baseline_mode_24);
        mIcon.setVisibility(View.GONE);
        mTitle.setText("추천인 등록");

        //alarm.setOnClickListener(this);

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
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /* 적립 안내 팝업 */
    public void savePopup(String pText) {

        View view = getLayoutInflater().inflate(R.layout.savepopupview, null);
        Dialog dilaog01= new Dialog(RecommendActivity.this);
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

    /**
     * /* 추천인 등록
     **/

    public void setRecommend(String userid, String recommender) {

        NetworkManager manager = new NetworkManager();
        manager.setRecommend(getVersion(), userid, recommender, new Callback<SetRecommendResultModel>() {
            @Override
            public void onResponse(Call<SetRecommendResultModel> call, Response<SetRecommendResultModel> response) {
                setRecommendResultModel = response.body();

                if (response.isSuccessful()) {

                    if (setRecommendResultModel.getResultCode().equalsIgnoreCase("200")) {

                        savePopup(setRecommendResultModel.getSavemoney());
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);

                    } else {
                        showToastLong(Errorcode.ERROCODE(setRecommendResultModel.getResultCode()));
                    }

                } else {
                    showToast(Errorcode.ERROCODE("1001"));
                }

                //hideLoding();

            }

            @Override
            public void onFailure(Call<SetRecommendResultModel> call, Throwable t) {
                showToast(Errorcode.ERROCODE("1001"));
            }
        });

    }

}
