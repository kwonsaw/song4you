package com.example.song4u.network.streaming;

public interface StreamingSaveServiceResultListener {

    void complete(Boolean isSuccess, String no, StreamingSponsorType sponsorType);
}
