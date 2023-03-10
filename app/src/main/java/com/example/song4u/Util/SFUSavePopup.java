package com.example.song4u.Util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.song4u.R;

public class SFUSavePopup {

    public static void showSavePopup(Context context, String msg) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.savepopupview, null);
        Dialog dilaog01= new Dialog(context);
        dilaog01.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dilaog01.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dilaog01.setContentView(view);

        TextView pTextView = (TextView) view.findViewById(R.id.savetext);
        pTextView.setText(msg+"P 적립되었습니다.");
        Button closeBtn = (Button) view.findViewById(R.id.close);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dilaog01.dismiss();
            }
        });

        dilaog01.show();
    }

}
