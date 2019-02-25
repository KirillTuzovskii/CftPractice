package com.example.elliorsworld.practice.domain.repository;

import com.example.elliorsworld.practice.domain.listener.BitmapDownloadListener;

public interface ImageRepository {
    void loadUrl(String url, BitmapDownloadListener listenerUrl);

}
