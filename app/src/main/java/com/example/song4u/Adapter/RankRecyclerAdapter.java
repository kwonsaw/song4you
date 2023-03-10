package com.example.song4u.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.song4u.R;
import com.example.song4u.network.resultmodel.GetUserInfoDataResultModel;

import java.text.NumberFormat;
import java.util.ArrayList;

public class RankRecyclerAdapter extends RecyclerView.Adapter<RankRecyclerAdapter.ViewHolder> {

    Context mContext = null;
    ArrayList<GetUserInfoDataResultModel> getUserInfoDataResultModel;
    private RankRecyclerAdapter.OnItemClickListener mListener = null ;

    public RankRecyclerAdapter(Context context, ArrayList<GetUserInfoDataResultModel> list) {
        mContext = context;
        getUserInfoDataResultModel = list;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageView uimageView;
        TextView uTextView;
        TextView pTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.mImage);
            uimageView = (ImageView) itemView.findViewById(R.id.uImage);
            uTextView = (TextView) itemView.findViewById(R.id.uText);
            pTextView = (TextView) itemView.findViewById(R.id.pText);


        }
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position) ;
    }

    public void setOnItemClickListener(RankRecyclerAdapter.OnItemClickListener listener) {
        this.mListener = listener ;
    }

    // 아이템 뷰를 위한 뷰홀더 객체를 생성하여 리턴
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.recycler_item_rank, parent, false);
        RankRecyclerAdapter.ViewHolder vh = new RankRecyclerAdapter.ViewHolder(view);
        return vh;
    }

    // position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    @Override
    public void onBindViewHolder(@NonNull RankRecyclerAdapter.ViewHolder holder, int position) {

        if (position == 0) {
            holder.imageView.setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load(R.drawable.ic_baseline_looks_one_24)
                    //.circleCrop()
                    //.apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(mContext, 15, 0, RoundedCornersTransformation.CornerType.ALL)))
                    .error(R.drawable.ic_baseline_looks_one_24)
                    .into(holder.imageView);

        } else if (position == 1) {
            holder.imageView.setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load(R.drawable.ic_baseline_looks_two_24)
                    //.circleCrop()
                    //.apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(mContext, 15, 0, RoundedCornersTransformation.CornerType.ALL)))
                    .error(R.drawable.ic_baseline_looks_two_24)
                    .into(holder.imageView);
        } else if (position == 2){
            holder.imageView.setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load(R.drawable.ic_baseline_looks_3_24)
                    //.circleCrop()
                    //.apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(mContext, 15, 0, RoundedCornersTransformation.CornerType.ALL)))
                    .error(R.drawable.ic_baseline_looks_3_24)
                    .into(holder.imageView);
        } else {
            holder.imageView.setVisibility(View.GONE);
        }

        Glide.with(mContext)
                .load(getUserInfoDataResultModel.get(position).getpImageUrl())
                .circleCrop()
                //.apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(mContext, 15, 0, RoundedCornersTransformation.CornerType.ALL)))
                //.error(R.drawable.gold)
                .into(holder.uimageView);

        String ustr = getUserInfoDataResultModel.get(position).getNickname()+"님의 후원";
        SpannableStringBuilder sb1 = new SpannableStringBuilder(ustr);
        sb1.setSpan(new StyleSpan(Typeface.BOLD), 0,getUserInfoDataResultModel.get(position).getNickname().length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        sb1.setSpan(new AbsoluteSizeSpan(18, true), 0, getUserInfoDataResultModel.get(position).getNickname().length()
                , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // Size
        holder.uTextView.setText(sb1);

        //holder.uTextView.setText(getUserInfoDataResultModel.get(position).getNickname()+"님의 후원");
        holder.uTextView.setSelected(true);
        holder.pTextView.setText(getNumberFormat(Integer.parseInt(getUserInfoDataResultModel.get(position).getRemovemoney())/50)+"회");

    }

    @Override
    public int getItemCount() {
        return getUserInfoDataResultModel.size();
    }

    public static String getNumberFormat (int getData) {

        NumberFormat nf = NumberFormat.getNumberInstance();
        String str = nf.format(getData);

        return str;
    }

}
