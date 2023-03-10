package com.example.song4u.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.song4u.R;
import com.example.song4u.Util.AES256Cipher;
import com.example.song4u.Util.CommonUtil;

import java.io.File;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by Administrator on 2015-09-03.
 */
public class Season2BaseActivity extends AppCompatActivity {

    public ActionBar actionBar;
    public Drawable mActionBarBackgroundDrawable;

    //private HashMap<TrackerName, Tracker> mTrackers;
    private String PROPERTY_ID = "UA-61311375-1";
    //private Tracker t;
    public Dialog mDialog;
    public ProgressBar progressBar;
    public ProgressDialog pDialogg;

    private AlertDialog.Builder mAlertDialog;

    private String getUrl;
    private String strSDFormatDay = "0";
    //private FirebaseAnalytics mFirebaseAnalytics;

    public Toast toast;

    //액션바 관련
    public View ActionbarView;
    public ImageView mIcon;
    public TextView mTitle;
    public RelativeLayout mBtn;
    public TextView mtxt;
    public ImageView alarm;
    public TextView TextCount;
    public TextView WriteBtn;

    private static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActionBarBackgroundDrawable = new ColorDrawable(getResources().getColor(R.color.songcolor));

        mContext = getApplication().getApplicationContext();

        ActionbarView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.actionbar , null);
        mTitle = (TextView)ActionbarView.findViewById(R.id.mainicon_title);
        mTitle.setTextColor(getResources().getColor(R.color.white));

        mIcon = (ImageView)ActionbarView.findViewById(R.id.mainicon_icon);
        mtxt = (TextView)ActionbarView.findViewById(R.id.maincion_btn_txt);
        alarm = (ImageView)ActionbarView.findViewById(R.id.mainicon_btn);
        TextCount = (TextView)ActionbarView.findViewById(R.id.web5s_count);
        mBtn = (RelativeLayout)ActionbarView.findViewById(R.id.mainicon_btn_lay);
        WriteBtn = (TextView)ActionbarView.findViewById(R.id.season2_navi_wrritebtn);

        overridePendingTransition(R.anim.none, R.anim.none);
        //setProgress();
    }

    public String getGlobalString()
    {
        return getUrl;
    }

    public void setGlobalString(String globalString)
    {
        this.getUrl = globalString;
    }

    public void showDialog(){
        mDialog = new Dialog(mContext);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.progress);
        mDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);//transparent로 맞추면 다이얼로그 주변 배경 사라짐
        //dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);  // 배경 검은색 없애기
        //pDialogg.setProgressStyle(0);
        mDialog.getWindow().setDimAmount(0.4f); //뒤 배경 opacity 90퍼센트
        mDialog.setCancelable(false);
        WindowManager.LayoutParams params = mDialog.getWindow().getAttributes();//다이얼로그 사이즈 휴대폰 기기에 맞춰서 사이즈 변경
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        mDialog.getWindow().setAttributes(params);
        mDialog.show();

    }

    public void closeDialog() {
        mDialog.dismiss();
    }//다이얼로그 창 닫기

    private static class TIME_MAXIMUM{
        public static final int SEC = 60;
        public static final int MIN = 60;
        public static final int HOUR = 24;
        public static final int DAY = 30;
        public static final int MONTH = 12;
    }

    public static String formatTimeString(long regTime) {
        long curTime = System.currentTimeMillis();
        long diffTime = (curTime - regTime) / 1000;
        String msg = null;
        if (diffTime < TIME_MAXIMUM.SEC) {
            msg = "방금 전";
        } else if ((diffTime /= TIME_MAXIMUM.SEC) < TIME_MAXIMUM.MIN) {
            msg = diffTime + "분 전";
        } else if ((diffTime /= TIME_MAXIMUM.MIN) < TIME_MAXIMUM.HOUR) {
            msg = (diffTime) + "시간 전";
        } else if ((diffTime /= TIME_MAXIMUM.HOUR) < TIME_MAXIMUM.DAY) {
            msg = (diffTime) + "일 전";
        } else if ((diffTime /= TIME_MAXIMUM.DAY) < TIME_MAXIMUM.MONTH) {
            msg = (diffTime) + "달 전";
        } else {
            msg = (diffTime) + "년 전";
        }
        return msg;
    }

    /*
     * 숫자 3자리 마다 콤마
     * */
    public static String getNumberFormat (int getData) {

        NumberFormat nf = NumberFormat.getNumberInstance();
        String str = nf.format(getData);

        return str;
    }

    /*
     * AES256 인코딩 암호화
     * */
    public static String getAESstring(String getData) {
        String key = "abcdefghijklmnopqrstuvwxyz123456";

        String encodeText = null;
        try {
            encodeText = AES256Cipher.AES_Encode(getData, key);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return encodeText;
    }

    /*
     * AES256 디코딩 암호화
     * */
    public static String getAESdecode(String getData) {

        String key = "abcdefghijklmnopqrstuvwxyz123456";

        String decodeText = null;
        try {
            decodeText = AES256Cipher.AES_Decode(getData, key);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return decodeText;
    }

    /*
     * url 인코딩
     * */
    public static String urlencode(String getData) {

        String encodeText = null;
        try {
            encodeText = URLEncoder.encode(getData, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return encodeText;
    }

    /**
     * 루팅 채크
     * */
    public boolean RootingChecks(){
        int i = 0;
        String[] arrayOfString = {"/sbin/su", "/system/su", "/system/sbin/su", "/system/xbin/su", "/data/data/com.noshufou.android.su", "/system/app/Superuser.apk" , "/system/bin/.ext", "/system/xbin/.ext"};

        while(true) {
            if(i >= arrayOfString.length)
                return false;
            if(new File(arrayOfString[i]).exists())
                return true;
            i++;
        }

    }

    /*
     * 현재 날짜
     * */

    public String getDate(){

        long CurrentTime = System.currentTimeMillis(); // 현재 시간을 msec 단위로 얻음
        Date TodayDate = new Date(CurrentTime); // 현재 시간 Date 변수에 저장
        SimpleDateFormat SDFormat = new SimpleDateFormat("dd");
        strSDFormatDay = SDFormat.format(TodayDate);

        return strSDFormatDay;

    }

    /*
     * 사용자 아이디
     * */

    public String getUserId(){

        return CommonUtil.mSharePrefreences(mContext, getString(R.string.share_userId), null, "guest");

    }

    /* 접근권한 */
    public String getAccess(){

        return CommonUtil.mSharePrefreences(mContext, getString(R.string.share_access), null, "0");

    }

    /*
     * 팝업 공지
     * */

    public String getNoticeCheck(){

        return CommonUtil.mSharePrefreences(mContext, getString(R.string.share_notice), null, "0");

    }

    public Boolean noticeCheck () {

        long CurrentTime = System.currentTimeMillis(); // 현재 시간을 msec 단위로 얻음
        Date TodayDate = new Date(CurrentTime); // 현재 시간 Date 변수에 저장
        SimpleDateFormat SDFormat = new SimpleDateFormat("dd");
        strSDFormatDay = SDFormat.format(TodayDate); // 'dd' 형태로 포맷 변경

        String strSPreferencesDay = getNoticeCheck();

        if((Integer.parseInt(strSDFormatDay) - Integer.parseInt(strSPreferencesDay)) != 0) {
            return true;
        } else {
            return false;
        }

    }

    /*
     * 앱 버젼 가져오기
     * */
    public static String getVersion() {

        PackageInfo info;
        String version = "1.0.0";
        try {
            info = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            version = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            version = "1.0.0";
        }

        return version;
    }

    /*
     * 폰번호 가져오기
     * */
    public static String getPhoneNumber(Context c){
        String phoneNumber = "";
        TelephonyManager telManager = (TelephonyManager)c.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            phoneNumber = telManager.getLine1Number();
            if ( phoneNumber != null)
                phoneNumber = phoneNumber.replace("+82", "0");
        } catch (Exception e) {}
        return phoneNumber;
    }

    /*
     * 앱 OS
     * */
    public static String getOs() {

        return "001";
    }

    /**
     * 키 해쉬값 얻기 (카카오 로그인)
     */

    private void getHashKey(){
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo == null)
            Log.e("KeyHash", "KeyHash:null");

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            } catch (NoSuchAlgorithmException e) {
                Log.e("KeyHash", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
    }

    /**
     * 기기 고유 아이디 생성 및 저장
     */
    public String getUniqueID() {
        String deviceId;
        //final TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, imei, androidId;

        //tmDevice = "" + tm.getDeviceId();
        //tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        //UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
        //deviceId = deviceUuid.toString();
        return androidId;
    }


    /**
     * @param data
     * @return 금액 콤마 포멧
     */
    public String Format(String data){
        Long result = Long.valueOf(data);
        return new DecimalFormat("###,###,###,###P").format(result);
    }

    /**
     * 프로그레스 생성 메인에서 한번만 요청
     * @return
     */
    public void setProgress(){
        View i = LayoutInflater.from(this).inflate(R.layout.progress, null);
        mDialog = new Dialog(this);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(i);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(false);
        mDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if ( keyCode == KeyEvent.KEYCODE_BACK) {
                    if (mDialog.isShowing()) {
                        mDialog.dismiss();
                        finish();
                    }
                }
                return false;
            }
        });
    }

    public void showLoding(){
        if(!isFinishing()){
            mDialog.show();
        }
    }

    public void hideLoding(){
        /*Activity activity = mDialog.getOwnerActivity();
        if( activity!=null && !activity.isFinishing()) {
            mDialog.dismiss();
        }
         */
        mDialog.dismiss();
    }

    public boolean isLoding(){
        return mDialog.isShowing();
    }

    public void ToastMessage(String msg) {

    }

    /*
    * 토스트 기본
    * */
    public void showToast(String msg){
        //Toast toast ;
        toast = Toast.makeText(this, msg , Toast.LENGTH_SHORT);
        int offsetX = 0;
        int offsetY = 0;
        toast.setGravity(Gravity.CENTER, offsetX, offsetY);
        toast.show();
    }

    public void showToastLong(String msg){
        //Toast toast ;
        toast = Toast.makeText(this, msg , Toast.LENGTH_LONG);
        int offsetX = 0;
        int offsetY = 0;
        toast.setGravity(Gravity.CENTER, offsetX, offsetY);
        toast.show();
    }

    public void showToastTop(String msg){
        //Toast toast ;
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(this, msg , Toast.LENGTH_SHORT);
        int offsetX = 0;
        int offsetY = 0;
        toast.setGravity(Gravity.TOP, offsetX, offsetY);
        toast.show();

    }

    public void hideToast() {
        if (toast != null) {
            toast.cancel();
        }
    }




    // 스크린 사이즈 계산
    public int ScreenSize(int i , int ii , int minus){
        int i2 = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, minus, getResources().getDisplayMetrics());

        int size = 0;
        DisplayMetrics mDisplay = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mDisplay);

        size = mDisplay.widthPixels - i2 ;
        size = size / i;
        size = size * ii;

        return size;
    }

    // 스크린 사이즈 계산
    public int ScreenSize(double i , int ii , int minus){
        int i2 = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, minus, getResources().getDisplayMetrics());

        int size = 0;
        DisplayMetrics mDisplay = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mDisplay);

        size = mDisplay.widthPixels - i2 ;
        size = (int)(size / i);
        size = (int)(size * ii);

        return size;
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);

            if(listItem != null){
                // This next line is needed before you call measure or else you won't get measured height at all. The listitem needs to be drawn first to know the height.
                listItem.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                totalHeight += listItem.getMeasuredHeight();
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }



    @Override
    protected void onDestroy() {
        System.gc();

        super.onDestroy();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.none, R.anim.none);
        //overridePendingTransition(R.anim.none, R.anim.right_out); // 오른쪽으로 사라지는 애니메이션
    }

}
