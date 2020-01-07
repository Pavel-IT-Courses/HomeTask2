package com.gmail.pavkascool.h7_weather;

import androidx.room.Database;
import androidx.room.Entity;
import androidx.room.RoomDatabase;

@Database(entities={Locations.class}, version=1)
public abstract class WeatherDataBase extends RoomDatabase {

    public abstract LocationDao locationDao();
}
