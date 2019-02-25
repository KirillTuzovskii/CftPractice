package com.example.elliorsworld.practice.domain.interactor;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.example.elliorsworld.practice.domain.repository.ImageRepository;
import com.example.elliorsworld.practice.domain.repository.OperationRepository;
import com.example.elliorsworld.practice.domain.listener.BitmapDownloadListener;
import com.example.elliorsworld.practice.domain.listener.HistoryListener;
import com.example.elliorsworld.practice.domain.model.ResultImage;

public class InteractorImpl implements Interactor {

    private static final int DEGREE = 90;
    private static final int SCALED_WIDTH = 800;

    private final OperationRepository operationRepository;
    private final ImageRepository urlRepository;

    public InteractorImpl(OperationRepository operationRepository, ImageRepository loadFromUrl) {
        this.operationRepository = operationRepository;
        this.urlRepository = loadFromUrl;
    }

    @Override
    public void rotate(Bitmap bitmap) {
        Matrix rotateMatrix = new Matrix();
        rotateMatrix.postRotate(DEGREE);

        float koef = (float) SCALED_WIDTH / bitmap.getWidth();
        int newSizeWidth = Math.round(bitmap.getWidth() * koef);
        int newSizeHeight = Math.round(bitmap.getHeight() * koef);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, newSizeWidth, newSizeHeight, true);
        Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), rotateMatrix, true);

        ResultImage image = new ResultImage();
        image.setBitmap(rotatedBitmap);

        operationRepository.addResultImage(image);
    }

    @Override
    public void gray(Bitmap bitmap) {
        if (bitmap == null || bitmap.isRecycled()) {
            return;
        }

        float koef = (float) SCALED_WIDTH / bitmap.getWidth();
        int newSizeWidth = Math.round(bitmap.getWidth() * koef);
        int newSizeHeight = Math.round(bitmap.getHeight() * koef);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, newSizeWidth, newSizeHeight, true);

        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        ColorMatrixColorFilter colorMatrixFilter = new ColorMatrixColorFilter(colorMatrix);

        Bitmap blackAndWhiteBitmap = scaledBitmap.copy(Bitmap.Config.ARGB_8888, true);

        Paint paint = new Paint();
        paint.setColorFilter(colorMatrixFilter);

        Canvas canvas = new Canvas(blackAndWhiteBitmap);
        canvas.drawBitmap(blackAndWhiteBitmap, 0, 0, paint);

        ResultImage image = new ResultImage();
        image.setBitmap(blackAndWhiteBitmap);

        operationRepository.addResultImage(image);
    }

    @Override
    public void mirror(Bitmap bitmap) {
        if (bitmap == null || bitmap.isRecycled()) {
            return;
        }

        float koef = (float) SCALED_WIDTH / bitmap.getWidth();
        int newSizeWidth = Math.round(bitmap.getWidth() * koef);
        int newSizeHeight = Math.round(bitmap.getHeight() * koef);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, newSizeWidth, newSizeHeight, true);

        Matrix matrix = new Matrix();
        matrix.preScale(-1, 1);

        Bitmap mirrorBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, false);
        ResultImage image = new ResultImage();
        image.setBitmap(mirrorBitmap);

        operationRepository.addResultImage(image);
    }

@Override
    public void removeImage(ResultImage image) {
        operationRepository.removeImage(image);
    }

    @Override
    public void listenHistory(HistoryListener listener) {
        operationRepository.listenHistory(listener);
    }

    @Override
    public void loadFromUrl(String url, BitmapDownloadListener listenerUrl) {
        urlRepository.loadUrl(url, listenerUrl);
    }

}
