package com.avukelic.finddominantcolor.view;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

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
        fotoapparat.start();
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
        viewModel.getImage().observe(this, image->{
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
                        FocusModeSelectorsKt. continuousFocusPicture(),
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
}
