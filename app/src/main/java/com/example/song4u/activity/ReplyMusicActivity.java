package com.example.song4u.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.recyclerview.widget.RecyclerView;

import com.example.song4u.Adapter.ReplyListAdapter;
import com.example.song4u.Adapter.ReplyRecyclerAdapter;
import com.example.song4u.Application.AppAplication;
import com.example.song4u.R;
import com.example.song4u.Util.Errorcode;
import com.example.song4u.Util.SFUAlertDialog;
import com.example.song4u.Util.SFUToast;
import com.example.song4u.databinding.ReplymusicactivityBinding;
import com.example.song4u.network.NetworkManager;
import com.example.song4u.network.resultmodel.GetReplyDataResultModel;
import com.example.song4u.network.resultmodel.GetReplyResultModel;
import com.example.song4u.network.resultmodel.SetReplyMusicResultModel;
import com.example.song4u.network.resultmodel.SetReplyReportResultModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ReplyMusicActivity extends Season2BaseActivity implements View.OnClickListener, AbsListView.OnScrollListener{

    private ReplymusicactivityBinding binding;

    private GetReplyResultModel getReplyResultModel;
    private SetReplyReportResultModel setReplyReportResultModel;
    private SetReplyMusicResultModel setReplyMusicResultModel;

    private ListView listView;
    private ReplyListAdapter replyListAdapter;

    private RecyclerView mRecyclerView;
    private ReplyRecyclerAdapter replyRecyclerAdapter;
    private ArrayList<GetReplyDataResultModel> replyList;

    private String getMusicid;

    private int PageNum = 1;
    private int Totalpage = 1;
    private boolean loock = false;

    private boolean isAdd = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ReplymusicactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getMusicid = getIntent().getStringExtra("musicid");

        initActionBar();
        initView();

        //getReply(getVersion(), PageNum);

    }

    @Override
    protected void onResume() {
        super.onResume();
        PageNum = 1;
        getReply(getVersion(), PageNum);
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
        mRecyclerView = binding.mRecyclerView;
        listView = binding.mListView;
        listView.setOnScrollListener(ReplyMusicActivity.this);

        replyList = new ArrayList<>();

    }

    private void initActionBar() {

        mBtn.setVisibility(View.VISIBLE);
        TextCount.setVisibility(View.GONE);
        mTitle.setVisibility(View.VISIBLE);
        alarm.setVisibility(View.VISIBLE);
        alarm.setImageResource(R.drawable.ic_baseline_mode_24);
        mIcon.setVisibility(View.GONE);
        mTitle.setText("음원 댓글");

        alarm.setOnClickListener(this);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.mainicon_btn:

                Intent i = new Intent(ReplyMusicActivity.this, ReplyWriteActivity.class);
                i.putExtra("musicid",getMusicid);
                i.putExtra("type","add");
                startActivityResult.launch(i);

                break;

        }

    }

    public void setReplyData(List<GetReplyDataResultModel> list) {

        if ( PageNum == 1){

            replyList.clear();
            replyList.addAll(list);
            replyListAdapter = new ReplyListAdapter(getApplicationContext(), replyList);
            listView.setAdapter(replyListAdapter);

        }else {

            replyList.addAll(list);
            replyListAdapter.notifyDataSetChanged();
        }

        replyListAdapter.setOnItemClickListener(new ReplyListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View a_view, int a_position) {

                if(replyList.get(a_position).getMyreply().equalsIgnoreCase("y")) {

                    View view1 = getLayoutInflater().inflate(R.layout.reply_report_view, null);
                    BottomSheetDialog dialog1 = new BottomSheetDialog(ReplyMusicActivity.this, R.style.AppBottomSheetDialogTheme); // Style here
                    dialog1.setContentView(view1);

                    TextView uTextview = (TextView) view1.findViewById(R.id.uText);
                    TextView dTextview = (TextView) view1.findViewById(R.id.dText);

                    uTextview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(ReplyMusicActivity.this, ReplyWriteActivity.class);
                            i.putExtra("musicid",getMusicid);
                            i.putExtra("desc",replyList.get(a_position).getDescription());
                            i.putExtra("replyid",replyList.get(a_position).getReplyid());
                            i.putExtra("type","update");
                            startActivityResult.launch(i);
                            dialog1.dismiss();
                        }
                    });

                    dTextview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SFUAlertDialog.doubleButtonShowAlert(ReplyMusicActivity.this, null, "해당 댓글을 삭제하시겠습니까?", "취소",null, "삭제", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    setReplyMusic(getVersion(), replyList.get(a_position).getReplyid());
                                    dialog1.dismiss();

                                }

                            });
                        }
                    });

                    Button closeBtn1 = (Button) view1.findViewById(R.id.close);
                    closeBtn1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog1.dismiss();
                        }
                    });

                    dialog1.show();



                } else {

                    SFUAlertDialog.doubleButtonShowAlert(ReplyMusicActivity.this, null, "해당 댓글을 신고하시겠습니까?", "취소",null, "신고", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            setReplyReport(getVersion(), replyList.get(a_position).getReplyid());
                        }

                    });


                }

            }
        });

    }

    /**
     /* 댓글 리스트
     **/

    public void getReply(String appVersion, int pagenum){

        String page = String.valueOf(pagenum);

        NetworkManager manager = new NetworkManager();
        manager.getReply(appVersion, getUserId(), getMusicid, page,  new Callback<GetReplyResultModel>() {
            @Override
            public void onResponse(Call<GetReplyResultModel> call, Response<GetReplyResultModel> response) {
                getReplyResultModel = response.body();

                if(response.isSuccessful()) {

                    if (getReplyResultModel.getResultCode().equalsIgnoreCase("200")) {
                        if (getReplyResultModel.getReplylist().size() == 0){
                            binding.dText.setVisibility(View.VISIBLE);
                        } else {
                            Totalpage = getReplyResultModel.getTotalpage();
                            setReplyData(getReplyResultModel.getReplylist());
                            loock = false;
                            binding.dText.setVisibility(View.GONE);
                        }

                    } else {
                        SFUToast.showToast(AppAplication.context, Errorcode.ERROCODE(getReplyResultModel.getResultCode()));
                    }

                } else {
                    SFUToast.showToast(AppAplication.context, Errorcode.ERROCODE("1001"));
                }

            }

            @Override
            public void onFailure(Call<GetReplyResultModel> call, Throwable t) {
                SFUToast.showToast(AppAplication.context, Errorcode.ERROCODE("1001"));
            }
        });

    }

    /**
     /* 댓글 신고
     **/

    public void setReplyReport(String appVersion, String replyid){

        NetworkManager manager = new NetworkManager();
        manager.setReplyReport(appVersion, getUserId(), replyid, new Callback<SetReplyReportResultModel>() {
            @Override
            public void onResponse(Call<SetReplyReportResultModel> call, Response<SetReplyReportResultModel> response) {
                setReplyReportResultModel = response.body();

                if(response.isSuccessful()) {

                    if (setReplyReportResultModel.getResultCode().equalsIgnoreCase("200")) {
                        SFUToast.showToastLong(AppAplication.context,"신고처리 되었습니다. 담당자 확인 후 조치하도록 하겠습니다.");

                    } else {
                        SFUToast.showToast(AppAplication.context, Errorcode.ERROCODE(setReplyReportResultModel.getResultCode()));
                    }

                } else {
                    SFUToast.showToast(AppAplication.context, Errorcode.ERROCODE("1001"));
                }

            }

            @Override
            public void onFailure(Call<SetReplyReportResultModel> call, Throwable t) {
                SFUToast.showToast(AppAplication.context, Errorcode.ERROCODE("1001"));
            }
        });

    }

    /**
     /* 댓글 삭제
     **/

    public void setReplyMusic(String appVersion, String replyid) {

        NetworkManager manager = new NetworkManager();
        manager.setReplyMusic(appVersion, getUserId(), getMusicid, replyid, "null", "delete", new Callback<SetReplyMusicResultModel>() {
            @Override
            public void onResponse(Call<SetReplyMusicResultModel> call, Response<SetReplyMusicResultModel> response) {
                setReplyMusicResultModel = response.body();

                if (response.isSuccessful()) {

                    if (setReplyMusicResultModel.getResultCode().equalsIgnoreCase("200")) {
                        SFUToast.showToast(AppAplication.context, "댓글이 등록되었습니다.");

                    } else if (setReplyMusicResultModel.getResultCode().equalsIgnoreCase("705")) {
                        SFUToast.showToast(AppAplication.context, "댓글이 수정되었습니다.");

                    } else if (setReplyMusicResultModel.getResultCode().equalsIgnoreCase("706")) {
                        SFUToast.showToast(AppAplication.context, "댓글이 삭제되었습니다.");
                        replyList.clear();
                        replyListAdapter.notifyDataSetChanged();
                        PageNum = 1;
                        getReply(getVersion(), PageNum);
                    } else {
                        SFUToast.showToast(AppAplication.context, Errorcode.ERROCODE(setReplyMusicResultModel.getResultCode()));
                    }

                } else {
                    SFUToast.showToast(AppAplication.context, Errorcode.ERROCODE("1001"));
                }

            }

            @Override
            public void onFailure(Call<SetReplyMusicResultModel> call, Throwable t) {
                SFUToast.showToast(AppAplication.context, Errorcode.ERROCODE("1001"));
            }
        });
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        int count = totalItemCount - visibleItemCount;
        if (firstVisibleItem >= count && totalItemCount != 0 && loock == false && PageNum < Totalpage) {
            loock = true;
            getReply(getVersion(), ++PageNum);
            Log.e("kwonsaw","PageNum: "+PageNum);
        }

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {}

    /* 댓글 등록하고 돌아왔을 경우 */
    ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        //isAdd = true;
                        //PageNum = 1;
                        //getReply(getVersion(), PageNum);
                        //Log.e("kwonsaw","onActivityResult");
                    }
                }
            });

}
