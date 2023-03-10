package com.example.song4u.Fragment;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.song4u.Application.AppAplication;
import com.example.song4u.R;
import com.example.song4u.Util.CommonUtil;
import com.example.song4u.Util.Errorcode;
import com.example.song4u.Util.SFUAlertDialog;
import com.example.song4u.Util.SFUToast;
import com.example.song4u.activity.HistoryActivity;
import com.example.song4u.activity.LoginActivity;
import com.example.song4u.activity.ProfileActivity;
import com.example.song4u.activity.RecommendActivity;
import com.example.song4u.activity.Season2BaseActivity;
import com.example.song4u.activity.ShareActivity;
import com.example.song4u.activity.SupportMusicActivity;
import com.example.song4u.databinding.FragmentMypageBinding;
import com.example.song4u.network.NetworkManager;
import com.example.song4u.network.resultmodel.GetUserInfoDataResultModel;
import com.example.song4u.network.resultmodel.GetUserInfoResultModel;
import com.example.song4u.network.resultmodel.SetDeleteUserResultModel;
import com.example.song4u.network.resultmodel.SetUserProfileResultModel;
import com.google.android.exoplayer2.util.Log;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Mypage extends Fragment {

    private FragmentMypageBinding binding;

    private SetDeleteUserResultModel setDeleteUserResultModel;
    private SetUserProfileResultModel setUserProfileResultModel;
    private GetUserInfoResultModel getUserInfoResultModel;
    private GetUserInfoDataResultModel getUserInfoDataResultModel;

    private ArrayList<GetUserInfoDataResultModel> mArrayList;

    private String imgUrl, nicknameStr;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //DashboardViewModel dashboardViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(DashboardViewModel.class);

        binding = FragmentMypageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowCustomEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        setHasOptionsMenu(true); //상단 액션바 버튼 사용 가능
        return root;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /* 내가 후원한 음원 */
        binding.supportRay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), SupportMusicActivity.class);
                startActivity(i);
            }
        });

        /* 회원탈퇴 */
        binding.deleteRelay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SFUAlertDialog.doubleButtonShowAlert(getActivity(), "알림", "회원탈퇴시 보유중인 포인트가 자동 소멸처리됩니다. 회원탈퇴 하시겠습니까?", "취소",null, "회원탈퇴", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setDeleteUser();
                    }
                });
            }
        });

        /* 닉네임 변경 */
        binding.nicknameRelay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), ProfileActivity.class);
                i.putExtra("imgUrl", imgUrl);
                i.putExtra("nicknameStr", nicknameStr);
                startActivityResult.launch(i);
            }
        });

        /* 프로필 사진 변경 */
        binding.profileRelay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfilePopup();

            }
        });

        binding.mImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfilePopup();
            }
        });

        /* 닉네임 변경 */
        binding.modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), ProfileActivity.class);
                i.putExtra("imgUrl", imgUrl);
                i.putExtra("nicknameStr", nicknameStr);
                startActivityResult.launch(i);
            }
        });

        /* 포인트 적립 내역 */
        binding.pLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), HistoryActivity.class);
                startActivity(i);
            }
        });

        /* 음원등록 신청 */
        binding.emailRelay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int numInt = getUserInfoResultModel.getPoint();
                int maxCnt = (numInt / 50);

                //String desc = "신청인 아이디: "+CommonUtil.mSharePrefreences(getActivity(), getString(R.string.share_userId), null, "guest")+"\n보유포인트: "+numInt+"P로 최대"+maxCnt+"회 후원 가능합니다.\n\n"+"· 아래 노래제목과 가수명, 재생횟수 후원(사용포인트 또는 재생횟수), 재생 플레이어(멜론 또는 지니)를 정확히 입력 후 메일을 보내주세요. 음원등록이 완료되면 전달주신 mail주소로 등록완료 메일을 보내드립니다.\n재생횟수 1회당 보유포인트에서 50P가 차감됩니다.\n\n노래제목:\n가수:\n재생횟수 후원:\n재생 플레이어:";
                //String desc = "<p style=\"text-align: center;\"><img src=\"http://139.150.71.159/project/noticeimg/email.png\" class=\"txc-image\" style=\"clear:none;float:none;\" /></p><p><br></p>신청인 아이디: "+CommonUtil.mSharePrefreences(getActivity(), getString(R.string.share_userId), null, "guest")+"\n보유포인트: "+numInt+"P로 최대"+maxCnt+"회 후원 가능합니다.\n\n";
                String desc = "<p>아래 노래제목과 가수, 재생횟수 (1회당 50포인트 차감), 재생앱 (멜론 또는 지니)을 입력해서 보내주세요. 관리자가 대신 음원을 등록해 드립니다.</p>\n" +
                        "<p><br></p>\n" + "신청인 아이디 : " + CommonUtil.mSharePrefreences(getActivity(), getString(R.string.share_userId), null, "guest")+"<br>" + "보유 포인트 : " +numInt+"P로 최대"+maxCnt+"회 후원 가능합니다.\n\n" + "<p><br></p>" +
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
            }
        });

        /* 추천인 등록 */
        binding.recommendRelay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), RecommendActivity.class);
                startActivityResult.launch(i);
            }
        });

        /* 친구추천 */
        binding.shareRelay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ShareActivity.class);
                startActivity(i);
            }
        });

        getUserInfo(CommonUtil.mSharePrefreences(getActivity(), getString(R.string.share_userId), null, "guest"));

    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.actionbar_mypage, menu) ;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // 상단 로그아웃 버튼
        switch (item.getItemId()) {

            case R.id.action_logout:

                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                //builder.setTitle("");
                builder.setMessage("로그아웃 하시겠습니까?");
                builder.setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                CommonUtil.mSharePrefreences(getActivity(), getString(R.string.share_userId), "guest", null);
                                Intent i = new Intent(getActivity() , LoginActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK );
                                startActivity(i);
                                dialog.dismiss();
                            }
                        });
                builder.setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        });
                builder.show();

            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 사용자 정보 호출 완료 후
     */
    public void setUserData(List<GetUserInfoDataResultModel> list){

        if (list.size() > 0) {

            imgUrl = list.get(0).getpImageUrl();
            nicknameStr = list.get(0).getNickname();

            mArrayList = new ArrayList<>();
            mArrayList.addAll(list);
            //Glide.with(context).load(url).apply(new RequestOption().circleCrop().centerCrop()).into(imageview);
            Glide.with(getActivity())
                    .load(list.get(0).getpImageUrl())
                    .circleCrop()
                    .error(R.drawable.ic_baseline_account_circle_24)
                    .into(binding.mImg);

            //Glide.with(this).load(R.drawable.sample).circleCrop().into(binding.mImg);

            binding.mText.setText(list.get(0).getNickname());

            int p = getUserInfoResultModel.getPoint();
            NumberFormat nf = NumberFormat.getNumberInstance();
            String str = "   "+nf.format(p)+"P";

            SpannableStringBuilder sb = new SpannableStringBuilder(str);
            Drawable image = getResources().getDrawable(R.drawable.ic_baseline_monetization_on_24);
            image.setBounds(0, 0, 50, 50);
            sb.setSpan(new ImageSpan(image), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            //binding.cText.setText(sb);

            binding.pText.setText(sb);

        } else {
            ((Season2BaseActivity) getActivity()).showToast("사용자 정보를 알 수 없습니다.");
        }

    }


    /**
     * 프로필 사진 업로드
     * 사진 , 카메라 선택
     */

    public void ProfilePopup() {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                goAlbum();
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            }

        } else {
            goAlbum();
        }

    }

    public void goAlbum() {

        CropImage.ActivityBuilder activityBuilder = CropImage.activity().setActivityTitle("프로필 사진")
                .setCropMenuCropButtonTitle("선택")
                .setGuidelines(CropImageView.Guidelines.ON)
                .setCropShape(CropImageView.CropShape.OVAL)
                .setAspectRatio(1, 1).setFixAspectRatio(true);
        activityBuilder.start(getContext(), this);

        /*
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setMessage("프로필 사진 변경");
        dialog.setPositiveButton("사진첩", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                launcher.launch(intent);

            }
        });

        dialog.setNegativeButton("닫기", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {

            }
        });

        dialog.show();
        */

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                setUserProfile(resultUri, CommonUtil.mSharePrefreences(getActivity(), getString(R.string.share_userId), null, "guest"));
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>()
            {
                @Override
                public void onActivityResult(ActivityResult result)
                {
                    if (result.getResultCode() == RESULT_OK)
                    {

                        Intent intent = result.getData();
                        Uri uri = intent.getData();

                        setUserProfile(uri, CommonUtil.mSharePrefreences(getActivity(), getString(R.string.share_userId), null, "guest"));

                    }
                }
            });



    /**
     /* 프로필 이미지 업로드
     **/

    public void setUserProfile(Uri path, String userid){

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor()).build();
        Log.e("kwonsaw","setUserProfile");
        NetworkManager manager = new NetworkManager();
        manager.setUserProfile(path, userid, client, new Callback<SetUserProfileResultModel>() {
            @Override
            public void onResponse(Call<SetUserProfileResultModel> call, Response<SetUserProfileResultModel> response) {
                setUserProfileResultModel = response.body();

                if(response.isSuccessful()) {

                    if (setUserProfileResultModel.getResultCode().equalsIgnoreCase("200")) {

                        ((Season2BaseActivity) getActivity()).showToast("프로필 사진이 변경되었습니다.");
                        Glide.with(getActivity())
                                .load(path)
                                .into(binding.mImg);

                    } else {
                        ((Season2BaseActivity) getActivity()).showToast(Errorcode.ERROCODE(setUserProfileResultModel.getResultCode()));
                    }

                } else {
                    ((Season2BaseActivity) getActivity()).showToast(Errorcode.ERROCODE(setUserProfileResultModel.getResultCode()));
                }

            }

            @Override
            public void onFailure(Call<SetUserProfileResultModel> call, Throwable t) {
                ((Season2BaseActivity) getActivity()).showToast("이미지 업로드에 실패했습니다. 다른 사진을 선택해주세요.");
                Log.e("kwonsaw",""+t.getMessage());
            }
        });

    }

    /**
     /* 사용자 정보
     **/

    public void getUserInfo(String userid){

        //OkHttpClient client = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor()).build();

        NetworkManager manager = new NetworkManager();
        manager.getUserInfo(userid, new Callback<GetUserInfoResultModel>() {
            @Override
            public void onResponse(Call<GetUserInfoResultModel> call, Response<GetUserInfoResultModel> response) {
                getUserInfoResultModel = response.body();

                if(response.isSuccessful()) {

                    if (getUserInfoResultModel.getResultCode().equalsIgnoreCase("200")) {

                        setUserData(getUserInfoResultModel.getUserinfo());

                    } else if (getUserInfoResultModel.getResultCode().equalsIgnoreCase("826")) {
                        SFUToast.showToast(AppAplication.context, Errorcode.ERROCODE(getUserInfoResultModel.getResultCode()));
                        CommonUtil.mSharePrefreences(getActivity(), getString(R.string.share_userId), "guest", null);
                        Intent i = new Intent(getActivity() , LoginActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK );
                        startActivity(i);
                    } else {
                        ((Season2BaseActivity) getActivity()).showToastLong(Errorcode.ERROCODE(getUserInfoResultModel.getResultCode()));
                    }

                } else {
                    ((Season2BaseActivity) getActivity()).showToast(Errorcode.ERROCODE("1001"));
                }

                //hideLoding();

            }

            @Override
            public void onFailure(Call<GetUserInfoResultModel> call, Throwable t) {
                ((Season2BaseActivity) getActivity()).showToast(Errorcode.ERROCODE("1001"));
            }
        });

    }

    /**
     /* 회원 탈퇴
     **/

    public void setDeleteUser(){

        NetworkManager manager = new NetworkManager();
        manager.setDeleteUser(CommonUtil.mSharePrefreences(getActivity(), getString(R.string.share_userId), null, "guest"), new Callback<SetDeleteUserResultModel>() {
            @Override
            public void onResponse(Call<SetDeleteUserResultModel> call, Response<SetDeleteUserResultModel> response) {
                setDeleteUserResultModel = response.body();

                if(response.isSuccessful()) {

                    if (setDeleteUserResultModel.getResultCode().equalsIgnoreCase("200")) {
                        SFUToast.showToastLong(AppAplication.context,"회원탈퇴 되었습니다.");
                        CommonUtil.mSharePrefreences(getActivity(), getString(R.string.share_userId), "guest", null);
                        Intent i = new Intent(getActivity() , LoginActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK );
                        startActivity(i);
                    } else {
                        SFUToast.showToast(AppAplication.context, Errorcode.ERROCODE(setDeleteUserResultModel.getResultCode()));
                    }

                } else {
                    SFUToast.showToast(AppAplication.context, Errorcode.ERROCODE("1001"));
                }

            }

            @Override
            public void onFailure(Call<SetDeleteUserResultModel> call, Throwable t) {
                SFUToast.showToast(AppAplication.context, Errorcode.ERROCODE("1001"));
            }
        });

    }


    /* 프로필 수정하고 돌아왔을 경우 */
    ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        getUserInfo(CommonUtil.mSharePrefreences(getActivity(), getString(R.string.share_userId), null, "guest"));
                    }
                }
            });

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
