package com.example.song4u.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.song4u.R;
import com.example.song4u.Util.RoundedCornersTransformation;
import com.example.song4u.network.resultmodel.GetMusicListDataResultModel;

import java.util.ArrayList;

public class RankListAdapter extends BaseAdapter {

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<GetMusicListDataResultModel> getMusicListDataResultModel;

    public RankListAdapter(Context context, ArrayList<GetMusicListDataResultModel> list) {
        mContext = context;
        getMusicListDataResultModel = list;
        mLayoutInflater = LayoutInflater.from(mContext);

    }

    @Override
    public int getCount() {
        return getMusicListDataResultModel.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public GetMusicListDataResultModel getItem(int position) {
        return getMusicListDataResultModel.get(position);
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {

        converView = mLayoutInflater.inflate(R.layout.recycler_item_musicrank, null);

        ImageView imageView;
        ImageView simageView;
        ImageView rimageView;
        TextView mTextView;
        TextView cTextView;
        TextView rTextView;

        imageView = (ImageView) converView.findViewById(R.id.mImage);
        simageView = (ImageView) converView.findViewById(R.id.sImage);
        rimageView = (ImageView) converView.findViewById(R.id.rImage);
        mTextView = (TextView) converView.findViewById(R.id.mText);
        cTextView = (TextView) converView.findViewById(R.id.cText);
        rTextView = (TextView) converView.findViewById(R.id.rText);

        if (position == 0) {
            rimageView.setVisibility(View.VISIBLE);
            rTextView.setVisibility(View.GONE);
            Glide.with(mContext)
                    .load(R.drawable.gold)
                    //.circleCrop()
                    //.apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(mContext, 15, 0, RoundedCornersTransformation.CornerType.ALL)))
                    .error(R.drawable.gold)
                    .into(rimageView);

        } else if (position == 1) {
            rimageView.setVisibility(View.VISIBLE);
            rTextView.setVisibility(View.GONE);
            Glide.with(mContext)
                    .load(R.drawable.silver)
                    //.circleCrop()
                    //.apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(mContext, 15, 0, RoundedCornersTransformation.CornerType.ALL)))
                    .error(R.drawable.silver)
                    .into(rimageView);
        } else if (position == 2){
            rimageView.setVisibility(View.VISIBLE);
            rTextView.setVisibility(View.GONE);
            Glide.with(mContext)
                    .load(R.drawable.bronze)
                    //.circleCrop()
                    //.apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(mContext, 15, 0, RoundedCornersTransformation.CornerType.ALL)))
                    .error(R.drawable.bronze)
                    .into(rimageView);
        } else {
            rimageView.setVisibility(View.GONE);
            rTextView.setVisibility(View.VISIBLE);

            rTextView.setText(String.valueOf(position+1));
        }

        Glide.with(mContext)
                .load(getMusicListDataResultModel.get(position).getMainimgurl())
                //.circleCrop()
                .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(mContext, 15, 0, RoundedCornersTransformation.CornerType.ALL)))
                //.error(R.drawable.gold)
                .into(imageView);

        if (getMusicListDataResultModel.get(position).getType().equalsIgnoreCase("melon")) {
            Glide.with(mContext)
                    .load(R.drawable.melonlogo)
                    //.circleCrop()
                    .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(mContext, 15, 0, RoundedCornersTransformation.CornerType.ALL)))
                    .error(R.drawable.melonlogo)
                    .into(simageView);
        } else {
            Glide.with(mContext)
                    .load(R.drawable.genielogo)
                    //.circleCrop()
                    .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(mContext, 15, 0, RoundedCornersTransformation.CornerType.ALL)))
                    .error(R.drawable.genielogo)
                    .into(simageView);
        }

        String ustr = getMusicListDataResultModel.get(position).getTitle()+"\n"+getMusicListDataResultModel.get(position).getSinger();
        SpannableStringBuilder sb1 = new SpannableStringBuilder(ustr);
        sb1.setSpan(new ForegroundColorSpan(Color.BLACK), 0, getMusicListDataResultModel.get(position).getTitle().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb1.setSpan(new StyleSpan(Typeface.BOLD), 0,getMusicListDataResultModel.get(position).getTitle().length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        sb1.setSpan(new AbsoluteSizeSpan(15, true), 0, getMusicListDataResultModel.get(position).getTitle().length()
                , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // Size
        mTextView.setText(sb1);

        //holder.mTextView.setText(getMusicListDataResultModel.get(position).getTitle()+"\n"+getMusicListDataResultModel.get(position).getSinger());
        cTextView.setText(getMusicListDataResultModel.get(position).getTotalplay_cnt()+"회");

        return converView;
    }

}
