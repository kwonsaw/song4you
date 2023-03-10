package com.example.song4u.Util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;

import com.example.song4u.R;

public class SFUAlertDialog {

    public static void singleButtonShowAlert(Context context, String title, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message).setCancelable(false).setNeutralButton("확인", listener);
        AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(context.getResources().getColor(R.color.songcolor));
            }
        });
        alertDialog.show();
    }

    public static void doubleButtonShowAlert(Context context, String title, String message, DialogInterface.OnClickListener leftListener, DialogInterface.OnClickListener rightListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message).setCancelable(false).setNegativeButton("취소", leftListener).setPositiveButton("확인", rightListener);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public static void doubleButtonShowAlert(Context context, String title, String message, String leftStr, DialogInterface.OnClickListener leftListener, String rightStr, DialogInterface.OnClickListener rightListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message).setCancelable(false).setNegativeButton(leftStr, leftListener).setPositiveButton(rightStr, rightListener);
        AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.songcolor));
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.DKGRAY);
            }
        });

        alertDialog.show();
    }


    public static void doubleLoginButtonShowAlert(final Context context) {
        final Activity activity = (Activity) context;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("로그인이 필요합니다.");
        builder.setMessage("로그인을 하시겠습니까?").setCancelable(false).setNegativeButton("취소", null).setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //activity.startActivityForResult(LoginActivity.newIntent(activity), MainActivity.LOGIN_ACTIVITY_RESULT);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public static void doubleLoginButtonShowAlert(final Context context, DialogInterface.OnClickListener cancelListener) {
        final Activity activity = (Activity) context;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("로그인이 필요합니다.");
        builder.setMessage("로그인을 하시겠습니까?").setCancelable(false).setNegativeButton("취소", cancelListener).setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //activity.startActivityForResult(LoginActivity.newIntent(activity), MainActivity.LOGIN_ACTIVITY_RESULT);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
