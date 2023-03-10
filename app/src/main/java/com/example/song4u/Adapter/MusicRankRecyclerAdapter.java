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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.song4u.R;
import com.example.song4u.Util.RoundedCornersTransformation;
import com.example.song4u.network.resultmodel.GetMusicListDataResultModel;

import java.util.ArrayList;

public class MusicRankRecyclerAdapter extends RecyclerView.Adapter<MusicRankRecyclerAdapter.ViewHolder> {

    Context mContext = null;
    ArrayList<GetMusicListDataResultModel> getMusicListDataResultModel;
    private MusicRankRecyclerAdapter.OnItemClickListener mListener = null ;

    public MusicRankRecyclerAdapter(Context context, ArrayList<GetMusicListDataResultModel> list) {
        mContext = context;
        getMusicListDataResultModel = list;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageView simageView;
        ImageView rimageView;
        TextView mTextView;
        TextView cTextView;
        TextView rTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.mImage);
            simageView = (ImageView) itemView.findViewById(R.id.sImage);
            rimageView = (ImageView) itemView.findViewById(R.id.rImage);
            mTextView = (TextView) itemView.findViewById(R.id.mText);
            cTextView = (TextView) itemView.findViewById(R.id.cText);
            rTextView = (TextView) itemView.findViewById(R.id.rText);


            itemView.setOnClickListener(new View.OnClickListener() {
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

    public void setOnItemClickListener(MusicRankRecyclerAdapter.OnItemClickListener listener) {
        this.mListener = listener ;
    }

    // 아이템 뷰를 위한 뷰홀더 객체를 생성하여 리턴
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.recycler_item_musicrank, parent, false);
        MusicRankRecyclerAdapter.ViewHolder vh = new MusicRankRecyclerAdapter.ViewHolder(view);
        return vh;
    }

    // position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    @Override
    public void onBindViewHolder(@NonNull MusicRankRecyclerAdapter.ViewHolder holder, int position) {

        if (position == 0) {
            holder.rimageView.setVisibility(View.VISIBLE);
            holder.rTextView.setVisibility(View.GONE);
            Glide.with(mContext)
                    .load(R.drawable.ic_baseline_looks_one_24)
                    //.circleCrop()
                    //.apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(mContext, 15, 0, RoundedCornersTransformation.CornerType.ALL)))
                    .error(R.drawable.ic_baseline_looks_one_24)
                    .into(holder.rimageView);

        } else if (position == 1) {
            holder.rimageView.setVisibility(View.VISIBLE);
            holder.rTextView.setVisibility(View.GONE);
            Glide.with(mContext)
                    .load(R.drawable.ic_baseline_looks_two_24)
                    //.circleCrop()
                    //.apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(mContext, 15, 0, RoundedCornersTransformation.CornerType.ALL)))
                    .error(R.drawable.ic_baseline_looks_two_24)
                    .into(holder.rimageView);
        } else if (position == 2){
            holder.rimageView.setVisibility(View.VISIBLE);
            holder.rTextView.setVisibility(View.GONE);
            Glide.with(mContext)
                    .load(R.drawable.ic_baseline_looks_3_24)
                    //.circleCrop()
                    //.apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(mContext, 15, 0, RoundedCornersTransformation.CornerType.ALL)))
                    .error(R.drawable.ic_baseline_looks_3_24)
                    .into(holder.rimageView);
        } else {
            holder.rimageView.setVisibility(View.GONE);
            holder.rTextView.setVisibility(View.VISIBLE);

            holder.rTextView.setText(String.valueOf(position+1));
        }

        Glide.with(mContext)
                .load(getMusicListDataResultModel.get(position).getMainimgurl())
                //.circleCrop()
                .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(mContext, 15, 0, RoundedCornersTransformation.CornerType.ALL)))
                //.error(R.drawable.gold)
                .into(holder.imageView);

        if (getMusicListDataResultModel.get(position).getType().equalsIgnoreCase("melon")) {
            Glide.with(mContext)
                    .load(R.drawable.melonlogo)
                    //.circleCrop()
                    .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(mContext, 15, 0, RoundedCornersTransformation.CornerType.ALL)))
                    .error(R.drawable.melonlogo)
                    .into(holder.simageView);
        } else {
            Glide.with(mContext)
                    .load(R.drawable.genielogo)
                    //.circleCrop()
                    .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(mContext, 15, 0, RoundedCornersTransformation.CornerType.ALL)))
                    .error(R.drawable.genielogo)
                    .into(holder.simageView);
        }

        String typeStr = "";

        if (getMusicListDataResultModel.get(position).getType().equalsIgnoreCase("melon")){
            typeStr = "멜론";
        } else {
            typeStr = "지니";
        }

        String ustr = getMusicListDataResultModel.get(position).getTitle()+"\n"+getMusicListDataResultModel.get(position).getSinger()+"\n잔여재생 "+getMusicListDataResultModel.get(position).getPlay_cnt()+"회";
        SpannableStringBuilder sb1 = new SpannableStringBuilder(ustr);
        sb1.setSpan(new ForegroundColorSpan(Color.BLACK), 0, getMusicListDataResultModel.get(position).getTitle().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb1.setSpan(new StyleSpan(Typeface.BOLD), 0,getMusicListDataResultModel.get(position).getTitle().length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        sb1.setSpan(new AbsoluteSizeSpan(15, true), 0, getMusicListDataResultModel.get(position).getTitle().length()
                , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // Size
        sb1.setSpan(new ForegroundColorSpan(Color.BLACK), getMusicListDataResultModel.get(position).getTitle().length()+getMusicListDataResultModel.get(position).getSinger().length()+1, ustr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.mTextView.setText(sb1);

        //holder.mTextView.setText(getMusicListDataResultModel.get(position).getTitle()+"\n"+getMusicListDataResultModel.get(position).getSinger());
        holder.cTextView.setText(getMusicListDataResultModel.get(position).getTotalplay_cnt()+"회");

    }

    @Override
    public int getItemCount() {
        return getMusicListDataResultModel.size();
    }


}
