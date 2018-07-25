package com.avukelic.finddominantcolor.view;

import android.Manifest;
import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.avukelic.finddominantcolor.R;
import com.avukelic.finddominantcolor.model.Image;
import com.avukelic.finddominantcolor.viewmodel.PhotoViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.fotoapparat.Fotoapparat;
import io.fotoapparat.parameter.ScaleType;
import io.fotoapparat.result.PhotoResult;
import io.fotoapparat.selector.FlashSelectorsKt;
import io.fotoapparat.selector.FocusModeSelectorsKt;
import io.fotoapparat.selector.LensPositionSelectorsKt;
import io.fotoapparat.selector.ResolutionSelectorsKt;
import io.fotoapparat.selector.SelectorsKt;
import io.fotoapparat.view.CameraView;

public class CameraActivity extends AppCompatActivity {

    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;

    @BindView(R.id.color_name_camera)
    TextView colorName;
    @BindView(R.id.color_hexa_camera)
    TextView colorHexa;
    @BindView(R.id.color_rgb_camera)
    TextView colorRGB;
    @BindView(R.id.camera_image_camera)
    CameraView camera;
    Fotoapparat fotoapparat;

    PhotoViewModel viewModel;

    Image img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.take_picture);
        ButterKnife.bind(this);
        initializeUi();
    }

    private void initializeUi() {
        img = new Image();
        configureCamera();
        viewModel = ViewModelProviders.of(this).get(PhotoViewModel.class);
        viewModel.setImage(img);
        updateDisplay();
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkForCameraPermission();
    }

    @Override
    protected void onStop() {
        super.onStop();
        fotoapparat.stop();
    }

    //Take a picture and update it in ViewModel
    @OnClick(R.id.btn_take_picture)
    public void takePicture() {
        PhotoResult photoResult = fotoapparat.takePicture();
        photoResult.toBitmap().whenAvailable(bitmapPhoto -> {
            img.setBitmap(bitmapPhoto.bitmap);
            viewModel.updateImage(img);
            updateDisplay();
            return null;
        });
    }


    //Update TextView fields
    private void updateDisplay() {
        viewModel.getImage().observe(this, image -> {
            colorName.setText(image.getColor());
            colorHexa.setText(image.getHexadecimal());
            colorRGB.setText(image.getRGB());
        });
    }

    //Configure camera
    private void configureCamera() {
        fotoapparat = Fotoapparat
                .with(this)
                .into(camera)
                .previewScaleType(ScaleType.CenterCrop)
                .photoResolution(ResolutionSelectorsKt.highestResolution())
                .lensPosition(LensPositionSelectorsKt.back())
                .focusMode(SelectorsKt.firstAvailable(
                        FocusModeSelectorsKt.continuousFocusPicture(),
                        FocusModeSelectorsKt.autoFocus(),
                        FocusModeSelectorsKt.fixed()
                ))
                .flash(SelectorsKt.firstAvailable(
                        FlashSelectorsKt.autoRedEye(),
                        FlashSelectorsKt.autoFlash(),
                        FlashSelectorsKt.torch()
                ))
                .build();
    }

    private void checkForCameraPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_CAMERA);

        } else {
            fotoapparat.start();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fotoapparat.start();
                } else {
                    Toast.makeText(this, getString(R.string.permission_denied_msg), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

        }
    }
}
