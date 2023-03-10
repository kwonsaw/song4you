package com.example.song4u.Util;

import android.app.Dialog;

import android.content.Context;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.song4u.R;

public class ProgressDialog {

    private Context context;
    private Dialog dialog;

    ConstraintLayout.LayoutParams mLayoutParams;

    public ProgressDialog(Context context){this.context = context;}

    public void showDialog(){
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progress);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);//transparent로 맞추면 다이얼로그 주변 배경 사라짐
        //dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);  // 배경 검은색 없애기
        dialog.getWindow().setDimAmount(0.4f); //뒤 배경 opacity 90퍼센트
        dialog.setCancelable(false);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();//다이얼로그 사이즈 휴대폰 기기에 맞춰서 사이즈 변경
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(params);
        dialog.show();

    }
    public void closeDialog() {
        dialog.dismiss();
    }//다이얼로그 창 닫기

}