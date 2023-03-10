package com.example.song4u.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.song4u.Adapter.MoreListAdapter;
import com.example.song4u.Data.MoreListData;
import com.example.song4u.R;
import com.example.song4u.Util.CommonUtil;
import com.example.song4u.activity.AccessActivity;
import com.example.song4u.activity.NoticeActivity;
import com.example.song4u.activity.PolicyActivity;
import com.example.song4u.databinding.FragmentMoreBinding;

import java.util.ArrayList;

public class Fragment_More extends Fragment implements AdapterView.OnItemClickListener {

    private FragmentMoreBinding binding;

    ArrayList<MoreListData> moreListData;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //DashboardViewModel dashboardViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(DashboardViewModel.class);

        binding = FragmentMoreBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowCustomEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();

        return root;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.InitializeMoreListData();

        /* 앱 정보 섹션 : 기기별 dp를 int 값으로 변경 */
        DisplayMetrics dm = getResources().getDisplayMetrics();

        ListView mListView = binding.mListView;
        //View headerView = getLayoutInflater().inflate(R.layout.list_header_more, null);
        //TextView mtxt = headerView.findViewById(R.id.mText);
        //mtxt.setText("안내");

        final MoreListAdapter myAdapter = new MoreListAdapter(getActivity(),moreListData,dm);
        mListView.setAdapter(myAdapter);
        mListView.setOnItemClickListener(this);
        //mListView.addHeaderView(headerView);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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

    public void InitializeMoreListData()
    {
        moreListData = new ArrayList<MoreListData>();

        moreListData.add(new MoreListData("안내"));
        moreListData.add(new MoreListData("공지사항"));
        //moreListData.add(new MoreListData("이벤트"));
        moreListData.add(new MoreListData("자주 묻는 질문"));
        moreListData.add(new MoreListData("문의하기"));

        moreListData.add(new MoreListData("앱 정보"));
        moreListData.add(new MoreListData("서비스 이용약관"));
        moreListData.add(new MoreListData("개인정보 처리방침"));
        moreListData.add(new MoreListData("앱 접근 권한"));
        moreListData.add(new MoreListData("버전 정보"));

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {
            case R.id.mListView:

                //공지사항
                if(position == 1) {
                    Intent i = new Intent(getActivity(), NoticeActivity.class);
                    i.putExtra("type", "n");
                    startActivity(i);

                    //자주묻는 질문
                } else if(position == 2) {

                    Intent i = new Intent(getActivity(), NoticeActivity.class);
                    i.putExtra("type", "q");
                    startActivity(i);

                    //문의하기
                } else if(position == 3) {

                    String desc = "사용자 아이디: "+ CommonUtil.mSharePrefreences(getActivity(), getString(R.string.share_userId), null, "guest")+"\n·아래 문의 내용을 적어 전달주시면 관리자 확인 후 답변드리도록 하겠습니다.\n\n문의내용 : ";

                    Intent Email = new Intent(Intent.ACTION_SEND);
                    Email.setType("text/email");
                    Email.setPackage("com.google.android.gm");
                    Email.putExtra(Intent.EXTRA_EMAIL, new String[] { "song4u2022@gmail.com" });
                    Email.putExtra(Intent.EXTRA_SUBJECT, "문의하기");
                    Email.putExtra(Intent.EXTRA_TEXT, desc);
                    startActivity(Intent.createChooser(Email, "문의하기"));

                } else if(position == 4) {

                    //서비스 이용약관
                } else if(position == 5) {
                    Intent i = new Intent(getActivity(), PolicyActivity.class);
                    i.putExtra("type", "policy1");
                    startActivity(i);

                    //개인정보 처리방침
                } else if(position == 6) {
                    Intent i = new Intent(getActivity(), PolicyActivity.class);
                    i.putExtra("type", "policy2");
                    startActivity(i);

                    //앱 접근 권한
                } else if(position == 7) {
                    Intent i = new Intent(getActivity(), AccessActivity.class);
                    i.putExtra("type", "end");
                    startActivity(i);
                } else if(position == 8) {

                } else {

                }

                break;
        }

    }
}
