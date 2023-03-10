package com.example.song4u.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.song4u.R;
import com.example.song4u.network.resultmodel.MobifeedDateResultModel;

import java.util.ArrayList;

public class Mobifeed_Adapter extends RecyclerView.Adapter<Mobifeed_Adapter.ViewHolder> {

    private Context mContext;
    private OnItemClickListener mListener = null ;

    private ArrayList<MobifeedDateResultModel> mCountries;

    public Mobifeed_Adapter(Context context, ArrayList<MobifeedDateResultModel> list) {

        mContext = context;
        mCountries = list;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // context 와 parent.getContext() 는 같다.
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.view_item_news, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MobifeedDateResultModel content = mCountries.get(position);

        holder.textview.setText(content.getNews_title());
        //holder.textview.setTag(position);
        //holder.textview.setOnClickListener(onClickItem);

        if(content.getNews_img_url() != null) {

            //Log.e("kwonsaw",""+content.getNews_img_url());
            Glide.with(mContext)
                    .load(content.getNews_img_url())
                    //.load(R.drawable.timg)
                    .fitCenter()
                    //.apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(mContext, 30, 0, RoundedCornersTransformation.CornerType.ALL)))
                    //.error(R.drawable.com_facebook_profile_picture_blank_portrait)
                    .into(holder.imgView);
            //holder.imgView.setTag(position);
            //holder.imgView.setOnClickListener(onClickItem);

        }

    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position) ;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener ;
    }

    @Override
    public int getItemCount() {

        return mCountries.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textview;
        public ImageView imgView;

        public ViewHolder(View itemView) {
            super(itemView);

            textview = itemView.findViewById(R.id.newsContents);
            imgView = itemView.findViewById(R.id.newsImg);

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


}