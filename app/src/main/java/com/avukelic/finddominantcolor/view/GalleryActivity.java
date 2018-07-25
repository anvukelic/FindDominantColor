package com.avukelic.finddominantcolor.view;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avukelic.finddominantcolor.R;
import com.avukelic.finddominantcolor.model.Image;
import com.avukelic.finddominantcolor.viewmodel.PhotoViewModel;

import java.io.FileNotFoundException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GalleryActivity extends AppCompatActivity {

    public static final int GALLERY_REQUEST = 1;

    @BindView(R.id.color_name_gallery)
    TextView colorName;
    @BindView(R.id.color_hexa_gallery)
    TextView colorHexa;
    @BindView(R.id.color_rgb_gallery)
    TextView colorRGB;
    @BindView(R.id.image_from_gallery)
    ImageView image;

    PhotoViewModel viewModel;

    Image img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.search_for_picture);
        ButterKnife.bind(this);
        initializeUi();
    }

    @OnClick(R.id.btn_search_for_picture)
    public void launchGallery(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {if (resultCode == RESULT_OK) {
        try {
            final Uri imageUri = data.getData();
            final InputStream imageStream = getContentResolver().openInputStream(imageUri);
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

            image.setImageBitmap(resizeBitmap(selectedImage));
            img.setBitmap(selectedImage);
            viewModel.updateImage(img);
            updateDisplay();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(GalleryActivity.this, R.string.pick_image_error_msg, Toast.LENGTH_LONG).show();
        }

    }else {
        Toast.makeText(GalleryActivity.this, R.string.no_image_picked_msg,Toast.LENGTH_LONG).show();
    }
    }

    private Bitmap resizeBitmap(Bitmap selectedImage) {
        float aspectRatio = selectedImage.getWidth() /
                (float) selectedImage.getHeight();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = Math.round(width/aspectRatio);
        return Bitmap.createScaledBitmap(selectedImage,width,height,false);
    }

    private void initializeUi() {
        img = new Image();
        viewModel = ViewModelProviders.of(this).get(PhotoViewModel.class);
        viewModel.setImage(img);
        updateDisplay();
    }

    private void updateDisplay() {
        viewModel.getImage().observe(this, image->{
            colorName.setText(image.getColor());
            colorHexa.setText(image.getHexadecimal());
            colorRGB.setText(image.getRGB());
        });
    }
}
