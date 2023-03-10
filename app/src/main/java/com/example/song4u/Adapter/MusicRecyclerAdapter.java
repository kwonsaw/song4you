package com.example.song4u.Adapter;

import android.content.Context;
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

public class MusicRecyclerAdapter extends RecyclerView.Adapter<MusicRecyclerAdapter.ViewHolder> {

    Context mContext = null;
    ArrayList<GetMusicListDataResultModel> getMusicListDataResultModel;
    private MusicRecyclerAdapter.OnItemClickListener mListener = null ;

    public MusicRecyclerAdapter(Context context, ArrayList<GetMusicListDataResultModel> list) {
        mContext = context;
        getMusicListDataResultModel = list;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageView simageView;
        TextView tTextView;
        TextView sTextView;
        TextView aTextView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.mImage);
            simageView = (ImageView) itemView.findViewById(R.id.sImage);
            tTextView = (TextView) itemView.findViewById(R.id.tText);
            sTextView = (TextView) itemView.findViewById(R.id.sText);
            aTextView = (TextView) itemView.findViewById(R.id.aText);

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

    public void setOnItemClickListener(MusicRecyclerAdapter.OnItemClickListener listener) {
        this.mListener = listener ;
    }

    // 아이템 뷰를 위한 뷰홀더 객체를 생성하여 리턴
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.recycler_item_music, parent, false);
        MusicRecyclerAdapter.ViewHolder vh = new MusicRecyclerAdapter.ViewHolder(view);
        return vh;
    }

    // position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    @Override
    public void onBindViewHolder(@NonNull MusicRecyclerAdapter.ViewHolder holder, int position) {

        if (position == 0) {

            Glide.with(mContext)
                    .load(R.drawable.ic_baseline_add_24)
                    .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(mContext, 15, 0, RoundedCornersTransformation.CornerType.ALL)))
                    .error(R.drawable.ic_launcher_foreground)
                    .into(holder.imageView);

            holder.imageView.setBackgroundResource(R.drawable.addmusic_bg);

            holder.aTextView.setVisibility(View.VISIBLE);
            holder.tTextView.setVisibility(View.GONE);
            holder.sTextView.setVisibility(View.GONE);
            holder.simageView.setVisibility(View.GONE);

        } else {
            Glide.with(mContext)
                    .load(getMusicListDataResultModel.get(position-1).getMainimgurl())
                    //.circleCrop()
                    .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(mContext, 15, 0, RoundedCornersTransformation.CornerType.ALL)))
                    .error(R.drawable.ic_launcher_foreground)
                    .into(holder.imageView);

            if (getMusicListDataResultModel.get(position-1).getType().equalsIgnoreCase("melon")) {
                Glide.with(mContext)
                        .load(R.drawable.melonlogo)
                        //.circleCrop()
                        .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(mContext, 15, 0, RoundedCornersTransformation.CornerType.ALL)))
                        .error(R.drawable.ic_launcher_foreground)
                        .into(holder.simageView);
            } else {
                Glide.with(mContext)
                        .load(R.drawable.genielogo)
                        //.circleCrop()
                        .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(mContext, 15, 0, RoundedCornersTransformation.CornerType.ALL)))
                        .error(R.drawable.ic_launcher_foreground)
                        .into(holder.simageView);
            }

            holder.imageView.setBackground(null);

            holder.tTextView.setText(getMusicListDataResultModel.get(position-1).getTitle());
            holder.sTextView.setText(getMusicListDataResultModel.get(position-1).getSinger());
            holder.tTextView.setVisibility(View.VISIBLE);
            holder.sTextView.setVisibility(View.VISIBLE);
            holder.simageView.setVisibility(View.VISIBLE);
            holder.aTextView.setVisibility(View.GONE);

        }
    }

    @Override
    public int getItemCount() {
        return getMusicListDataResultModel.size()+1;
    }


}
