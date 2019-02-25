package com.example.elliorsworld.practice.presentation;

import android.graphics.Bitmap;

import com.example.elliorsworld.practice.domain.model.ResultImage;

import java.util.ArrayList;

public interface MainContract {
    interface View {

        void createDialog();

        void setGalleryImage();

        void setCameraImage();

        void updateHistory(ArrayList<ResultImage> resultImages);

        void onUrlClicked();

        void startProgress();

        void updateUrlHistory(Bitmap bitmap);

    }

    interface Presenter {

        void onRotateButtonClicked(Bitmap bitmap);

        void onGrayButtonClicked(Bitmap bitmap);

        void onMirrorButtonClicked(Bitmap bitmap);

        void onImageClicked();

        void onGalleryClicked();

        void onCameraClicked();

        void onUrlClicked();

        void onRemoveClicked(ResultImage image);

        void onUrlSelected(String url);


    }

}
