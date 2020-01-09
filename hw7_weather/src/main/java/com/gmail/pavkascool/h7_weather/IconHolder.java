package com.gmail.pavkascool.h7_weather;

import android.graphics.Bitmap;

import java.util.LinkedHashMap;

//Singleton to cash icons from OpenWeather

public class IconHolder extends LinkedHashMap<String, Bitmap> {
    private final static int LENGTH = 16;

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
