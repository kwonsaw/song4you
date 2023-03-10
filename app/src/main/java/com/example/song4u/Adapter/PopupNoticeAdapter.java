package com.example.song4u.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.song4u.R;
import com.example.song4u.Util.RoundedCornersTransformation;
import com.example.song4u.network.resultmodel.GetPopupNoticeDataResultModel;

import java.util.ArrayList;


public class PopupNoticeAdapter  extends PagerAdapter {

    private int mCurrentPosition = -1;
    private final Context mContext;
    private ArrayList<GetPopupNoticeDataResultModel> mCountries;
    private PopupNoticeAdapter.OnItemClickListener mListener = null ;

    public PopupNoticeAdapter(Context context, ArrayList<GetPopupNoticeDataResultModel> list) {
        mCountries = list;
        mContext = context;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position) ;
    }

    public void setOnItemClickListener(PopupNoticeAdapter.OnItemClickListener listener) {
        this.mListener = listener ;
    }

    @Override
    public int getCount() {
        return mCountries.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        GetPopupNoticeDataResultModel content = mCountries.get(position);
        View view = null;

        if (mContext != null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.popupnotice_lay, container, false);

            ImageView img = (ImageView) view.findViewById(R.id.noticeimg);

            Glide.with(mContext)
                    .load(content.getNoticeImgUrl())
                    .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(mContext,50,0, RoundedCornersTransformation.CornerType.TOP)))
                    //.error(R.drawable.com_facebook_profile_picture_blank_portrait)
                    .into(img);

            img.setClipToOutline(true);

            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        if (mListener != null) {
                            mListener.onItemClick(v, position) ;
                        }

                }
            });

        }

        container.addView(view, ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.WRAP_CONTENT);
        //container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // 뷰페이저에서 삭제.
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (View) object);
    }

}
