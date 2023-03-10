package com.example.song4u.Util;

import com.buzzvil.buzzad.benefit.presentation.feed.entrypoint.FeedPromotion;
import com.buzzvil.buzzad.benefit.presentation.nativead.NativeAd;

public class CarouselItem {

    public static class NativeAdItem extends CarouselItem {
        public final NativeAd nativeAd;

        public NativeAdItem(final NativeAd nativeAd) {

            this.nativeAd = nativeAd;
        }

    }

    public static class CarouselToFeedSlideItem extends CarouselItem {
        public final FeedPromotion feedPromotion;

        public CarouselToFeedSlideItem(final FeedPromotion feedPromotion) {
            this.feedPromotion = feedPromotion;
        }
    }

}
