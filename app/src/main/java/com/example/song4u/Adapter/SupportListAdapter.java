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

public class SupportListAdapter extends BaseAdapter {

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<GetMusicListDataResultModel> getMusicListDataResultModel;
    private SupportListAdapter.OnItemClickListener mListener = null ;

    public SupportListAdapter(Context context, ArrayList<GetMusicListDataResultModel> list) {
        mContext = context;
        getMusicListDataResultModel = list;
        mLayoutInflater = LayoutInflater.from(mContext);

    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position) ;
    }

    public void setOnItemClickListener(SupportListAdapter.OnItemClickListener listener) {
        this.mListener = listener ;
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

        converView = mLayoutInflater.inflate(R.layout.list_item_support, null);

        ImageView imageView = (ImageView) converView.findViewById(R.id.mImage);
        TextView mTextView = (TextView) converView.findViewById(R.id.mText);
        TextView dTextView = (TextView) converView.findViewById(R.id.cText);

        Glide.with(mContext)
                .load(getMusicListDataResultModel.get(position).getMainimgurl())
                .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(mContext, 15, 0, RoundedCornersTransformation.CornerType.ALL)))
                .error(R.drawable.ic_launcher_foreground)
                .into(imageView);

        String ustr = getMusicListDataResultModel.get(position).getTitle()+"\n"+getMusicListDataResultModel.get(position).getSinger();
        SpannableStringBuilder sb1 = new SpannableStringBuilder(ustr);
        sb1.setSpan(new ForegroundColorSpan(Color.BLACK), 0, getMusicListDataResultModel.get(position).getTitle().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb1.setSpan(new StyleSpan(Typeface.BOLD), 0,getMusicListDataResultModel.get(position).getTitle().length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        sb1.setSpan(new AbsoluteSizeSpan(15, true), 0, getMusicListDataResultModel.get(position).getTitle().length()
                , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // Size
        mTextView.setText(sb1);

        dTextView.setText("잔여재생 "+getMusicListDataResultModel.get(position).getPlay_cnt()+"회");

        return converView;
    }

}
