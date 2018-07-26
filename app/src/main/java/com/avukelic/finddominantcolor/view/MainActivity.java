package com.avukelic.finddominantcolor.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.avukelic.finddominantcolor.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.container_camera)
    public void goToCamera() {
        launchActivity(CameraActivity.class);
    }

    @OnClick(R.id.container_gallery)
    public void goToGallery() {
        launchActivity(GalleryActivity.class);
    }

    private void launchActivity(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }
}
