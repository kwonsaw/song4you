package com.example.song4u.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.song4u.Adapter.SupportListAdapter;
import com.example.song4u.R;
import com.example.song4u.Util.Errorcode;
import com.example.song4u.databinding.SupportmusicactivityBinding;
import com.example.song4u.network.NetworkManager;
import com.example.song4u.network.resultmodel.GetMusicListDataResultModel;
import com.example.song4u.network.resultmodel.GetSupportMusicResultModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SupportMusicActivity extends Season2BaseActivity {

    private SupportmusicactivityBinding binding;
    private GetSupportMusicResultModel getSupportMusicResultModel;
    private ArrayList<GetMusicListDataResultModel> mArrayList;
    private ListView listView;
    private SupportListAdapter supportListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SupportmusicactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        initActionBar();
        getSupportMusic();

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
        listView = binding.mListView;
        mArrayList = new ArrayList<>();
    }

    private void initActionBar() {

        TextCount.setVisibility(View.GONE);
        mTitle.setVisibility(View.VISIBLE);
        alarm.setVisibility(View.GONE);
        mtxt.setVisibility(View.GONE);
        mIcon.setVisibility(View.GONE);

        mTitle.setText("내가 후원한 음원");

        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(mActionBarBackgroundDrawable);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(ActionbarView);
        actionBar.setElevation(0);
    }


    public void setSupportData(List<GetMusicListDataResultModel> list){

        if (list.size() == 0) {
            listView.setVisibility(View.GONE);
            binding.mText.setVisibility(View.VISIBLE);
        } else {

            listView.setVisibility(View.VISIBLE);
            binding.mText.setVisibility(View.GONE);

            mArrayList.clear();
            mArrayList.addAll(list);
            supportListAdapter = new SupportListAdapter(getApplicationContext(), mArrayList);
            listView.setAdapter(supportListAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(SupportMusicActivity.this, MusicDetailActivity.class);
                    intent.putExtra("musicid", mArrayList.get(i).getMusicId());
                    intent.putExtra("songid", mArrayList.get(i).getSongid());
                    startActivity(intent);
                }
            });
        }

    }

    /**
     * 내가 후원한 음원
     */
    public void getSupportMusic(){

        NetworkManager manager = new NetworkManager();
        manager.getSupportMusic(getVersion(), getUserId(), new Callback<GetSupportMusicResultModel>() {
            @Override
            public void onResponse(Call<GetSupportMusicResultModel> call, Response<GetSupportMusicResultModel> response) {
                getSupportMusicResultModel = response.body();

                if (response.isSuccessful()) {

                    if (getSupportMusicResultModel.getResultCode().equalsIgnoreCase("200")) {
                        setSupportData(getSupportMusicResultModel.getMusiclist());
                    } else {
                        showToast(Errorcode.ERROCODE(getSupportMusicResultModel.getResultCode()));
                    }

                } else {
                    showToast(Errorcode.ERROCODE("1001"));
                }
            }

            @Override
            public void onFailure(Call<GetSupportMusicResultModel> call, Throwable t) {
                showToast(Errorcode.ERROCODE("1001"));
            }
        });

    }

}
