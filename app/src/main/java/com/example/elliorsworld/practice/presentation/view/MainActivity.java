package com.example.elliorsworld.practice.presentation.view;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.elliorsworld.practice.R;
import com.example.elliorsworld.practice.domain.model.ResultImage;
import com.example.elliorsworld.practice.presentation.MainContract;
import com.example.elliorsworld.practice.presentation.presenter.PresenterFactory;
import com.example.elliorsworld.practice.presentation.adapter.Adapter;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MainContract.View, Adapter.OnImageListener {

    private static final int GALLERY_REQUEST = 1;
    private static final int CAMERA_REQUEST = 0;
    private final int IDD_LIST_SOURCE = 1;
    private final int IDD__RECYCLE_SOURCE = 2;
    private static final String DATA = "data";

    private ImageView imageView;
    private MainContract.Presenter presenter;
    private Adapter adapter;
    private ProgressDialog bar;
    private ResultImage resultImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.image_view);

        Button btnRotate = findViewById(R.id.btn_rotate);
        Button btnGray = findViewById(R.id.btn_gray);
        Button btnMirror = findViewById(R.id.btn_mirror);

        PresenterFactory presenterFactory = new PresenterFactory();
        presenter = presenterFactory.createPresenter(this, this);
        initRecyclerView();

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.image_view:
                        presenter.onImageClicked();
                        break;
                    case R.id.btn_rotate:
                        presenter.onRotateButtonClicked(getBitmap());
                        break;
                    case R.id.btn_gray:
                        presenter.onGrayButtonClicked(getBitmap());
                        break;
                    case R.id.btn_mirror:
                        presenter.onMirrorButtonClicked(getBitmap());
                        break;
                }
            }
        };
        imageView.setOnClickListener(listener);
        btnRotate.setOnClickListener(listener);
        btnGray.setOnClickListener(listener);
        btnMirror.setOnClickListener(listener);
    }
    private void initRecyclerView() {
        RecyclerView resultView = findViewById(R.id.result_view);
        resultView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(this);
        resultView.setAdapter(adapter);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        switch (requestCode) {
            case GALLERY_REQUEST:
                if (resultCode == RESULT_OK) {
                    Bitmap bitmap = null;
                    Uri selectedImage = imageReturnedIntent.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    imageView.setImageBitmap(bitmap);
                }
                break;

            case CAMERA_REQUEST:
                if (resultCode == RESULT_OK) {
                    Bitmap bitmap = null;
                    try {
                        bitmap = imageReturnedIntent.getParcelableExtra(DATA);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    imageView.setImageBitmap(bitmap);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        AlertDialog.Builder builder = null;

        switch (id) {
            case IDD_LIST_SOURCE:
                final String[] sourceName = {getString(R.string.gallery), getString(R.string.take_photo),
                        getString(R.string.url)};
                String title = getString(R.string.dialog_title);
                builder = new AlertDialog.Builder(this);
                builder.setTitle(title);
                builder.setItems(sourceName, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (sourceName[which]) {
                            case "Галерея":
                                presenter.onGalleryClicked();
                                break;
                            case "Камера":
                                presenter.onCameraClicked();
                                break;
                            case "URL":
                                presenter.onUrlClicked();
                                break;
                        }
                    }
                });
                builder.setCancelable(true);
                break;
            case IDD__RECYCLE_SOURCE:
                final String[] sourceRecycleName = {getString(R.string.make_source), getString(R.string.delete)};
                String recycleTitle = getString(R.string.action_title); // в ресурсы
                builder = new AlertDialog.Builder(this);
                builder.setTitle(recycleTitle);
                builder.setItems(sourceRecycleName, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (sourceRecycleName[which].equals("Сделать источником")) {
                            setRecycleViewImage(resultImage);
                        } else if (sourceRecycleName[which].equals("Удалить")) {
                            presenter.onRemoveClicked(resultImage);
                        }
                    }
                });
        }
        return builder.create();
    }

    private void setRecycleViewImage(ResultImage resultImage) {
        imageView.setImageBitmap(resultImage.getBitmap());
    }

    @Override
    public void createDialog() {
        Dialog alert = onCreateDialog(IDD_LIST_SOURCE);
        alert.show();
    }

    @Override
    public void setGalleryImage() {
        Intent photoPickIntent = new Intent(Intent.ACTION_PICK);
        photoPickIntent.setType("image/*");
        startActivityForResult(photoPickIntent, GALLERY_REQUEST);
    }

    @Override
    public void setCameraImage() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    @Override
    public void updateHistory(ArrayList<ResultImage> resultImages) {
        adapter.setItems(resultImages);
    }

    private Bitmap getBitmap() {
        return ((BitmapDrawable) imageView.getDrawable()).getBitmap();
    }

    @Override
    public void onImageClick(ResultImage image) {
        resultImage = image;
        Dialog alert = onCreateDialog(IDD__RECYCLE_SOURCE);
        alert.show();

    }

    @Override
    public void onUrlClicked() {
        android.support.v7.app.AlertDialog.Builder builderInputURLOfImage = new android.support.v7.app.AlertDialog.Builder(this);
        View dialogOfInputURL = LayoutInflater.from(this)
                .inflate(R.layout.dialog_for_url, null);
        builderInputURLOfImage.setView(dialogOfInputURL);
        final EditText userInput = dialogOfInputURL.findViewById(R.id.input_url);
        builderInputURLOfImage.setTitle(this.getString(R.string.download_from_URL));
        builderInputURLOfImage.setCancelable(false).setPositiveButton(this.getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String url = userInput.getText().toString();
                        if (android.util.Patterns.WEB_URL.matcher(url).matches()) {
                            presenter.onUrlSelected(url);
                        } else {
                            Toast toast = Toast.makeText(MainActivity.this,getString(R.string.wrong_URL),
                                    Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                })
                .setNegativeButton(this.getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        android.support.v7.app.AlertDialog inputURLOfImage = builderInputURLOfImage.create();
        inputURLOfImage.show();
    }

    public void startProgress() {
        bar = new ProgressDialog(this);
        bar.setMessage(this.getString(R.string.processing_download));
        bar.setIndeterminate(false);
        bar.show();
    }

    @Override
    public void updateUrlHistory(Bitmap bitmap) {
        if (bitmap == null) {
            Toast toast = Toast.makeText(this, this.getString(R.string.error_set_image),
                    Toast.LENGTH_SHORT);
            toast.show();
        } else {
            imageView.setImageBitmap(bitmap);
            bar.dismiss();
        }
    }
}
