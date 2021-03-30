package com.miguel_lm.newapptodo.core;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

public class DBManagerRoom {

    @Database(entities = {Tarea.class}, version = 1, exportSchema = false)
    @TypeConverters(DateConverter.class)
    public static abstract class AppDatabase extends RoomDatabase {
        public abstract TareaDao getTareaDao();
    }
}
