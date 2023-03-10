package com.example.song4u.network;

import com.example.song4u.network.resultmodel.GetAddMusicCheckResultModel;
import com.example.song4u.network.resultmodel.GetAppVersionResultModel;
import com.example.song4u.network.resultmodel.GetBannerResultModel;
import com.example.song4u.network.resultmodel.GetCheckMusicResultModel;
import com.example.song4u.network.resultmodel.GetCouponResultModel;
import com.example.song4u.network.resultmodel.GetFaqResultModel;
import com.example.song4u.network.resultmodel.GetGenieParserResultModel;
import com.example.song4u.network.resultmodel.GetGenieSearchResultModel;
import com.example.song4u.network.resultmodel.GetGifticonResultModel;
import com.example.song4u.network.resultmodel.GetMelonParserResultModel;
import com.example.song4u.network.resultmodel.GetMelonSearchResultModel;
import com.example.song4u.network.resultmodel.GetMusicInfoResultModel;
import com.example.song4u.network.resultmodel.GetMusicListResultModel;
import com.example.song4u.network.resultmodel.GetMusicRankResultModel;
import com.example.song4u.network.resultmodel.GetMusicResultModel;
import com.example.song4u.network.resultmodel.GetNoticeResultModel;
import com.example.song4u.network.resultmodel.GetPointHistoryResultModel;
import com.example.song4u.network.resultmodel.GetPopupNoticeResultModel;
import com.example.song4u.network.resultmodel.GetPrerollResultModel;
import com.example.song4u.network.resultmodel.GetReplyResultModel;
import com.example.song4u.network.resultmodel.GetSupportMusicResultModel;
import com.example.song4u.network.resultmodel.GetUserInfoResultModel;
import com.example.song4u.network.resultmodel.MobifeedResultModel;
import com.example.song4u.network.resultmodel.PointNewsResultModel;
import com.example.song4u.network.resultmodel.SetAddMusicResultModel;
import com.example.song4u.network.resultmodel.SetBannerResultModel;
import com.example.song4u.network.resultmodel.SetDeleteUserResultModel;
import com.example.song4u.network.resultmodel.SetGifticonResultModel;
import com.example.song4u.network.resultmodel.SetMusicLikeResultModel;
import com.example.song4u.network.resultmodel.SetMusicPointResultModel;
import com.example.song4u.network.resultmodel.SetPointNewsResultModel;
import com.example.song4u.network.resultmodel.SetPrerollResultModel;
import com.example.song4u.network.resultmodel.SetRecommendResultModel;
import com.example.song4u.network.resultmodel.SetReplyMusicResultModel;
import com.example.song4u.network.resultmodel.SetReplyReportResultModel;
import com.example.song4u.network.resultmodel.SetSupportMusicResultModel;
import com.example.song4u.network.resultmodel.SetUserLoginResultModel;
import com.example.song4u.network.resultmodel.SetUserNicknameResultModel;
import com.example.song4u.network.resultmodel.SetUserProfileResultModel;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;

public interface NetworkService {

    @FormUrlEncoded
    @POST("?")
    Call<GetUserInfoResultModel> getUserInfo(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("?")
    Call<SetUserLoginResultModel> setUserLogin(@FieldMap HashMap<String, String> params);

    @GET("?")
    Call<PointNewsResultModel> getPointNews(@QueryMap Map<String, String> params);

    @Headers({"Content-Type: Application/Json;charset=UTF-8"})
    @POST("/api/v2/channel/oneline/appletree")
    Call<MobifeedResultModel> getMobFeedLineNews(@Body String body);

    @GET("?")
    Call<GetAppVersionResultModel> getAppVersion(@QueryMap Map<String, String> params);

    @GET("?")
    Call<GetPopupNoticeResultModel> getPopupNotice(@QueryMap Map<String, String> params);

    @GET("?")
    Call<GetPrerollResultModel> getPreroll(@QueryMap Map<String, String> params);

    @FormUrlEncoded
    @POST("?")
    Call<SetPrerollResultModel> setPreroll(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("?")
    Call<GetBannerResultModel> getBanner(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("?")
    Call<SetBannerResultModel> setBanner(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("?")
    Call<SetPointNewsResultModel> setPointNews(@FieldMap HashMap<String, String> params);

    @GET("?")
    Call<GetNoticeResultModel> getNotice(@QueryMap Map<String, String> params);

    @GET("?")
    Call<GetFaqResultModel> getFaq(@QueryMap Map<String, String> params);

    @Multipart
    @POST("project/SFU/setUserProfile")
    Call<SetUserProfileResultModel> setUserProfile(@Part("userId") RequestBody userid,
                                                          @Part MultipartBody.Part image);

    @FormUrlEncoded
    @POST("?")
    Call<SetUserNicknameResultModel> setUserNickname(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("?")
    Call<GetPointHistoryResultModel> getPointHistory(@FieldMap HashMap<String, String> params);

    //@GET("?")
    //Call<GetMusicResultModel> getMusic(@QueryMap Map<String, String> params);

    @FormUrlEncoded
    @POST("?")
    Call<GetMusicResultModel> getMusic(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("?")
    Call<SetMusicPointResultModel> setMusicPoint(@FieldMap HashMap<String, String> params);

    @GET("?")
    Call<GetMelonParserResultModel> getMelonParser(@QueryMap Map<String, String> params);

    @GET("?")
    Call<GetMelonSearchResultModel> getMelonSearch(@QueryMap Map<String, String> params);

    @GET("?")
    Call<GetGenieParserResultModel> getGenieParser(@QueryMap Map<String, String> params);

    @GET("?")
    Call<GetGenieSearchResultModel> getGenieSearch(@QueryMap Map<String, String> params);

    @FormUrlEncoded
    @POST("?")
    Call<SetAddMusicResultModel> setAddMusic(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("?")
    Call<GetMusicListResultModel> getMusicList(@FieldMap HashMap<String, String> params);

    @GET("?")
    Call<GetMusicInfoResultModel> getMusicInfo(@QueryMap Map<String, String> params);

    @FormUrlEncoded
    @POST("?")
    Call<SetMusicLikeResultModel> setMusicLike(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("?")
    Call<GetCheckMusicResultModel> getCheckMusic(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("?")
    Call<SetSupportMusicResultModel> setSupportMusic(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("?")
    Call<SetReplyMusicResultModel> setReplyMusic(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("?")
    Call<GetReplyResultModel> getReply(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("?")
    Call<SetReplyReportResultModel> setReplyReport(@FieldMap HashMap<String, String> params);

    @GET("?")
    Call<GetMusicRankResultModel> getMusicRank(@QueryMap Map<String, String> params);

    @FormUrlEncoded
    @POST("?")
    Call<SetDeleteUserResultModel> setDeleteUser(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("?")
    Call<GetSupportMusicResultModel> getSupportMusic(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("?")
    Call<SetRecommendResultModel> setRecommend(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("?")
    Call<GetAddMusicCheckResultModel> getAddMusicCheck(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("?")
    Call<GetGifticonResultModel> getGifticon(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("?")
    Call<SetGifticonResultModel> setGifticon(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("?")
    Call<GetCouponResultModel> getCoupon(@FieldMap HashMap<String, String> params);
}
