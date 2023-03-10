package com.example.song4u.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.example.song4u.R;
import com.example.song4u.Util.Errorcode;
import com.example.song4u.Util.SFUAlertDialog;
import com.example.song4u.databinding.StoreactivityBinding;
import com.example.song4u.network.NetworkManager;
import com.example.song4u.network.resultmodel.GetGifticonDataResultModel;
import com.example.song4u.network.resultmodel.SetGifticonResultModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.nekolaboratory.EmulatorDetector;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreActivity extends Season2BaseActivity implements View.OnClickListener{

    private StoreactivityBinding binding;
    private ArrayList<GetGifticonDataResultModel> storeList;

    private SetGifticonResultModel setGifticonResultModel;
    private String priceStr, productidStr;

    //오픈소스 에뮬레이터 체크
    private Boolean isEmulator = false;

    //기울기를 이용한 에뮬레이터 체크
    private Boolean isSensor = false;
    private SensorManager sm;
    private SensorEventListener oriL;
    private Sensor oriSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = StoreactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        setIsEmulator();
        initActionBar();

    }

    @Override
    protected void onResume() {
        super.onResume();

        sm.registerListener(oriL, oriSensor, SensorManager.SENSOR_DELAY_NORMAL);
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
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {

        ArrayList<GetGifticonDataResultModel> storeList = (ArrayList<GetGifticonDataResultModel>) getIntent().getSerializableExtra("storeinfo");
        int i = getIntent().getIntExtra("position",0);

        Glide.with(StoreActivity.this)
                .load(storeList.get(i).getImageurl())
                .error(R.drawable.ic_baseline_account_circle_24)
                .into(binding.mImage);

        priceStr = storeList.get(i).getPrice();
        productidStr = storeList.get(i).getGifticonproductid();

        binding.pText.setText(storeList.get(i).getBrandname());
        binding.bText.setText(storeList.get(i).getProductname());
        binding.cText.setText(getNumberFormat(Integer.parseInt(storeList.get(i).getPrice()))+"P");
        binding.eText.setText(getNumberFormat(Integer.parseInt(storeList.get(i).getExdate()))+"일");
        binding.dText.setText(Html.fromHtml(storeList.get(i).getGifticonproductinfourl()));

        binding.buybtn.setOnClickListener(this);

    }

    private void initActionBar() {

        TextCount.setVisibility(View.GONE);
        mTitle.setVisibility(View.VISIBLE);
        alarm.setVisibility(View.GONE);
        mtxt.setVisibility(View.VISIBLE);
        mIcon.setVisibility(View.GONE);

        mTitle.setText("스토어");
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

    public void setIsEmulator() {
        isEmulator = EmulatorDetector.isEmulator(StoreActivity.this);

        sm = (SensorManager)getSystemService(SENSOR_SERVICE);    // SensorManager 인스턴스를 가져옴
        oriSensor = sm.getDefaultSensor(Sensor.TYPE_ORIENTATION);    // 방향 센서
        oriL = new oriListener();
    }

    private class oriListener implements SensorEventListener {
        public void onSensorChanged(SensorEvent event) {  // 방향 센서 값이 바뀔때마다 호출됨
            double accX = event.values[0];
            double accY = event.values[1];
            double accZ = event.values[2];

            if (accX == 0.0 || accY == 0.0 || accZ == 0.0) {
                isSensor = true;
            } else {
                isSensor = false;
            }

            sm.unregisterListener(oriL);
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.buybtn:

                String phone = getPhoneNumber(StoreActivity.this) == null ? "" : getPhoneNumber(StoreActivity.this);

                if (phone.length() == 0) {
                    SFUAlertDialog.singleButtonShowAlert(StoreActivity.this, "알림", "죄송합니다. 상품 구매 이용에 적합하지 않은 휴대기기입니다.", null);
                    return;
                } else if (isSensor) {
                    SFUAlertDialog.singleButtonShowAlert(StoreActivity.this, "알림", "죄송합니다. 상품 구매 이용에 적합하지 않은 휴대기기입니다.", null);
                    return;
                }else if (isEmulator) {
                    SFUAlertDialog.singleButtonShowAlert(StoreActivity.this, "알림", "죄송합니다. 상품 구매 이용에 적합하지 않은 휴대기기입니다.", null);
                    return;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("알림").setMessage("구매 완료 시, 환불이 불가한 상품이오니 확인 후 구매를 진행해주세요. 구매하시겠습니까?.");

                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //Toast.makeText(getApplicationContext(), "OK Click", Toast.LENGTH_SHORT).show();
                        setGiftcon(productidStr, getPhoneNumber(StoreActivity.this));
                    }
                });

                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //Toast.makeText(getApplicationContext(), "Cancel Click", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                break;

            case R.id.maincion_btn_txt:

                View view = getLayoutInflater().inflate(R.layout.storehelpview, null);
                BottomSheetDialog dialog = new BottomSheetDialog(StoreActivity.this, R.style.AppBottomSheetDialogTheme); // Style here
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


    /**
     /* 기프티콘 구매
     **/

    public void setGiftcon(String productId , String phoneNumber){

        String androidid = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor()).build();

        NetworkManager manager = new NetworkManager();
        manager.setGifticon(getVersion(), getUserId(), androidid,phoneNumber,productId,priceStr, client, new Callback<SetGifticonResultModel>() {
            @Override
            public void onResponse(Call<SetGifticonResultModel> call, Response<SetGifticonResultModel> response) {
                setGifticonResultModel = response.body();

                if(response.isSuccessful()) {

                    if (setGifticonResultModel.getResultCode().equalsIgnoreCase("200")) {
                        SFUAlertDialog.singleButtonShowAlert(StoreActivity.this, "알림", "구매가 완료되었습니다. 쿠폰함을 확인해주세요.", null);
                    } else {
                        showToast(Errorcode.ERROCODE(setGifticonResultModel.getResultCode()));
                    }

                } else {
                    showToast(Errorcode.ERROCODE("1001"));
                }

            }

            @Override
            public void onFailure(Call<SetGifticonResultModel> call, Throwable t) {
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