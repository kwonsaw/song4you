package com.example.song4u.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.song4u.Data.MoreListData;
import com.example.song4u.R;
import com.example.song4u.Util.CommonUtil;

import java.util.ArrayList;

public class MoreListAdapter extends BaseAdapter {

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    DisplayMetrics displayMetrics;
    ArrayList<MoreListData> moreListDataArrayList;

    public MoreListAdapter(Context context, ArrayList<MoreListData> data, DisplayMetrics dm) {
        mContext = context;
        moreListDataArrayList = data;
        mLayoutInflater = LayoutInflater.from(mContext);

        displayMetrics = dm;
    }



    @Override
    public int getCount() {
        return moreListDataArrayList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public MoreListData getItem(int position) {
        return moreListDataArrayList.get(position);
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {

        converView = mLayoutInflater.inflate(R.layout.list_item_more, null);

        TextView listName = (TextView) converView.findViewById(R.id.mText);
        TextView verName = (TextView) converView.findViewById(R.id.verTxt);
        ImageView arrowImg = (ImageView) converView.findViewById(R.id.mImg);

        listName.setText(moreListDataArrayList.get(position).getListName());


        if (position == 0 || position == 4) {

            listName.setTypeface(null, Typeface.BOLD);
            listName.setTextSize(20);
            arrowImg.setVisibility(View.GONE);

            if (position == 4) {

                int sizeTop = Math.round(30 * displayMetrics.density);
                int sizeLeft = Math.round(15 * displayMetrics.density);
                int sizeBottom = Math.round(10 * displayMetrics.density);

                RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                param.topMargin = sizeTop;
                param.leftMargin = sizeLeft;
                param.bottomMargin = sizeBottom;
                listName.setLayoutParams(param);
            }

        } else if (position == 8) {
            arrowImg.setVisibility(View.GONE);
            verName.setVisibility(View.VISIBLE);

            verName.setText(CommonUtil.getVerSion(mContext));
        }

        return converView;
    }

}
