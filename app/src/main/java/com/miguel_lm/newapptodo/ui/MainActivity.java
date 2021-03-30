package com.miguel_lm.newapptodo.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.miguel_lm.newapptodo.R;
import com.miguel_lm.newapptodo.core.Tarea;
import com.miguel_lm.newapptodo.core.TareaLab;
import com.miguel_lm.newapptodo.ui.fragments.FragmentTareas;
import com.miguel_lm.newapptodo.ui.fragments.FragmentTareasCompletadas;
import com.miguel_lm.newapptodo.ui.fragments.FragmentTareasFav;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private long tiempoParaSalir = 0;
    private Tarea tareaAmodificar;
    private static final int REQUEST_NUEVA_TAREA = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(R.drawable.to_do__2_);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_favoritas, R.id.navigation_tareas, R.id.navigation_completadas)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

    }

    @Override
    public void onResume() {
        super.onResume();

        TareaLab tareaLab = TareaLab.get(this);
        List<Tarea> listaTareas = tareaLab.getTareas();

        Date fechaActual = new Date();
        ArrayList<Tarea> listaTareasFinalizadas = new ArrayList<>();

        for (Tarea tarea : listaTareas) {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            boolean tareaEsCaducada = sdf.format(tarea.getFechaLimite()).compareTo(sdf.format(fechaActual)) < 0;

            if (tareaEsCaducada) {
                listaTareasFinalizadas.add(tarea);
            }
        }

        if (!listaTareasFinalizadas.isEmpty()) {
            mostrarTareasCaducadas(listaTareasFinalizadas);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_NUEVA_TAREA && resultCode == RESULT_OK) {
            refrescarTodosListados();
        }
    }

    private void refrescarTodosListados() {

        if (FragmentTareas.FragmentTareasInstance != null)
            FragmentTareas.FragmentTareasInstance.refrescarListado();
        if (FragmentTareasFav.FragmentTareasFavInstance != null)
            FragmentTareasFav.FragmentTareasFavInstance.refrescarListado();
        if (FragmentTareasCompletadas.FragmentTareasInstanceCompletadas != null)
            FragmentTareasCompletadas.FragmentTareasInstanceCompletadas.refrescarListado();
    }

    public void botonNuevaTareaClick(View view) {

        Intent intentNuevaTarea = new Intent(this, ActivityTarea.class);
        startActivityForResult(intentNuevaTarea, REQUEST_NUEVA_TAREA);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_principal, menu);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.accionCrearTarea) {
            botonNuevaTareaClick(null);

        } else if (item.getItemId() == R.id.accionModificar) {
            accionEscogerYModificar();

        } else if (item.getItemId() == R.id.accionEliminar) {
            accionEscogerYEliminar();
        }
        return super.onOptionsItemSelected(item);
    }


    private void crearOModificarTarea(final Tarea tareaAmodificar) {

        Intent intentNuevaTarea = new Intent(this, ActivityTarea.class);
        intentNuevaTarea.putExtra(ActivityTarea.PARAM_TAREA_EDITAR, tareaAmodificar);
        startActivityForResult(intentNuevaTarea, REQUEST_NUEVA_TAREA);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private void accionEscogerYModificar() {

        TareaLab tareaLab = TareaLab.get(this);
        List<Tarea> listaTareas = tareaLab.getTareas();

        AlertDialog.Builder builderDialogEscogerTareas = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom));
        builderDialogEscogerTareas.setIcon(R.drawable.editartarea2);
        builderDialogEscogerTareas.setTitle("Modificar Tarea");


        final String[] arrayTareasAMostrar = new String[listaTareas.size()];
        for (int i = 0; i < listaTareas.size(); i++) {
            arrayTareasAMostrar[i] = listaTareas.get(i).toStringTarea();
        }
        builderDialogEscogerTareas.setSingleChoiceItems(arrayTareasAMostrar, -1, (dialog, posicionElementoSeleccionado) -> tareaAmodificar = listaTareas.get(posicionElementoSeleccionado));
        builderDialogEscogerTareas.setPositiveButton("Modificar", (dialog, i) -> {

            if (tareaAmodificar == null) {
                Toast.makeText(getApplicationContext(), "Debe escoger una tarea", Toast.LENGTH_SHORT).show();
            } else {
                crearOModificarTarea(tareaAmodificar);
            }
        });
        builderDialogEscogerTareas.setNegativeButton("Cancelar", null);
        builderDialogEscogerTareas.create().show();
    }

    private void accionEscogerYEliminar() {

        TareaLab tareaLab = TareaLab.get(this);
        List<Tarea> listaTareas = tareaLab.getTareas();

        AlertDialog.Builder builderEliminar = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom),R.style.AlertDialogCustomBoton);
        builderEliminar.setIcon(R.drawable.ic_eliminar__1_);
        builderEliminar.setTitle("Eliminar elementos");

        final ArrayList<String> listaTareasAeliminar = new ArrayList<>();
        String[] arrayTareas = new String[listaTareas.size()];
        final boolean[] tareasSeleccionadas = new boolean[listaTareas.size()];
        for (int i = 0; i < listaTareas.size(); i++) {
            arrayTareas[i] = "\n· TAREA: " + listaTareas.get(i).getTitulo() + "\n· FECHA:  " + listaTareas.get(i).getFechaTextoCorta();
        }
        builderEliminar.setMultiChoiceItems(arrayTareas, tareasSeleccionadas, (dialog, indiceSeleccionado, isChecked) -> {
            tareasSeleccionadas[indiceSeleccionado] = isChecked;
            String tareasParaEliminar = "\n· TAREA: " + listaTareas.get(indiceSeleccionado).getTitulo() + "\n· FECHA:  " + listaTareas.get(indiceSeleccionado).getFechaTextoCorta() + "\n";
            listaTareasAeliminar.add(tareasParaEliminar);
        });

        builderEliminar.setPositiveButton("Borrar", (dialog, which) -> {

            AlertDialog.Builder builderEliminar_Confirmar = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom),R.style.AlertDialogCustomBoton);
            builderEliminar_Confirmar.setIcon(R.drawable.exclamation);
            builderEliminar_Confirmar.setTitle("¿Eliminar los elementos?");
            String tareasPorBorrar = null;

            if(listaTareasAeliminar.isEmpty()){
                Toast.makeText(getApplicationContext(), "Debe escoger una tarea", Toast.LENGTH_SHORT).show();
                accionEscogerYEliminar();
                return;
            }

            for (int i = 0; i < listaTareasAeliminar.size(); i++) {
                tareasPorBorrar = listaTareasAeliminar.get(i);
            }
            builderEliminar_Confirmar.setMessage(tareasPorBorrar);
            builderEliminar_Confirmar.setNegativeButton("Cancelar", null);
            builderEliminar_Confirmar.setPositiveButton("Borrar", (dialogInterface, which1) -> {

                for (int i = listaTareas.size() - 1; i >= 0; i--) {
                    if (tareasSeleccionadas[i]) {
                        listaTareas.remove(i);
                        tareaLab.get(this).deleteTarea(listaTareas.get(i));
                    }
                }
                Toast.makeText(getApplicationContext(), "Tareas eliminadas correctamente en la BD.", Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "Tareas eliminadas correctamente.", Toast.LENGTH_SHORT).show();
            });
            builderEliminar_Confirmar.create().show();
            dialog.dismiss();
        });

        builderEliminar.setNegativeButton("Cancelar", null);
        builderEliminar.create().show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        return Navigation.findNavController(this, R.id.nav_host_fragment).navigateUp();
    }

    @Override
    public void onBackPressed() {

        long tiempo = System.currentTimeMillis();
        if (tiempo - tiempoParaSalir > 3000) {
            tiempoParaSalir = tiempo;
            Toast.makeText(this, "Presione de nuevo 'Atrás' si desea salir",
                    Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();
            overridePendingTransition(R.anim.right_in, R.anim.right_out);
        }
    }

    private void mostrarTareasCaducadas(final List<Tarea> listaTareasFinalizadas) {

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom),R.style.AlertDialogCustomBoton);
        builder.setIcon(R.drawable.eliminar__1_);
        builder.setTitle("Tareas Finalizadas");

        String listaTareasParaBorrar = null;

        String[] arrayTareas = new String[listaTareasFinalizadas.size()];
        final boolean[] tareasSeleccionadas = new boolean[listaTareasFinalizadas.size()];
        for (int i = 0; i < listaTareasFinalizadas.size(); i++) {
            arrayTareas[i] = "\n· TAREA: " + listaTareasFinalizadas.get(i).getTitulo() + "\n· FECHA:  " + listaTareasFinalizadas.get(i).getFechaTextoCorta();
            listaTareasParaBorrar = arrayTareas[i];
        }
        builder.setMultiChoiceItems(arrayTareas, tareasSeleccionadas, (dialog, i, isChecked) -> tareasSeleccionadas[i] = isChecked);

        final String finalListaTareasParaBorrar = listaTareasParaBorrar;
        builder.setPositiveButton("Borrar", (dialog, which) -> {

            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setIcon(R.drawable.eliminar__1_);
            builder1.setTitle("¿Eliminar el elemento?");
            builder1.setMessage(finalListaTareasParaBorrar);

            AlertDialog.Builder builderEliminar_Confirmar = new AlertDialog.Builder(this);
            builderEliminar_Confirmar.setIcon(R.drawable.exclamation);
            builderEliminar_Confirmar.setMessage("¿Eliminar los elementos?");
            builderEliminar_Confirmar.setNegativeButton("Cancelar", null);
            builderEliminar_Confirmar.setPositiveButton("Borrar", (dialogInterface, which1) -> {

                TareaLab tareaLab = TareaLab.get(this);

                for (int i = listaTareasFinalizadas.size() - 1; i >= 0; i--) {
                    if (tareasSeleccionadas[i]) {
                        tareaLab.get(this).deleteTarea(listaTareasFinalizadas.get(i));
                    }
                }
                Toast.makeText(this, "Tareas eliminadas correctamente", Toast.LENGTH_SHORT).show();

                refrescarTodosListados();
            });
            builderEliminar_Confirmar.create().show();
        });
        builder.setNegativeButton("Cancelar", null);
        builder.create().show();
    }
}