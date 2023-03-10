package com.example.song4u.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.song4u.R;
import com.example.song4u.network.resultmodel.GetMelonSearchDataResultModel;

import java.util.ArrayList;

public class MusicSearchList_Adapter extends BaseAdapter {

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<GetMelonSearchDataResultModel> getMelonSearchDataResultModel;

    public MusicSearchList_Adapter(Context context, ArrayList<GetMelonSearchDataResultModel> list) {
        mContext = context;
        getMelonSearchDataResultModel = list;
        mLayoutInflater = LayoutInflater.from(mContext);

    }

    @Override
    public int getCount() {
        return getMelonSearchDataResultModel.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public GetMelonSearchDataResultModel getItem(int position) {
        return getMelonSearchDataResultModel.get(position);
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {

        converView = mLayoutInflater.inflate(R.layout.list_item_musicsearch, null);

        TextView titleName = (TextView) converView.findViewById(R.id.tText);
        TextView singerName = (TextView) converView.findViewById(R.id.sText);

        titleName.setText("제목 : "+getMelonSearchDataResultModel.get(position).getTitle());
        singerName.setText("가수 : "+getMelonSearchDataResultModel.get(position).getSinger());

        return converView;
    }

}
