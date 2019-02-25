package com.example.elliorsworld.practice.data.repository;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Patterns;

import com.example.elliorsworld.practice.domain.listener.BitmapDownloadListener;
import com.example.elliorsworld.practice.domain.repository.ImageRepository;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ImageRepositoryImpl implements ImageRepository {

    private static final int SCALED_WIDTH = 800;
    private Context context;

    public ImageRepositoryImpl(Context context) {
        this.context = context;
    }

    public void loadUrl(String url,BitmapDownloadListener listenerUrl){
        new LoadFromUrl(listenerUrl,context.getContentResolver()).execute(url);
    }

    private static class LoadFromUrl extends AsyncTask<String, Void, Bitmap> {

        private final ContentResolver resolver;
        private final BitmapDownloadListener listener;

        public LoadFromUrl(BitmapDownloadListener listener,ContentResolver resolver){
            this.listener = listener;
            this.resolver = resolver;
        }

        @Override
        public Bitmap doInBackground(String... params) {
            Bitmap bitmap;
            InputStream inputStream;
            try {
                inputStream = createInputStream(params[0]);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = scaleCalc(options.outWidth,options.outHeight);
                 options.inPreferredConfig = Bitmap.Config.RGB_565;
                bitmap = BitmapFactory.decodeStream(inputStream, null, options);
                inputStream.close();
            } catch (Exception exception) {
                Log.e("Ошибка загрузки", exception.getMessage());
                exception.printStackTrace();
                return null;
            }
            return bitmap;
        }

        private int scaleCalc(int width, int height) {
            int scale;
            if (width > height) {
                scale = Math.round((float) height / (float) SCALED_WIDTH);
            } else {
                scale = Math.round((float) width / (float) SCALED_WIDTH);
            }
            return scale;
        }

        protected void onPostExecute(final Bitmap result) {
            updateUrlHistory(result);
        }

        private InputStream createInputStream(String param) {
            InputStream inputStream = null;
            if (Patterns.WEB_URL.matcher(param).matches()) {
                try {
                    URL url = new URL(param);
                    inputStream = url.openStream();
                } catch (IOException ioException) {
                    Log.e("Fail download", ioException.getMessage());
                    ioException.printStackTrace();
                }
            } else {
                Uri uri = Uri.parse(param);
                try {
                    inputStream = resolver.openInputStream(uri);
                } catch (FileNotFoundException fileNotFound) {
                    Log.e("File do not exist", fileNotFound.getMessage());
                    fileNotFound.printStackTrace();
                } catch (Exception exception) {
                    //ignore
                }
            }
            return inputStream;
        }
        private void updateUrlHistory(Bitmap bitmap) {
            if (listener != null) {
                listener.onImageLoaded(bitmap);
            }

        }
    }

}
