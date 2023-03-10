package com.example.song4u.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.song4u.R;
import com.example.song4u.network.resultmodel.GetPointHistoryDataResultModel;

import java.util.ArrayList;

public class PointHistoryAdapter extends BaseAdapter {

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<GetPointHistoryDataResultModel> getPointHistoryDataResultModel;

    public PointHistoryAdapter(Context context, ArrayList<GetPointHistoryDataResultModel> list) {
        mContext = context;
        getPointHistoryDataResultModel = list;
        mLayoutInflater = LayoutInflater.from(mContext);

    }

    @Override
    public int getCount() {
        return getPointHistoryDataResultModel.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public GetPointHistoryDataResultModel getItem(int position) {
        return getPointHistoryDataResultModel.get(position);
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {

        converView = mLayoutInflater.inflate(R.layout.list_item_pointhistory, null);

        TextView listName = (TextView) converView.findViewById(R.id.mText);
        TextView dateName = (TextView) converView.findViewById(R.id.dateText);
        TextView pName = (TextView) converView.findViewById(R.id.pText);

        listName.setText(getPointHistoryDataResultModel.get(position).getContentTitle());
        dateName.setText(getPointHistoryDataResultModel.get(position).getInsertedDatetime());

        if (getPointHistoryDataResultModel.get(position).getContentTitle().contains("적립")) {
            pName.setTextColor(Color.parseColor("#9773FF"));
        } else {
            pName.setTextColor(Color.RED);
        }

        pName.setText(""+getPointHistoryDataResultModel.get(position).getSaveMoney()+"P");

        return converView;
    }

}
