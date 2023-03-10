package com.example.song4u.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.song4u.Data.AccessListData;
import com.example.song4u.R;

import java.util.ArrayList;

public class AccessListAdapter extends BaseAdapter {

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<AccessListData> accessListDataArrayList;

    public AccessListAdapter(Context context, ArrayList<AccessListData> data) {
        mContext = context;
        accessListDataArrayList = data;
        mLayoutInflater = LayoutInflater.from(mContext);

    }



    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public AccessListData getItem(int position) {
        return accessListDataArrayList.get(position);
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {

        converView = mLayoutInflater.inflate(R.layout.list_item_access, null);

        TextView listName = (TextView) converView.findViewById(R.id.mText);
        TextView sTextView = (TextView) converView.findViewById(R.id.sText);
        ImageView arrowImg = (ImageView) converView.findViewById(R.id.mImage);

        listName.setText(accessListDataArrayList.get(position).getListName());

        if (position == 0) {
            Glide.with(mContext)
                    .load(R.drawable.ic_baseline_person_24)
                    .error(R.drawable.silver)
                    .into(arrowImg);
            sTextView.setText("포인트 참여적립 확인을 위해 사용");
        } else if (position == 1) {
            Glide.with(mContext)
                    .load(R.drawable.ic_baseline_phone_24)
                    .error(R.drawable.silver)
                    .into(arrowImg);
            sTextView.setText("광고주에게 전화걸기 기능에 사용");
        } else if (position == 2) {
            Glide.with(mContext)
                    .load(R.drawable.ic_baseline_camera_alt_24)
                    .error(R.drawable.silver)
                    .into(arrowImg);
            sTextView.setText("프로필 사진 변경을 위해 사용");
        } else {
            Glide.with(mContext)
                    .load(R.drawable.ic_baseline_folder_24)
                    .error(R.drawable.silver)
                    .into(arrowImg);
            sTextView.setText("프로필 사진 변경을 위해 사용");
        }


        return converView;
    }

}
