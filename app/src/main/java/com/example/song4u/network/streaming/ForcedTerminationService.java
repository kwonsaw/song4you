package com.example.song4u.network.streaming;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class ForcedTerminationService extends Service {

    private StreamingSaveUpService streamingSaveUpService = StreamingSaveUpService.build();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {

        if (streamingSaveUpService.isOn()) {
            streamingSaveUpService.forcedStreamingVote();
        }
        stopSelf();
    }
}