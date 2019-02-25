package com.example.elliorsworld.practice.presentation.presenter;

import android.graphics.Bitmap;

import com.example.elliorsworld.practice.domain.listener.BitmapDownloadListener;
import com.example.elliorsworld.practice.domain.listener.HistoryListener;
import com.example.elliorsworld.practice.domain.interactor.InteractorImpl;
import com.example.elliorsworld.practice.domain.interactor.Interactor;
import com.example.elliorsworld.practice.domain.model.ResultImage;
import com.example.elliorsworld.practice.presentation.MainContract;

import java.util.ArrayList;

public class Presenter implements MainContract.Presenter {

    private final MainContract.View view;
    private final Interactor interactor;


     Presenter(final MainContract.View view, InteractorImpl interactorImpl) {
        this.view = view;
        this.interactor = interactorImpl;
        interactorImpl.listenHistory(new HistoryListener() {
                                     @Override
                                     public void onHistoryUpdate(ArrayList<ResultImage> resultImages) {
                                         view.updateHistory(resultImages);
                                     }
                                 }
        );
    }

    @Override
    public void onRotateButtonClicked(Bitmap bitmap) {
        interactor.rotate(bitmap);
    }

    @Override
    public void onGrayButtonClicked(Bitmap bitmap) {
        interactor.gray(bitmap);
    }
    @Override
    public void onMirrorButtonClicked(Bitmap bitmap) {
        interactor.mirror(bitmap);
    }

    @Override
    public void onImageClicked() {
        view.createDialog();
    }

    @Override
    public void onGalleryClicked() {
        view.setGalleryImage();
    }

    @Override
    public void onCameraClicked() {
        view.setCameraImage();
    }

    @Override
    public void onUrlClicked() {
        view.onUrlClicked();
    }

@Override
    public void onRemoveClicked(ResultImage image) {
        interactor.removeImage(image);
    }
@Override
   public   void onUrlSelected(String url) {
        view.startProgress();
        interactor.loadFromUrl(url, new BitmapDownloadListener() {
            @Override
            public void onImageLoaded(Bitmap bmImage) {
                view.updateUrlHistory(bmImage);
            }
        });
    }
}
