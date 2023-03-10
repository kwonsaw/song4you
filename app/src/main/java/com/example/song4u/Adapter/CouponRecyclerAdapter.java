package com.example.song4u.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
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
import com.example.song4u.network.resultmodel.GetCouponDataResultModel;

import java.text.NumberFormat;
import java.util.ArrayList;

public class CouponRecyclerAdapter extends RecyclerView.Adapter<CouponRecyclerAdapter.ViewHolder> {

    Context mContext = null;
    ArrayList<GetCouponDataResultModel> getCouponDataResultModel;
    private CouponRecyclerAdapter.OnItemClickListener mListener = null ;

    public CouponRecyclerAdapter(Context context, ArrayList<GetCouponDataResultModel> list) {
        mContext = context;
        getCouponDataResultModel = list;

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView mTextView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.mImage);
            mTextView = (TextView) itemView.findViewById(R.id.mText);

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

    public void setOnItemClickListener(CouponRecyclerAdapter.OnItemClickListener listener) {
        this.mListener = listener ;
    }

    // 아이템 뷰를 위한 뷰홀더 객체를 생성하여 리턴
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.recycler_item_store, parent, false);
        CouponRecyclerAdapter.ViewHolder vh = new CouponRecyclerAdapter.ViewHolder(view);
        return vh;
    }

    // position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    @Override
    public void onBindViewHolder(@NonNull CouponRecyclerAdapter.ViewHolder holder, int position) {


        Glide.with(mContext)
                .load(getCouponDataResultModel.get(position).getGifticonImageUrl())
                .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(mContext, 15, 0, RoundedCornersTransformation.CornerType.ALL)))
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.imageView);

        int numInt = Integer.parseInt(getCouponDataResultModel.get(position).getPrice());

        String ustr = getCouponDataResultModel.get(position).getBrandname()+"\n"+getCouponDataResultModel.get(position).getProductname()+"\n구매일자: "+getCouponDataResultModel.get(position).getInsertedDatetime();
        SpannableStringBuilder sb1 = new SpannableStringBuilder(ustr);
        //int color = getResources().getColor(R.color.songcolor);
        sb1.setSpan(new ForegroundColorSpan(Color.BLACK), 0, getCouponDataResultModel.get(position).getBrandname().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //sb1.setSpan(new StyleSpan(Typeface.BOLD), getCouponDataResultModel.get(position).getBrandname().length(),getCouponDataResultModel.get(position).getProductname().length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        sb1.setSpan(new AbsoluteSizeSpan(15, true), 0, getCouponDataResultModel.get(position).getBrandname().length()
                , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // Size
        sb1.setSpan(new ForegroundColorSpan(Color.DKGRAY), getCouponDataResultModel.get(position).getBrandname().length(), ustr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.mTextView.setText(sb1);


    }

    @Override
    public int getItemCount() {
        return getCouponDataResultModel.size();
    }

    public static String getNumberFormat (int getData) {

        NumberFormat nf = NumberFormat.getNumberInstance();
        String str = nf.format(getData);

        return str;
    }

}
