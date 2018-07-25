package com.avukelic.finddominantcolor.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.avukelic.finddominantcolor.model.Image;

/**
 * Created by avukelic on 25-Jul-18.
 */
public class PhotoViewModel extends ViewModel {

    private MutableLiveData<Image> image;

    public LiveData<Image> setImage(Image image) {
        if (this.image == null) {
            this.image = new MutableLiveData<>();
            this.image.setValue(image);
        }
        return this.image;
    }

    public void updateImage(Image image){
        this.image.setValue(image);
        this.image.getValue().getDominantColor();
    }

    public MutableLiveData<Image> getImage() {
        return image;
    }
}
