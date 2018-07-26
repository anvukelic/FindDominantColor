package com.avukelic.finddominantcolor.utils;

import android.graphics.Bitmap;

import com.avukelic.finddominantcolor.model.Image;

/**
 * Created by avukelic on 26-Jul-18.
 */
public class ImageUtil {

    public static Image mapBitmapToImage(Bitmap bitmap){
        Image image = new Image();
        image.setBitmap(bitmap);
        return image;
    }
}

