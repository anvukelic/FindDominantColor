package com.avukelic.finddominantcolor.model;

import android.graphics.Bitmap;
import android.support.v7.graphics.Palette;

import com.avukelic.finddominantcolor.App;
import com.avukelic.finddominantcolor.R;
import com.avukelic.finddominantcolor.utils.ColorList;
import com.avukelic.finddominantcolor.utils.ColorName;

/**
 * Created by avukelic on 24-Jul-18.
 */
public class Image {

    private Bitmap bitmap;
    private String color;
    private String hexadecimal;
    private String RGB;

    private int r, g, b;

    ColorList colors;

    public Image() {
        colors = new ColorList();
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getHexadecimal() {
        return hexadecimal;
    }

    public void setHexadecimal(String hexadecimal) {
        this.hexadecimal = hexadecimal;
    }

    public String getRGB() {
        return RGB;
    }

    public void setRGB(String RGB) {
        this.RGB = RGB;
    }

    //Get RGB, Hexadecimal and Name value for dominant color
    public void getDominantColor() {
        Palette p = Palette.from(getBitmap()).generate();
        setHexadecimal(getHexadecimal(p.getDominantSwatch().getRgb()));
        setRGB(getRgbCode(p.getDominantSwatch().getRgb()));
        if (isGray(r, g, b)) {
            setColor(App.getContext().getString(R.string.gray));
        } else {
            setColor(getColorName(r, g, b));
        }
    }

    //Convert Hexadecimal value to RGB value
    private String getRgbCode(int intColor) {
        String color = String.format("#%06X", (0xFFFFFF & intColor));
        r = Integer.valueOf(color.substring(1, 3), 16);
        g = Integer.valueOf(color.substring(3, 5), 16);
        b = Integer.valueOf(color.substring(5, 7), 16);
        StringBuilder sb = new StringBuilder();
        sb.append("Red: ");
        sb.append(r);
        sb.append(" Green: ");
        sb.append(g);
        sb.append(" Blue: ");
        sb.append(b);
        return sb.toString();
    }

    //Get Hexadecimal
    private String getHexadecimal(int intColor) {
        return String.format("#%06X", (0xFFFFFF & intColor));
    }

    //Check if color is gray
    private boolean isGray(int r, int g, int b) {
        if (r == g && r == b) {
            return true;
        } else {
            return false;
        }
    }

    //If color is not gray check for other colors
    private String getColorName(int r, int g, int b) {
        ColorName closestMatch = null;
        int minMSE = Integer.MAX_VALUE;
        int mse;
        for (ColorName c : colors.getColors()) {
            System.out.println(c.getName());
            mse = c.computeMSE(r, g, b);
            System.out.println(mse);
            if (mse < minMSE) {
                minMSE = mse;
                closestMatch = c;
            }
        }
        if (closestMatch != null) {
            return closestMatch.getName();
        } else {
            return "No matched color name.";
        }
    }
}
