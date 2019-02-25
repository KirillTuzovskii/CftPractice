package com.example.elliorsworld.practice.domain.interactor;

import android.graphics.Bitmap;

import com.example.elliorsworld.practice.domain.listener.BitmapDownloadListener;
import com.example.elliorsworld.practice.domain.listener.HistoryListener;
import com.example.elliorsworld.practice.domain.model.ResultImage;

public interface Interactor {

    void removeImage(ResultImage index);

    void rotate(Bitmap bitmap);

    void gray(Bitmap bitmap);

    void mirror(Bitmap bitmap);

    void listenHistory(HistoryListener listener);

    void loadFromUrl(String url, BitmapDownloadListener listenerUrl);

}
