package com.gmail.pavkascool.h7_weather;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class Weather {
    private String location, temp, desc;
    private Bitmap weatherImage;

    public String getTime() {
        return "";
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
