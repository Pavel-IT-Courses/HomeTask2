package com.gmail.pavkascool.h7_weather;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class Weather {
    private String location, temp, desc, iconCode, time;
    private Bitmap weatherImage;
    IconHolder iconHolder = IconHolder.getInstance();

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public String getIconCode() {
        return iconCode;
    }

    public void setIconCode(String iconCode) {
        this.iconCode = iconCode;
    }

    public String getTime() {
        return time;
    }

    public Weather(String location) {
        this.location = location;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Bitmap getWeatherImage() {
        return weatherImage;
    }

    public void setWeatherImage(Bitmap weatherImage) {
        this.weatherImage = weatherImage;
    }
}
