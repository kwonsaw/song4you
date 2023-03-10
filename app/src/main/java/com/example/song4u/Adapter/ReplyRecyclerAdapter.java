package com.example.song4u.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.song4u.R;
import com.example.song4u.Util.SFUTimeago;
import com.example.song4u.network.resultmodel.GetReplyDataResultModel;

import java.text.ParseException;
import java.util.ArrayList;

public class ReplyRecyclerAdapter extends RecyclerView.Adapter<ReplyRecyclerAdapter.ViewHolder> {

    Context mContext = null;
    ArrayList<GetReplyDataResultModel> getReplyDataResultModel;
    private ReplyRecyclerAdapter.OnItemClickListener mListener = null ;

    public ReplyRecyclerAdapter(Context context, ArrayList<GetReplyDataResultModel> list) {
        mContext = context;
        getReplyDataResultModel = list;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView mTextView;
        TextView dTextView;
        TextView tTextView;
        Button btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.mImage);
            mTextView = (TextView) itemView.findViewById(R.id.mText);
            dTextView = (TextView) itemView.findViewById(R.id.dText);
            tTextView = (TextView) itemView.findViewById(R.id.tText);
            btn = (Button) itemView.findViewById(R.id.pbtn);


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


        }
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position) ;
    }

    public void setOnItemClickListener(ReplyRecyclerAdapter.OnItemClickListener listener) {
        this.mListener = listener ;
    }

    // 아이템 뷰를 위한 뷰홀더 객체를 생성하여 리턴
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.recycler_item_reply, parent, false);
        ReplyRecyclerAdapter.ViewHolder vh = new ReplyRecyclerAdapter.ViewHolder(view);
        return vh;
    }

    // position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    @Override
    public void onBindViewHolder(@NonNull ReplyRecyclerAdapter.ViewHolder holder, int position) {

        Glide.with(mContext)
                .load(getReplyDataResultModel.get(position).getProfileimgurl())
                .circleCrop()
                //.apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(mContext, 15, 0, RoundedCornersTransformation.CornerType.ALL)))
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.imageView);

        holder.mTextView.setText(getReplyDataResultModel.get(position).getNickname());
        holder.dTextView.setText(getReplyDataResultModel.get(position).getDescription());

        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        try {
            java.util.Date date = format.parse(getReplyDataResultModel.get(position).getInserteddatetime());
            holder.tTextView.setText(SFUTimeago.formatTimeString(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (getReplyDataResultModel.get(position).getMyreply().equalsIgnoreCase("y")) {
            holder.btn.setText("수정");
        } else {
            holder.btn.setText("신고");
        }



    }

    @Override
    public int getItemCount() {
        return getReplyDataResultModel.size();
    }


}
