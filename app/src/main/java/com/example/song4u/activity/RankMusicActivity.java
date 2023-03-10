package com.example.song4u.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.song4u.Adapter.MusicRankRecyclerAdapter;
import com.example.song4u.Adapter.RankListAdapter;
import com.example.song4u.Application.AppAplication;
import com.example.song4u.R;
import com.example.song4u.Util.Errorcode;
import com.example.song4u.Util.SFUToast;
import com.example.song4u.databinding.RankmusicactivityBinding;
import com.example.song4u.network.NetworkManager;
import com.example.song4u.network.resultmodel.GetMusicListDataResultModel;
import com.example.song4u.network.resultmodel.GetMusicRankResultModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RankMusicActivity extends Season2BaseActivity implements AbsListView.OnScrollListener {

    private RankmusicactivityBinding binding;

    private GetMusicRankResultModel getMusicRankResultModel;
    private ArrayList<GetMusicListDataResultModel> musicrankList;

    //재생횟수 랭킹
    private ListView listView;
    private RankListAdapter rankListAdapter;
    private RecyclerView rRecyclerView;
    private MusicRankRecyclerAdapter musicRankRecyclerAdapter;

    private int PageNum = 1;
    private int Totalpage = 1;
    private boolean loock = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = RankmusicactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        initActionBar();
        initView();

        getMusicRank(getVersion(), PageNum);

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

        rRecyclerView = binding.rRecyclerView;
        listView = binding.mListView;
        listView.setOnScrollListener(RankMusicActivity.this);


    }

    private void initActionBar() {

        mBtn.setVisibility(View.VISIBLE);
        TextCount.setVisibility(View.GONE);
        mTitle.setVisibility(View.VISIBLE);
        alarm.setVisibility(View.GONE);
        alarm.setImageResource(R.drawable.ic_baseline_mode_24);
        mIcon.setVisibility(View.GONE);
        mTitle.setText("재생횟수 누적 랭킹");

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

    public void setMusicRankListData(List<GetMusicListDataResultModel> list) {

        if ( PageNum == 1){

            musicrankList = new ArrayList<>();
            musicrankList.clear();
            musicrankList.addAll(list);

            GridLayoutManager gridLayoutManager = new GridLayoutManager(RankMusicActivity.this, 1, GridLayoutManager.VERTICAL, false);
            musicRankRecyclerAdapter = new MusicRankRecyclerAdapter(RankMusicActivity.this,musicrankList);
            rRecyclerView.setAdapter(musicRankRecyclerAdapter);
            rRecyclerView.setLayoutManager(gridLayoutManager);

        }else {

            musicrankList.addAll(list);
            musicRankRecyclerAdapter.notifyDataSetChanged();
        }

        musicRankRecyclerAdapter.setOnItemClickListener(new MusicRankRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

                Intent i = new Intent(RankMusicActivity.this, MusicDetailActivity.class);
                i.putExtra("musicid", musicrankList.get(position).getMusicId());
                i.putExtra("songid", musicrankList.get(position).getSongid());
                startActivity(i);

            }
        }) ;

        rRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // 리사이클러뷰 가장 마지막 index
                int lastPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                // 받아온 리사이클러 뷰 카운트
                int totalCount = recyclerView.getAdapter().getItemCount();
                // 스크롤을 맨 끝까지 한 것!
                if(lastPosition == totalCount -1 && loock == false && PageNum < Totalpage){
                    loock = true;
                    getMusicRank(getVersion(), ++PageNum);
                }
            }
        });


    }

    /**
     /* 재생횟수 랭킹
     **/

    public void getMusicRank(String appVersion, int pageNum){

        String page = String.valueOf(pageNum);

        NetworkManager manager = new NetworkManager();
        manager.getMusicRank(appVersion, page, new Callback<GetMusicRankResultModel>() {
            @Override
            public void onResponse(Call<GetMusicRankResultModel> call, Response<GetMusicRankResultModel> response) {
                getMusicRankResultModel = response.body();

                if(response.isSuccessful()) {

                    if (getMusicRankResultModel.getResultCode().equalsIgnoreCase("200")) {

                        Totalpage = Integer.parseInt(getMusicRankResultModel.getTotalPage());
                        setMusicRankListData(getMusicRankResultModel.getRanklist());
                        loock = false;

                    } else {
                        SFUToast.showToast(AppAplication.context, Errorcode.ERROCODE(getMusicRankResultModel.getResultCode()));
                    }

                } else {
                    SFUToast.showToast(AppAplication.context, Errorcode.ERROCODE("1001"));
                }

            }

            @Override
            public void onFailure(Call<GetMusicRankResultModel> call, Throwable t) {
                SFUToast.showToast(AppAplication.context, Errorcode.ERROCODE("1001"));
            }
        });

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        int count = totalItemCount - visibleItemCount;
        if (firstVisibleItem >= count && totalItemCount != 0 && loock == false && PageNum < Totalpage) {
            loock = true;
            getMusicRank(getVersion(), ++PageNum);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {}


}
