package com.gmail.pavkascool.h7_weather;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

@Dao
public interface LocationDao {

    @Insert
    long insert(Locations loc);

    @Update
    int update(Locations loc);

    @Delete
    int delete(Locations loc);
}
