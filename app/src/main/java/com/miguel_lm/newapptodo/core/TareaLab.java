package com.miguel_lm.newapptodo.core;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.room.Room;

import java.util.List;

public class TareaLab {

    @SuppressLint("StaticFieldLeak")
    private static TareaLab tareaLab;

    private TareaDao tareaDao;

    private TareaLab(Context context) {
        Context appContext = context.getApplicationContext();
        DBManagerRoom.AppDatabase database = Room.databaseBuilder(appContext, DBManagerRoom.AppDatabase.class, "tarea").allowMainThreadQueries().build(); //.addMigrations ( MIGRATION_1_2 )
        tareaDao = database.getTareaDao();
    }

    public static TareaLab get(Context context) {
        if (tareaLab == null) {
            tareaLab = new TareaLab(context);
        }
        return tareaLab;
    }

    public List<Tarea> getTareas() {
        return tareaDao.getTareas();
    }

    public List<Tarea> getTareasNoCaducadas() {
        return tareaDao.getTareasNoCaducadas();
    }


    public List<Tarea> getTareasFavoritas() {
        return tareaDao.getTareasFavoritas();
    }

    public List<Tarea> getTareasCaducadas() {
        return tareaDao.getTareasCaducadas();
    }

    public void insertTarea(Tarea tarea) {
        tareaDao.insert(tarea);
    }

    public void updateTarea(Tarea tarea) {
        tareaDao.update(tarea);
    }

    public void deleteTarea(Tarea tarea) {
        tareaDao.delete(tarea);
    }
}
