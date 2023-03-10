package com.example.song4u.network.streaming;

import static android.content.ContentValues.TAG;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.song4u.Application.AppAplication;
import com.example.song4u.R;
import com.example.song4u.Util.CommonUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class StreamingSaveUpService {

    private static StreamingSaveUpService sService;
    private StreamingSaveServiceResultListener mListener;
    private Boolean isOn = false;
    private Boolean isComplete = false;
    private Boolean isSongmusic = false;
    private String mSongId;
    private String mTitle;
    private String mSinger;
    private StreamingSponsorType mSponsorType = StreamingSponsorType.NONE;
    private String mNo;
    private BugsTimer bugsTimer;
    private Boolean isBugsPlaying = false;
    private Boolean isGeniePlaying = false;
    private Boolean isMelonPlaying = false;
    private GenieTimer genieTimer;
    private MelonTimer melonTimer;

    private static Long mPlayTime;

    private StreamingSaveUpService() {

    }

    public static StreamingSaveUpService build() {
        if (sService == null) {
            sService = new StreamingSaveUpService();
        }
        return sService;
    }

    public StreamingSaveUpService resultListener(StreamingSaveServiceResultListener listener) {
        mListener = listener;

        return sService;
    }

    public StreamingSaveUpService setSongId(String songId) {
        mSongId = songId;
        return sService;
    }

    public StreamingSaveUpService setTitle(String title) {
        mTitle = title;
        return sService;
    }

    public StreamingSaveUpService setSinger(String singer) {
        mSinger = singer;
        return sService;
    }

    public StreamingSaveUpService setSponsorType(StreamingSponsorType sponsorType) {
        mSponsorType = sponsorType;
        return sService;
    }


    public StreamingSaveUpService setNo(String no) {
        mNo = no;
        return sService;
    }

    public StreamingSaveUpService setPlayTime(int playTime) {
        mPlayTime = Long.valueOf(playTime * 1000);
        isSongmusic = true;
        return sService;
    }

    public boolean isOn() {
        return isOn;
    }
    private void setOn() {
        isOn = true;
    }

    public void setComplete(Boolean complete) {
        isComplete = complete;
    }

    private void setOff() {
        isOn = false;

        stopBugsTimer();
        stopGenieTimer();
        stopMelonTimer();
    }

    public void stopBugsTimer() {
        if (bugsTimer != null) {
            bugsTimer.cancel();
            bugsTimer = null;
        }
    }

    public void stopGenieTimer() {
        if (genieTimer != null) {
            genieTimer.cancel();
            genieTimer = null;
        }
    }

    public void stopMelonTimer() {
        if (melonTimer != null) {
            melonTimer.cancel();
            melonTimer = null;
        }
    }

    public StreamingSaveUpService register() {
        AudioManager manager = (AudioManager) AppAplication.context.getSystemService(Context.AUDIO_SERVICE);
        if(manager.isMusicActive())
        {
            // Something is being played.
        }

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.android.music.metachanged");
        intentFilter.addAction("com.android.music.playstatechanged");
        intentFilter.addAction("com.android.music.playbackcomplete");
        intentFilter.addAction("com.android.music.queuechanged");

        // various
        intentFilter.addAction("com.miui.player.metachanged");
        intentFilter.addAction("com.htc.music.metachanged");
        intentFilter.addAction("com.nullsoft.winamp.metachanged");
        intentFilter.addAction("com.real.IMP.metachanged");
        intentFilter.addAction("fm.last.android.metachanged");
        intentFilter.addAction("com.sec.android.app.music.metachanged");
        intentFilter.addAction("com.amazon.mp3.metachanged");
        intentFilter.addAction("com.real.IMP.metachanged");
        intentFilter.addAction("com.rdio.android.metachanged");
        intentFilter.addAction("com.andrew.apollo.metachanged");
        intentFilter.addAction("com.lge.music.metachanged");
        intentFilter.addAction("com.pantech.app.music.metachanged");

        // 벅스뮤직 테스트 ok
        intentFilter.addAction("com.neowiz.android.bugs.metachanged");
        intentFilter.addAction("com.neowiz.android.bugs.playstatechanged");

        intentFilter.addAction("com.soundcloud.android.metachanged");
        intentFilter.addAction("com.soundcloud.android.playback.playcurrent");
        intentFilter.addAction("com.nullsoft.winamp.metachanged");
        intentFilter.addAction("com.amazon.mp3.metachanged");
        intentFilter.addAction("com.miui.player.metachanged");
        intentFilter.addAction("com.real.IMP.metachanged");
        intentFilter.addAction("com.samsung.sec.android.MusicPlayer.metachanged");
        intentFilter.addAction("com.andrew.apollo.metachanged");
        intentFilter.addAction("com.htc.music.metachanged");
        intentFilter.addAction("com.spotify.music.metadatachanged");

        //네이버 뮤직 테스트
        intentFilter.addAction("com.nhn.android.music");


        // 멜론 테스트 (곡 재생 완료 여부값만 나옴, 안드로이드 버전별로 동작이 다름)
        intentFilter.addAction("com.iloen.melon.intent.action.playback.statechanged");
        intentFilter.addAction("com.iloen.melon.intent.action.playback.metaupated");

        // 지니뮤직테스트 이걸로 안옴..
        intentFilter.addAction("com.ktmusic.geniemusic.metachanged");
        intentFilter.addAction("com.ktmusic.geniemusic.playstatechanged");


        // 플로 테스트
        intentFilter.addAction("android.media.metadata");
        intentFilter.addAction("com.skplanet.musicmate.playstatechanged");

        // sony walkman
        intentFilter.addAction("com.sonyericsson.music.metachanged");
        intentFilter.addAction("com.sonyericsson.music.playbackcontrol.ACTION_PLAYBACK_PLAY");
        intentFilter.addAction("com.sonyericsson.music.TRACK_COMPLETED");
        intentFilter.addAction("com.sonyericsson.music.playbackcomplete");
        intentFilter.addAction("com.sonyericsson.music.playstatechanged");
        intentFilter.addAction("com.sonyericsson.music.playbackcontrol.ACTION_TRACK_STARTED");
        intentFilter.addAction("com.sonyericsson.music.playbackcontrol.ACTION_PAUSED");

        // xiaomi
        intentFilter.addAction("soundbar.music.metachanged");


        AppAplication.context.registerReceiver(mReceiver, intentFilter);

        return sService;
    }


    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (isComplete) {
                return;
            }
            dumpIntent(intent);
            switch (mSponsorType) {
                case BUGS:
                    //bugsCheckSponsor(intent);
                    break;
                case GENIE:
                    genieCheckSponsor(intent);
                    break;
                case MELON:
                    melonCheckSponsor(intent);
                    break;
                case NONE:
                    break;
            }

        }

        private void dumpIntent(Intent i){

            Bundle bundle = i.getExtras();
            if (bundle != null) {
                Set<String> keys = bundle.keySet();
                Iterator<String> it = keys.iterator();
                //Log.d(TAG,"BroadcastReceiver - Dumping Intent start");
                while (it.hasNext()) {
                    String key = it.next();
                    Log.d(TAG,"BroadcastReceiver - [" + key + "=" + bundle.get(key)+"]");
                }
                Log.d(TAG,"BroadcastReceiver - Dumping Intent end");
            }
        }
    };
/*
    private void bugsCheckSponsor(Intent intent) {
        boolean isPlaying = intent.getBooleanExtra("playing", false);
        Long trackId = intent.getLongExtra("trackid", 0);

        if (isPlaying) {
            if (trackId.equals(Long.valueOf(mSongId))) {
                stopBugsTimer();
                bugsTimer = new BugsTimer(mPlayTime, 1000);
                bugsTimer.start();
                setOn();
                isBugsPlaying = true;
                notifyStreaming(AppAplication.context.getString(R.string.app_name), AppAplication.context.getString(R.string.streaming_save_up_fail_notify_starting));
            } else {
                stopBugsTimer();
            }
        } else {
            if (isBugsPlaying) { // 곡 변경
                cancelStreamingVote();
            }
            stopBugsTimer();
        }
    }

 */

    private void genieCheckSponsor(Intent intent) {
        String action = intent.getAction();
        Log.d(TAG,  "BroadcastReceiver - genie action : " + action);
        final String GENIE_META_CHANGED = "com.android.music.metachanged";
        boolean isPlaying = intent.getBooleanExtra("playing", false);
        String artist = intent.getStringExtra("artist");
        String track = intent.getStringExtra("track");


        if (artist == null || track == null || mSinger == null || mTitle == null) {
            Map<String, String> params = new HashMap<>();
            params.put("GINIE_ARTIST", artist);
            params.put("GINIE_TRACK", track);
            params.put("SINGER", mSinger);
            params.put("TITLE", mTitle);
            //FADFirebaseUtil.sendEvent("GINIE_CHECK_SPONSOR", params);
            return;
        }

        String artistTrim = CommonUtil.StringReplace(artist);
        String trackTrim = CommonUtil.StringReplace(track);
        String singerTrim = CommonUtil.StringReplace(mSinger);
        String titleTrim = CommonUtil.StringReplace(mTitle);

        if (isPlaying) {

                if (!isSongmusic) {

                    mPlayTime = intent.getLongExtra("duration", 0);
                    if (mPlayTime==60000) {
                        unavailableStreamingVote();
                        return;
                    }

                    mPlayTime = mPlayTime - 1000;
                }

                if ((artistTrim.toUpperCase().contains(singerTrim.toUpperCase()) && trackTrim.toUpperCase().contains(titleTrim.toUpperCase()))
                        || (singerTrim.toUpperCase().contains(artistTrim.toUpperCase()) && titleTrim.toUpperCase().contains(trackTrim.toUpperCase()))
                        || (artistTrim.toUpperCase().contains(singerTrim.toUpperCase()) && titleTrim.toUpperCase().contains(trackTrim.toUpperCase()))
                        || (singerTrim.toUpperCase().contains(artistTrim.toUpperCase()) && trackTrim.toUpperCase().contains(titleTrim.toUpperCase()))) {
                    stopGenieTimer();
                    genieTimer = new GenieTimer(mPlayTime, 1000);
                    genieTimer.start();
                    setOn();
                    isGeniePlaying = true;
                    notifyStreaming(AppAplication.context.getString(R.string.app_name), AppAplication.context.getString(R.string.streaming_save_up_fail_notify_starting));
                } else {
                    stopGenieTimer();
                }

        } else {

            //지니에서 1분 미리듣기 실패 후 멜론(이용권)에서 재생했을 때 지니가 종료되면서 브로드캐스트 호출되며 적립실패로 뜨는 경우
            if ((artistTrim.toUpperCase().contains(singerTrim.toUpperCase()) && trackTrim.toUpperCase().contains(titleTrim.toUpperCase()))
                    || (singerTrim.toUpperCase().contains(artistTrim.toUpperCase()) && titleTrim.toUpperCase().contains(trackTrim.toUpperCase()))
                    || (artistTrim.toUpperCase().contains(singerTrim.toUpperCase()) && titleTrim.toUpperCase().contains(trackTrim.toUpperCase()))
                    || (singerTrim.toUpperCase().contains(artistTrim.toUpperCase()) && trackTrim.toUpperCase().contains(titleTrim.toUpperCase()))) {

                if (isGeniePlaying && action.equals(GENIE_META_CHANGED)) {
                    cancelStreamingVote();
                }
                    stopGenieTimer();
            }
        }
    }

    private void melonCheckSponsor(Intent intent) {
        String action = intent.getAction();
        final String MELON_META_CHANGED = "com.android.music.metachanged";
        final String MELON_PLAY_CHANGED = "com.android.music.playstatechanged";
        boolean isPlaying = intent.getBooleanExtra("playing", false);
        String artist = intent.getStringExtra("artist");
        String track = intent.getStringExtra("track");

        Log.e("kwonsaw","mPlayTime:"+mPlayTime);
        Log.d(TAG,  "BroadcastReceiver - melon : " + action);

        if (artist == null || track == null || mSinger == null || mTitle == null) {
            Map<String, String> params = new HashMap<>();
            params.put("MELON_ARTIST", artist);
            params.put("MELON_TRACK", track);
            params.put("SINGER", mSinger);
            params.put("TITLE", mTitle);
            //FADFirebaseUtil.sendEvent("MELON_CHECK_SPONSOR", params);
            return;
        }

        String artistTrim = CommonUtil.StringReplace(artist);
        String trackTrim = CommonUtil.StringReplace(track);
        String singerTrim = CommonUtil.StringReplace(mSinger);
        String titleTrim = CommonUtil.StringReplace(mTitle);
        //mPlayTime = Long.valueOf(ptime);

        if (isPlaying) {
            if (!isSongmusic) {
                mPlayTime = intent.getLongExtra("duration", 0);
                if (mPlayTime==60000) {
                    unavailableStreamingVote();
                    return;
                }
                mPlayTime = mPlayTime - 1000;
            }


            if ((artistTrim.toUpperCase().contains(singerTrim.toUpperCase()) && trackTrim.toUpperCase().contains(titleTrim.toUpperCase()))
                    || (singerTrim.toUpperCase().contains(artistTrim.toUpperCase()) && titleTrim.toUpperCase().contains(trackTrim.toUpperCase()))
                    || (artistTrim.toUpperCase().contains(singerTrim.toUpperCase()) && titleTrim.toUpperCase().contains(trackTrim.toUpperCase()))
                    || (singerTrim.toUpperCase().contains(artistTrim.toUpperCase()) && trackTrim.toUpperCase().contains(titleTrim.toUpperCase()))) {

                stopMelonTimer();
                melonTimer = new MelonTimer(mPlayTime, 1000);
                melonTimer.start();
                setOn();
                isMelonPlaying = true;
                notifyStreaming(AppAplication.context.getString(R.string.app_name), AppAplication.context.getString(R.string.streaming_save_up_fail_notify_starting));

            } else {
                stopMelonTimer();
            }
        } else {

            if ((artistTrim.toUpperCase().contains(singerTrim.toUpperCase()) && trackTrim.toUpperCase().contains(titleTrim.toUpperCase())) ||
                    (singerTrim.toUpperCase().contains(artistTrim.toUpperCase()) && titleTrim.toUpperCase().contains(trackTrim.toUpperCase()))
                    || (artistTrim.toUpperCase().contains(singerTrim.toUpperCase()) && titleTrim.toUpperCase().contains(trackTrim.toUpperCase())) || (singerTrim.toUpperCase().contains(artistTrim.toUpperCase()) && trackTrim.toUpperCase().contains(titleTrim.toUpperCase()))) {

                if (isMelonPlaying && (action.equals(MELON_META_CHANGED) || action.equals(MELON_PLAY_CHANGED))) {
                    Log.e("kwonsaw", "isGeniePlaying: " + isGeniePlaying);
                    cancelStreamingVote();
                }
                stopMelonTimer();
            }

        }
    }

    public void forcedStreamingVote() {
        stopStreamingVote();
        fail();
        notifyStreaming(AppAplication.context.getString(R.string.app_name), AppAplication.context.getString(R.string.streaming_save_up_fail_app_forced_notify_message));
    }
    private void cancelStreamingVote() {
        stopStreamingVote();
        fail();
        notifyStreaming(AppAplication.context.getString(R.string.app_name), AppAplication.context.getString(R.string.streaming_save_up_fail_notify_message));
    }
    private void unavailableStreamingVote() {
        stopStreamingVote();
        fail();
        notifyStreaming(AppAplication.context.getString(R.string.app_name), AppAplication.context.getString(R.string.streaming_save_up_fail_notify_unavailable));
    }

    private void stopStreamingVote() {
        isBugsPlaying = false;
        isGeniePlaying = false;
        isMelonPlaying = false;
        isComplete = true;
        isSongmusic = false;
        setOff();
    }

    private void fail() {
        mListener.complete(false, mNo, mSponsorType);
    }

    private void success() {
        stopStreamingVote();
        mListener.complete(true, mNo, mSponsorType);
    }

    public void notifyStreaming(String title, String message) {

        NotificationManager notificationManager = (NotificationManager) AppAplication.context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            String channelID="channel_01"; //알림채널 식별자
            String channelName="송포유"; //알림채널의 이름(별명)

            //알림채널 객체 만들기
            NotificationChannel channel= new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("스트리밍 리워드 진행시 상태에 대한 알림을 표시합니다.");
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

            //알림매니저에게 채널 객체의 생성을 요청
            notificationManager.createNotificationChannel(channel);

            //알림건축가 객체 생성
            builder=new NotificationCompat.Builder(AppAplication.context, channelID);


        }else{
            //알림 건축가 객체 생성
            builder= new NotificationCompat.Builder(AppAplication.context, "");
        }

        builder.setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                //.setLargeIcon(BitmapFactory.decodeResource(AppAplication.context.getResources(),R.drawable.ic_launcher_background))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setTicker(title)
                .setVibrate(new long[0])
                .setAutoCancel(true);
        notificationManager.notify(1, builder.build());

    }
    private class BugsTimer extends CountDownTimer {
        public BugsTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            int value = (int)millisUntilFinished/1000;
            Log.d(TAG, "BroadcastReceiver - Bugs Tick : " + value);
        }

        @Override
        public void onFinish() {
            success();
        }
    }


    private class GenieTimer extends CountDownTimer {
        public GenieTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            int value = (int)millisUntilFinished/1000;
            //Log.d(TAG, "BroadcastReceiver - Genie Tick : " + value);
        }

        @Override
        public void onFinish() {
            success();
        }
    }


    private class MelonTimer extends CountDownTimer {
        public MelonTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            int value = (int)millisUntilFinished/1000;
            Log.d(TAG, "BroadcastReceiver - Melon Tick : " + value);
        }

        @Override
        public void onFinish() {
            success();
        }
    }

}
