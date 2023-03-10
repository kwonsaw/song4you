package com.example.song4u.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.song4u.R;
import com.example.song4u.network.resultmodel.GetUserInfoDataResultModel;

import java.util.ArrayList;

public class PlayuserRecyclerAdapter extends RecyclerView.Adapter<PlayuserRecyclerAdapter.ViewHolder> {

    Context mContext = null;
    ArrayList<GetUserInfoDataResultModel> getUserInfoDataResultModel;
    private PlayuserRecyclerAdapter.OnItemClickListener mListener = null ;

    public PlayuserRecyclerAdapter(Context context, ArrayList<GetUserInfoDataResultModel> list) {
        mContext = context;
        getUserInfoDataResultModel = list;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.mImage);

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

    public void setOnItemClickListener(PlayuserRecyclerAdapter.OnItemClickListener listener) {
        this.mListener = listener ;
    }

    // 아이템 뷰를 위한 뷰홀더 객체를 생성하여 리턴
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.recycler_item_playuser, parent, false);
        PlayuserRecyclerAdapter.ViewHolder vh = new PlayuserRecyclerAdapter.ViewHolder(view);
        return vh;
    }

    // position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    @Override
    public void onBindViewHolder(@NonNull PlayuserRecyclerAdapter.ViewHolder holder, int position) {

            Glide.with(mContext)
                    .load(getUserInfoDataResultModel.get(position).getpImageUrl())
                    .circleCrop()
                    //.apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(mContext, 15, 0, RoundedCornersTransformation.CornerType.ALL)))
                    .error(R.drawable.ic_launcher_foreground)
                    .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return getUserInfoDataResultModel.size();
    }


}
