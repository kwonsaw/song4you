package com.example.song4u.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.song4u.R;
import com.example.song4u.Util.SFUTimeago;
import com.example.song4u.network.resultmodel.GetReplyDataResultModel;

import java.text.ParseException;
import java.util.ArrayList;

public class ReplyListAdapter extends BaseAdapter {

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<GetReplyDataResultModel> getReplyDataResultModel;
    private ReplyListAdapter.OnItemClickListener mListener = null ;

    public ReplyListAdapter(Context context, ArrayList<GetReplyDataResultModel> list) {
        mContext = context;
        getReplyDataResultModel = list;
        mLayoutInflater = LayoutInflater.from(mContext);

    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position) ;
    }

    public void setOnItemClickListener(ReplyListAdapter.OnItemClickListener listener) {
        this.mListener = listener ;
    }

    @Override
    public int getCount() {
        return getReplyDataResultModel.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public GetReplyDataResultModel getItem(int position) {
        return getReplyDataResultModel.get(position);
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {

        converView = mLayoutInflater.inflate(R.layout.recycler_item_reply, null);

        ImageView imageView = (ImageView) converView.findViewById(R.id.mImage);
        TextView mTextView = (TextView) converView.findViewById(R.id.mText);
        TextView dTextView = (TextView) converView.findViewById(R.id.dText);
        TextView tTextView = (TextView) converView.findViewById(R.id.tText);
        Button btn = (Button) converView.findViewById(R.id.pbtn);
        ImageView sImageView = (ImageView) converView.findViewById(R.id.sImage);

        sImageView.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(v, position) ;
                }
            }
        });

        /*
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = getAdapterPosition() ;
                if (pos != RecyclerView.NO_POSITION) {
                    // 리스너 객체의 메서드 호출.
                    if (mListener != null) {
                        mListener.onItemClick(v, pos) ;
                    }
                }
            }
        });
         */

        Glide.with(mContext)
                .load(getReplyDataResultModel.get(position).getProfileimgurl())
                .circleCrop()
                //.apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(mContext, 15, 0, RoundedCornersTransformation.CornerType.ALL)))
                .error(R.drawable.ic_launcher_foreground)
                .into(imageView);

        mTextView.setText(getReplyDataResultModel.get(position).getNickname());
        dTextView.setText(getReplyDataResultModel.get(position).getDescription());

        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        try {
            java.util.Date date = format.parse(getReplyDataResultModel.get(position).getInserteddatetime());
            tTextView.setText(SFUTimeago.formatTimeString(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (getReplyDataResultModel.get(position).getMyreply().equalsIgnoreCase("y")) {
            btn.setText("수정");
        } else {
            btn.setText("신고");
        }

        return converView;
    }

}
