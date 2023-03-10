package com.example.song4u.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ExpandableListView;

import com.example.song4u.Adapter.FaqAdapter;
import com.example.song4u.Adapter.NoticeAdapter;
import com.example.song4u.R;
import com.example.song4u.Util.Errorcode;
import com.example.song4u.databinding.NoticeactivityBinding;
import com.example.song4u.network.NetworkManager;
import com.example.song4u.network.resultmodel.GetFaqDataResultModel;
import com.example.song4u.network.resultmodel.GetFaqResultModel;
import com.example.song4u.network.resultmodel.GetNoticeDataResultModel;
import com.example.song4u.network.resultmodel.GetNoticeResultModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoticeActivity extends Season2BaseActivity implements AbsListView.OnScrollListener {

    private NoticeactivityBinding binding;

    private GetNoticeResultModel getNoticeResultModel;
    private GetFaqResultModel getFaqResultModel;

    private ArrayList<GetNoticeDataResultModel> mArrayList;
    private ArrayList<GetFaqDataResultModel> mFaqArrayList;
    private ExpandableListView listView;
    private NoticeAdapter noticeAdapter;
    private FaqAdapter faqAdapter;

    private int PageNum = 1;
    private int Totalpage = 1;
    private boolean loock = false;
    private String type = "001";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = NoticeactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        initActionBar();

        //type = getIntent().getStringExtra("type");

        if("q".equalsIgnoreCase(getIntent().getStringExtra("type"))) {
           getFaq(PageNum, type);
        } else {
            getNotice(PageNum, type);
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
        listView.setOnScrollListener(NoticeActivity.this);

        mArrayList = new ArrayList<>();
        mFaqArrayList = new ArrayList<>();
    }

    private void initActionBar() {

        mBtn.setVisibility(View.VISIBLE);
        TextCount.setVisibility(View.GONE);
        mTitle.setVisibility(View.VISIBLE);
        alarm.setVisibility(View.GONE);
        mIcon.setVisibility(View.GONE);

        if("n".equalsIgnoreCase(getIntent().getStringExtra("type"))) {
            mTitle.setText("공지사항");
        } else if ("e".equalsIgnoreCase(getIntent().getStringExtra("type"))) {
            mTitle.setText("이벤트");
        } else {
            mTitle.setText("자주 묻는 질문");
        }


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
     * 공지사항 리스트 파싱 완료 후
     */
    public void setNoticeData(List<GetNoticeDataResultModel> list){

        if ( PageNum == 1){
            mArrayList.clear();
            mArrayList.addAll(list);
            noticeAdapter = new NoticeAdapter(getApplicationContext(), mArrayList);
            listView.setAdapter(noticeAdapter);

        }else {

            mArrayList.addAll(list);
            noticeAdapter.notifyDataSetChanged();
        }

        listView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            int lastExpandedPosition = -1;
            @Override
            public void onGroupExpand(int position) {
                if(lastExpandedPosition != -1 && position != lastExpandedPosition){
                    listView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = position;
            }
        });

        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                listView.clearFocus();
                listView.post(new Runnable() {
                    @Override
                    public void run() {
                        listView.setSelection(groupPosition);

                    }
                });

                return false;
            }
        });
    }

    /**
     * 자주묻는질문 리스트 파싱 완료 후
     */
    public void setFaqData(List<GetFaqDataResultModel> list){

        if ( PageNum == 1){
            mFaqArrayList.clear();
            mFaqArrayList.addAll(list);
            faqAdapter = new FaqAdapter(getApplicationContext(), mFaqArrayList);
            listView.setAdapter(faqAdapter);

        }else {

            mFaqArrayList.addAll(list);
            faqAdapter.notifyDataSetChanged();
        }

        listView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            int lastExpandedPosition = -1;
            @Override
            public void onGroupExpand(int position) {
                if(lastExpandedPosition != -1 && position != lastExpandedPosition){
                    listView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = position;
            }
        });

        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                listView.clearFocus();
                listView.post(new Runnable() {
                    @Override
                    public void run() {
                        listView.setSelection(groupPosition);

                    }
                });

                return false;
            }
        });
    }

    /**
     /* 공지사항 or 이벤트
     **/

    public void getNotice(int pageNum, String getType){

        String page = String.valueOf(pageNum);

        NetworkManager manager = new NetworkManager();
        manager.getNotice(page, getType, new Callback<GetNoticeResultModel>() {
            @Override
            public void onResponse(Call<GetNoticeResultModel> call, Response<GetNoticeResultModel> response) {
                getNoticeResultModel = response.body();

                if (response.isSuccessful()) {

                    if (getNoticeResultModel.getResultCode().equalsIgnoreCase("200")) {

                        Totalpage = Integer.parseInt(getNoticeResultModel.getTotalPage());
                        setNoticeData(getNoticeResultModel.getNoticelist());
                        loock = false;

                    }

                } else {
                    showToast(Errorcode.ERROCODE("1001"));
                }
            }

            @Override
            public void onFailure(Call<GetNoticeResultModel> call, Throwable t) {
                showToast(Errorcode.ERROCODE("1001"));
            }
        });

    }

    /**
     /* 자주 묻는 질문
     **/

    public void getFaq(int pageNum, String getos){

        String page = String.valueOf(pageNum);

        NetworkManager manager = new NetworkManager();
        manager.getFaq(page, getos, new Callback<GetFaqResultModel>() {
            @Override
            public void onResponse(Call<GetFaqResultModel> call, Response<GetFaqResultModel> response) {
                getFaqResultModel = response.body();

                if (response.isSuccessful()) {

                    if (getFaqResultModel.getResultCode().equalsIgnoreCase("200")) {

                        Totalpage = Integer.parseInt(getFaqResultModel.getTotalPage());
                        setFaqData(getFaqResultModel.getFaqlist());
                        loock = false;

                    }

                } else {
                    showToast(Errorcode.ERROCODE("1001"));
                }
            }

            @Override
            public void onFailure(Call<GetFaqResultModel> call, Throwable t) {
                showToast(Errorcode.ERROCODE("1001"));
            }
        });

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        int count = totalItemCount - visibleItemCount;
        if (firstVisibleItem >= count && totalItemCount != 0 && loock == false && PageNum < Totalpage) {
            loock = true;
            if("q".equalsIgnoreCase(getIntent().getStringExtra("type"))) {
                getFaq(PageNum, type);
            } else {
                getNotice(++PageNum, type);
            }

        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {}


}
