package com.gmail.pavkascool.h7_weather;

import android.app.Application;

import androidx.room.Room;

public class WeatherApplication extends Application {
    private static WeatherApplication instance;
    private static WeatherDataBase database;

    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(this, WeatherDataBase.class, "weather_database")
                .build();
    }

    public static WeatherApplication getInstance() {
        return instance;
    }

    public WeatherDataBase getDatabase() {
        return database;
    }
}
