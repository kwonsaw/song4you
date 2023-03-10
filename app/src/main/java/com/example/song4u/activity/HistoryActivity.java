package com.example.song4u.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.example.song4u.Adapter.PointHistoryAdapter;
import com.example.song4u.R;
import com.example.song4u.Util.Errorcode;
import com.example.song4u.databinding.HistoryactivityBinding;
import com.example.song4u.network.NetworkManager;
import com.example.song4u.network.resultmodel.GetPointHistoryDataResultModel;
import com.example.song4u.network.resultmodel.GetPointHistoryResultModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryActivity extends Season2BaseActivity implements AbsListView.OnScrollListener {

    private HistoryactivityBinding binding;

    private GetPointHistoryResultModel getPointHistoryResultModel;
    private ArrayList<GetPointHistoryDataResultModel> mArrayList;
    private ListView listView;
    private PointHistoryAdapter pointHistoryAdapter;

    private int PageNum = 1;
    private int Totalpage = 1;
    private boolean loock = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = HistoryactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        initActionBar();

        getPointHistory(PageNum);

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

    public void init(){
        listView = binding.mListView;
        listView.setOnScrollListener(HistoryActivity.this);

        mArrayList = new ArrayList<>();
    }

    private void initActionBar() {

        mBtn.setVisibility(View.VISIBLE);
        TextCount.setVisibility(View.GONE);
        mTitle.setVisibility(View.VISIBLE);
        alarm.setVisibility(View.GONE);
        mIcon.setVisibility(View.GONE);

        mTitle.setText("포인트 적립/사용 내역");

        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(mActionBarBackgroundDrawable);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(ActionbarView);
        actionBar.setElevation(0);
    }

    /**
     * 적립내역 리스트 완료 후
     */
    public void setHistoryData(List<GetPointHistoryDataResultModel> list){

        if ( PageNum == 1){
            mArrayList.clear();
            mArrayList.addAll(list);
            pointHistoryAdapter = new PointHistoryAdapter(getApplicationContext(), mArrayList);
            listView.setAdapter(pointHistoryAdapter);

        }else {

            mArrayList.addAll(list);
            pointHistoryAdapter.notifyDataSetChanged();
        }


    }

    /**
     * 포인트 적립 내역
     */
    public void getPointHistory(int pageNum){

        String page = String.valueOf(pageNum);

        NetworkManager manager = new NetworkManager();
        manager.getPointHistory(getVersion(), getUserId(), page, new Callback<GetPointHistoryResultModel>() {
            @Override
            public void onResponse(Call<GetPointHistoryResultModel> call, Response<GetPointHistoryResultModel> response) {
                getPointHistoryResultModel = response.body();

                if (response.isSuccessful()) {

                    if (getPointHistoryResultModel.getResultCode().equalsIgnoreCase("200")) {

                        Totalpage = Integer.parseInt(getPointHistoryResultModel.getTotalPage());
                        setHistoryData(getPointHistoryResultModel.getHistorylist());
                        loock = false;

                    }

                } else {
                    showToast(Errorcode.ERROCODE("1001"));
                }
            }

            @Override
            public void onFailure(Call<GetPointHistoryResultModel> call, Throwable t) {
                showToast(Errorcode.ERROCODE("1001"));
            }
        });

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        int count = totalItemCount - visibleItemCount;
        if (firstVisibleItem >= count && totalItemCount != 0 && loock == false && PageNum < Totalpage) {
            loock = true;
            getPointHistory(++PageNum);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {}

}
