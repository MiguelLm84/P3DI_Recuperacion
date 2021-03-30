package com.miguel_lm.newapptodo.core;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Entity(tableName = "Tarea")
public class Tarea implements Serializable, Comparable<Tarea> {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int mId;

    @ColumnInfo(name="titulo")
    public String titulo;

    @ColumnInfo(name="fechaCreacion")
    public Date fechaCreacion;

    @ColumnInfo(name="esFav")
    public boolean esFav;

    @ColumnInfo(name="completado")
    public boolean completado;

    @ColumnInfo(name="fechaLimite")
    public Date fechaLimite;

    private boolean tareaSeleccionada;

    public Tarea(String titulo, Date fechaLimite) {
        this.titulo = titulo;
        this.fechaCreacion = new Date();
        this.esFav = false;
        this.completado = false;
        this.fechaLimite = fechaLimite;
        tareaSeleccionada = false;
        mId = 0;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public boolean isEsFav() {
        return esFav;
    }

    public void setEsFav(boolean esFav) {
        this.esFav = esFav;
    }

    public boolean isCompletado() {
        return completado;
    }

    public void setCompletado(boolean completado) {
        this.completado = completado;
    }

    public Date getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(Date fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public String getFechaTexto() {
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd MMMM 'de' yyyy ", Locale.getDefault());
        return formatoFecha.format(fechaLimite);
    }

    public String getFechaTextoCorta() {
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy ", Locale.getDefault());
        return formatoFecha.format(fechaLimite);
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String toStringTarea(){
        return "\n· TITULO: "+getTitulo()+"\n\n· FECHA: "+getFechaTexto();
    }

    public void modificar(String titulo, Date fecha) {
        this.titulo = titulo;
        this.fechaLimite = fecha;
    }

    public boolean isTareaSeleccionada() {
        return tareaSeleccionada;
    }

    public void setTareaSeleccionada(boolean tareaSeleccionada) {
        this.tareaSeleccionada = tareaSeleccionada;
    }

    @Override
    public int compareTo(Tarea tarea) {

        if(tarea.fechaLimite.compareTo(fechaLimite)< 0){
            return -1;
        } else if(tarea.fechaLimite.compareTo(fechaLimite)< 0){
            return 0;
        } else{
            return 1;
        }
    }
}