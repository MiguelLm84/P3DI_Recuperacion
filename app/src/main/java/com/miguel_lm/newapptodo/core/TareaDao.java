package com.miguel_lm.newapptodo.core;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TareaDao {

    @Query("SELECT * FROM tarea")
    List<Tarea> getTareas();


    @Query("SELECT * FROM tarea WHERE completado = 0 ORDER BY fechaLimite")
    List<Tarea> getTareasNoCaducadas();

    @Query("SELECT * FROM tarea WHERE esFav = 1 ORDER BY fechaLimite")
    List<Tarea> getTareasFavoritas();

    @Query("SELECT * FROM tarea WHERE completado = 1 ORDER BY fechaLimite")
    List<Tarea> getTareasCaducadas();

    @Insert
    void insert(Tarea tarea);

    @Delete
    void delete(Tarea tarea);

    @Update
    void update(Tarea tarea);
}
