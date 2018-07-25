package com.avukelic.finddominantcolor.utils;

import com.avukelic.finddominantcolor.App;
import com.avukelic.finddominantcolor.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by avukelic on 25-Jul-18.
 */
public class ColorList {

    private List<ColorName> colors = new ArrayList<>();

    public ColorList() {
        colors.add(new ColorName(App.getContext().getString(R.string.black), 0x00, 0x00, 0x00));
        colors.add(new ColorName(App.getContext().getString(R.string.white), 0xff, 0xff, 0xff));
        colors.add(new ColorName(App.getContext().getString(R.string.red), 0xFF, 0x00, 0x00));
        colors.add(new ColorName(App.getContext().getString(R.string.blue), 0x00, 0x00, 0xFF));
        colors.add(new ColorName(App.getContext().getString(R.string.yellow), 0xFF, 0xFF, 0x00));
        colors.add(new ColorName(App.getContext().getString(R.string.purple), 0xff, 0x00, 0xff));
        colors.add(new ColorName(App.getContext().getString(R.string.green), 0x00, 0x80, 0x00));
        colors.add(new ColorName(App.getContext().getString(R.string.orange), 0xFF, 0xA5, 0x00));
    }

    public List<ColorName> getColors() {
        return colors;
    }
}
