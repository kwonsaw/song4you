package com.example.song4u.Util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.song4u.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CommonUtil extends AppCompatActivity {

    public Dialog mDialog;
    public Toast toast;

    private static SharedPreferences mSharePreFerence;
    private static SharedPreferences.Editor mEditor;

    private static Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getApplication().getApplicationContext();

    }

    public static String mSharePrefreences(Context context , String param , String putValue , String default_str){
        String result = "";
        if (mSharePreFerence == null ) {
            mSharePreFerence = context.getSharedPreferences(context.getString(R.string.app_name), MODE_PRIVATE);
            mEditor = mSharePreFerence.edit();
        }

        if(param != null && putValue != null){
            mEditor.putString(param , putValue);
            mEditor.commit();
        } else if ( param != null){
            result =  mSharePreFerence.getString(param , "");
            if ( result.equalsIgnoreCase("")){
                mEditor.putString(param , default_str);
                mEditor.commit();
                result = default_str;
            }
        }

        return result;
    }

    /*
     * 사용자 아이디
     * */

    public static String getUserId(){

        if (mSharePreFerence == null ) {
            mSharePreFerence = mContext.getSharedPreferences(mContext.getString(R.string.app_name), MODE_PRIVATE);
            mEditor = mSharePreFerence.edit();
        }

        return mSharePreFerence.getString("share_userId", "");

    }

    /*
     * 사용자 닉네임
     * */

    public static String getNickname(){

        return mSharePreFerence.getString("share_nickname", "");

    }

    /*
     * 사용자 프로필 이미지
     * */

    public static String getImageUrl(){

        return mSharePreFerence.getString("share_profileurl", "");

    }

    /*
     * 사용자 로그인 타입
     * */

    public static String getLogintype(){

        return mSharePreFerence.getString("share_loginType", "");


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
     * 앱 버젼 추상 클래서에서 가져오기
     * */
    public static String getVerSion(Context context) {

        PackageInfo info;
        String version = "1.0.0";
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            version = "1.0.0";
        }

        return version;
    }

    public static void getResources(Context context) {

        context.getResources();

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

    public static void showLoading(Activity activity, boolean isShow) {
        if(isShow) {
            LinearLayout linear = new LinearLayout(activity);
            linear.setTag("MyProgressBar");
            linear.setGravity(Gravity.CENTER);
            linear.setBackgroundColor(0x33000000);
            ProgressBar progressBar = new ProgressBar(activity);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            progressBar.setLayoutParams(layoutParams);
            linear.addView(progressBar);
            linear.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) { /*클릭방지*/ }
            });

            FrameLayout rootView = activity.findViewById(android.R.id.content);
            rootView.addView(linear);
        } else {
            FrameLayout rootView = activity.findViewById(android.R.id.content);
            LinearLayout linear = rootView.findViewWithTag("MyProgressBar");
            if(linear != null) {
                rootView.removeView(linear);
            }
        }
    }

    /* * Gets the file path of the given Uri. */
    @SuppressLint("NewApi")
    public static String getPath(Uri uri, Context context) {

        final boolean needToCheckUri = Build.VERSION.SDK_INT >= 19;
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to // deal with different Uris.
        if (needToCheckUri && DocumentsContract.isDocumentUri(context, uri)) {

            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                if (id.startsWith("raw:")) {
                    return id.replaceFirst("raw:", "");
                }
                uri = ContentUris.withAppendedId( Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                switch (type) {
                    case "image":
                        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        break;
                    case "video":
                        uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                        break;
                    case "audio":
                        uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                        break;
                }
                selection = "_id=?";
                selectionArgs = new String[]{ split[1] };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { MediaStore.Images.Media.DATA };
            try (
                    Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null)
            ) { if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                return cursor.getString(columnIndex);
            }
            } catch (Exception e) {
                Log.e("on getPath", "Exception", e);
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /** * @param uri The Uri to check. * @return Whether the Uri authority is ExternalStorageProvider. */
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }
    /** * @param uri The Uri to check. * @return Whether the Uri authority is DownloadsProvider. */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }
    /** * @param uri The Uri to check. * @return Whether the Uri authority is MediaProvider. */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /* 이미지 압축, 품질, 회전 처리 */

    public static Bitmap getimage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //      ，   options.inJustDecodeBounds   true
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//    bm

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = 800f;//       800f
        float ww = 480f;//       480f
        //   。         ，
        int be = 1;//be=1
        if (w > h && w > ww) {//
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//
        //      ，       options.inJustDecodeBounds   false
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap);//
    }

    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//      ，  100     ，          baos
        int options = 99;//     0-100     0 100
        while ( baos.toByteArray().length / 1024>100 && options > 0) {	//               100kb,
            baos.reset();//  baos   baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//    options%，          baos
            options -= 10;//     10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//       baos   ByteArrayInputStream
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// ByteArrayInputStream
        return bitmap;
    }

    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        }catch (IOException e) {
            e.printStackTrace();
        }

        return degree;
    }

    public static String StringReplace(String str){
        String match = "[^\uAC00-\uD7A30-9a-zA-Z]";
        str = str.replaceAll(match, "");
        return str;
    }

    /**
     *
     * @param bitmap
     * @param rotate
     * @return
     */
    private static Bitmap rotateBitmap(Bitmap bitmap, int rotate) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postRotate(rotate);
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
    }

    public static String compress(String path) {
        Bitmap bm = getimage(path);//
        if(bm == null){
            return null;
        }
        int degree = readPictureDegree(path);//
        if (degree > 0) {
            bm = rotateBitmap(bm, degree); //
        }
        String file = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "temp" + File.separator;
        File f = new File(file);
        if(!f.exists()){
            f.mkdirs();

        }


        //String picName = userId + ".jpg";
        String picName = System.currentTimeMillis() + ".jpg";
        String resultFilePath = file + picName;
        try {
            FileOutputStream out = new FileOutputStream(resultFilePath);
            bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultFilePath;
    }
}
