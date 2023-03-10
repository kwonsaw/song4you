package com.example.song4u.Util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class SFUToast {

    public static void showToast(Context context, String msg) {
        Toast toast = Toast.makeText(context, msg , Toast.LENGTH_SHORT);
        int offsetX = 0;
        int offsetY = 0;
        toast.setGravity(Gravity.CENTER, offsetX, offsetY);
        toast.show();
    }

    public static void showToastLong(Context context, String msg) {
        Toast toast = Toast.makeText(context, msg , Toast.LENGTH_LONG);
        int offsetX = 0;
        int offsetY = 0;
        toast.setGravity(Gravity.CENTER, offsetX, offsetY);
        toast.show();
    }
}
