package com.example.song4u.Adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.buzzvil.buzzad.benefit.presentation.nativead.NativeAd;
import com.buzzvil.buzzad.benefit.presentation.nativead.NativeAdViewBinder;
import com.example.song4u.Util.CarouselItem;
import com.example.song4u.databinding.ViewItemCarouselBinding;


public class CarouselAdapter extends ListAdapter<CarouselItem, CarouselAdapter.ViewHolder> {

    private final boolean isInfiniteLoopEnabled;

    public CarouselAdapter(final boolean isInfiniteLoopEnabled) {
        super(new CarouselDiff());
        this.isInfiniteLoopEnabled = isInfiniteLoopEnabled;
    }


    @Override
    public int getItemCount() {
        final int actualItemCount = super.getItemCount();
        if (isInfiniteLoopEnabled && actualItemCount > 0) {
            // 무한 루프를 쉽게 구현하는 방법으로 매우 큰 수를 여기서 반환합니다.
            return Integer.MAX_VALUE;
        }
        return actualItemCount;
    }

    @Override
    public CarouselItem getItem(final int position) {
        int newPosition;
        if (isInfiniteLoopEnabled) {
            // 무한 루프인 경우의 position은 매우 큰 수이므로 실제 아이템 수로 나눈 나머지를 사용합니다
            newPosition = position % super.getItemCount();
        } else {
            newPosition = position;
        }
        return super.getItem(newPosition);
    }

    @Override
    public ViewHolder onCreateViewHolder(
            ViewGroup parent,
            int viewType
    ) {
        return new ViewHolder(ViewItemCarouselBinding.inflate(LayoutInflater.from(parent.getContext())));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final CarouselItem item = getItem(position);

        if (item instanceof CarouselItem.NativeAdItem) {
            holder.bind(((CarouselItem.NativeAdItem) item).nativeAd);
        }
    }

    @Override
    public void onViewRecycled(final ViewHolder holder) {
        super.onViewRecycled(holder);
        // unbind를 반드시 호출하여 NativeAd나 FeedPromotion을 재사용할 때 문제가 발생하지 않게 합니다.
        holder.unbind();
    }

    private static class CarouselDiff extends DiffUtil.ItemCallback<CarouselItem> {
        @Override
        public boolean areItemsTheSame(final CarouselItem oldItem, final CarouselItem newItem) {
            if (oldItem instanceof CarouselItem.NativeAdItem
                    && newItem instanceof CarouselItem.NativeAdItem
                    && ((CarouselItem.NativeAdItem) oldItem).nativeAd == ((CarouselItem.NativeAdItem) newItem).nativeAd) {
                return true;
            } else if (oldItem instanceof CarouselItem.CarouselToFeedSlideItem
                    && newItem instanceof CarouselItem.CarouselToFeedSlideItem) {
                return true;
            }
            return false;

        }

        @Override
        public boolean areContentsTheSame(final CarouselItem oldItem, final CarouselItem newItem) {
            if (oldItem instanceof CarouselItem.NativeAdItem
                    && newItem instanceof CarouselItem.NativeAdItem
                    && ((CarouselItem.NativeAdItem) oldItem).nativeAd.getId() == ((CarouselItem.NativeAdItem) newItem).nativeAd.getId()) {
                return true;
            } else if (oldItem instanceof CarouselItem.CarouselToFeedSlideItem
                    && newItem instanceof CarouselItem.CarouselToFeedSlideItem) {
                return true;
            }
            return false;
        }
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        // 레이아웃과 NativeAd를 연결해주는 ViewBinder
        private final NativeAdViewBinder nativeAdViewBinder;

        public ViewHolder(final ViewItemCarouselBinding binding) {
            super(binding.getRoot());

            nativeAdViewBinder = new NativeAdViewBinder.Builder(
                    binding.getRoot(),
                    binding.adMediaView
            )
                    //.titleTextView(binding.adTitleText)
                    .descriptionTextView(binding.adDescriptionText)
                    //.iconImageView(binding.adIconImage)
                    .ctaView(binding.adCtaView)
                    .build();

        }

        /**
         * 레이아웃과 NativeAd를 연결합니다.
         *
         * @param nativeAd: 레이아웃에 연결할 NativeAd 객체
         */
        void bind(final NativeAd nativeAd) {
            nativeAdViewBinder.bind(nativeAd);
        }

        /**
         * 레이아웃과 NativeAd 간의 연결을 해제합니다.
         */
        void unbind() {
            this.nativeAdViewBinder.unbind();
        }
    }
}