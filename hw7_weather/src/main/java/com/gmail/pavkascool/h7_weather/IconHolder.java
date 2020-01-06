package com.gmail.pavkascool.h7_weather;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;


import java.util.LinkedHashMap;


public class IconHolder extends LinkedHashMap<String, Bitmap> {
    private final static int LENGTH = 8;

    private static IconHolder iconHolder;
    public static IconHolder getInstance() {
        if(iconHolder == null) iconHolder = new IconHolder();
        return iconHolder;
    }

    private IconHolder() {
        super(LENGTH, 0.8f, true);
    }

    @Override
    protected boolean removeEldestEntry(Entry<String, Bitmap> eldest) {
        return size() > LENGTH;
    }
}
