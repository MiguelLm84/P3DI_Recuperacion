package com.miguel_lm.newapptodo.ui;

import android.view.Menu;

import com.miguel_lm.newapptodo.core.Tarea;

public interface ListenerTareas {


    void seleccionarTarea(Tarea tarea);
    void eliminarTarea(Tarea tarea);
    void seleccionarTareasFavAdd(Tarea tarea);
    void seleccionarTareasFavRemove(Tarea tarea);
    void completarTarea(Tarea tarea, boolean completada);
}
