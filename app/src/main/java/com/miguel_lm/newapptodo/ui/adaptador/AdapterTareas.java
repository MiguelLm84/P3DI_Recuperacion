package com.miguel_lm.newapptodo.ui.adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.miguel_lm.newapptodo.R;
import com.miguel_lm.newapptodo.core.Tarea;
import com.miguel_lm.newapptodo.ui.ListenerTareas;

import java.util.List;

public class AdapterTareas extends RecyclerView.Adapter<ViewHolderTarea> {

    private List<Tarea> listaTareas;
    Context context;
    private final ListenerTareas listenerTareas;

    public AdapterTareas(final Context context, List<Tarea> listaTareas, ListenerTareas listenerTareas) {
        this.context = context;
        this.listaTareas = listaTareas;
        this.listenerTareas = listenerTareas;
    }

    public void actualizarListado(List<Tarea> listaTareas) {
        this.listaTareas = listaTareas;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolderTarea onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tarea, parent, false);
        return new ViewHolderTarea(v, listenerTareas);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderTarea holder, int position) {
        holder.mostrarTarea(listaTareas.get(position), context);
    }

    @Override
    public int getItemCount() {
        return listaTareas.size();
    }
}
