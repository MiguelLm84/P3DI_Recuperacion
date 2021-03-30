package com.miguel_lm.newapptodo.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.miguel_lm.newapptodo.R;
import com.miguel_lm.newapptodo.core.Tarea;
import com.miguel_lm.newapptodo.core.TareaLab;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ActivityTarea extends AppCompatActivity {

    private EditText editTextTareaTitulo;
    private TextView textViewTareaFechaLimite;
    private TextView tv_tituloNuevaTarea;
    private Date fechaLimiteSeleccionada;
    private ImageButton btn_fav_no_activado,btn_fav_activado;
    List<Tarea> listaTareas;
    public static final String PARAM_TAREA_EDITAR = "param_tarea_editar";
    public enum ActivityTareaModo { crear, editar}
    private ActivityTareaModo activityTareaModo;
    private long tiempoParaSalir = 0;
    TareaLab tareaLab;
    Tarea tareaEditar;
    private ListenerTareas listenerTareas;
    Tarea tarea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarea);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.to_do__2_);
        overridePendingTransition(R.anim.right_in, R.anim.right_out);

        tareaLab = TareaLab.get(this);
        listaTareas = tareaLab.getTareas();

        editTextTareaTitulo = findViewById(R.id.ed_nomTarea);
        textViewTareaFechaLimite = findViewById(R.id.textViewTareaFechaLimite);
        tv_tituloNuevaTarea = findViewById(R.id.tv_tituloNuevaTarea);
        btn_fav_no_activado = findViewById(R.id.btn_Fav_blanco2);
        btn_fav_activado = findViewById(R.id.btn_Fav_amarillo2);

        tareaEditar = (Tarea) getIntent().getSerializableExtra(PARAM_TAREA_EDITAR);

        activityTareaModo = tareaEditar == null ? ActivityTareaModo.crear : ActivityTareaModo.editar;

        if (activityTareaModo == ActivityTareaModo.editar) {
            fechaLimiteSeleccionada = tareaEditar.fechaLimite;
            mostrarTarea();
            seleccionFav(tareaEditar);
            tv_tituloNuevaTarea.setText("Modificar Tarea");
            overridePendingTransition(R.anim.left_in, R.anim.left_out);
        }
        else {
            fechaLimiteSeleccionada = new Date();
            tv_tituloNuevaTarea.setText("Nueva Tarea");

            tarea = new Tarea(editTextTareaTitulo.getText().toString(),fechaLimiteSeleccionada);
            btn_fav_no_activado.setVisibility(tarea.esFav ? View.INVISIBLE : View.VISIBLE);
            btn_fav_activado.setVisibility(tarea.esFav ? View.VISIBLE : View.INVISIBLE);

            btn_fav_no_activado.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    btn_fav_no_activado.setVisibility(View.INVISIBLE);
                    btn_fav_activado.setVisibility(View.VISIBLE);

                    tarea.setEsFav(true);
                }
            });
            btn_fav_activado.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    btn_fav_activado.setVisibility(View.INVISIBLE);
                    btn_fav_no_activado.setVisibility(View.VISIBLE);

                    tarea.setEsFav(false);
                }
            });

            overridePendingTransition(R.anim.left_in, R.anim.left_out);
        }
        mostrarFecha();
    }

    private void mostrarTarea() {

        editTextTareaTitulo.setText(tareaEditar.getTitulo());
        textViewTareaFechaLimite.setText(tareaEditar.getFechaTexto());
    }

    private void mostrarFecha() {
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd MMMM 'de' yyyy", new Locale("es","ES"));
        textViewTareaFechaLimite.setText(formatoFecha.format(fechaLimiteSeleccionada));
    }

    public void cambiarFecha(View view) {

        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(fechaLimiteSeleccionada);

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        final DatePickerDialog dpd = new DatePickerDialog(this, (datePicker, year1, monthOfYear, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year1);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            fechaLimiteSeleccionada = calendar.getTime();

            mostrarFecha();
        }, year, month, day);
        dpd.show();
    }

    public void buttonCancelarClick(View view) {
        setResult(RESULT_CANCELED);
        finish();
        Toast.makeText(getApplicationContext(),"Ha salido sin realizar ninguna acción.",Toast.LENGTH_SHORT).show();
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }

    public void buttonOkClick(View view) {

        String titulo = editTextTareaTitulo.getText().toString();
        String fecha = textViewTareaFechaLimite.getText().toString();

        if (titulo.isEmpty()) {
            Toast.makeText(this, "El título no puede estar en blanco", Toast.LENGTH_SHORT).show();
            return;
        }

        if (activityTareaModo == ActivityTareaModo.crear) {

            tarea = new Tarea(titulo, fechaLimiteSeleccionada);
            tareaLab.insertTarea(tarea);
            listaTareas.add(tarea);

            if(tarea.isEsFav()){
                TareaLab.get(ActivityTarea.this).updateTarea(tarea);
                Toast.makeText(ActivityTarea.this, "Tarea añadida a Favoritos", Toast.LENGTH_SHORT).show();

                listenerTareas.seleccionarTareasFavAdd(tarea);
            }
        }

        setResult(RESULT_OK);
        finish();
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }

    @Override
    public void onBackPressed(){

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

    public void seleccionFav(Tarea tarea){

        btn_fav_no_activado.setVisibility(tarea.esFav ? View.INVISIBLE : View.VISIBLE);
        btn_fav_activado.setVisibility(tarea.esFav ? View.VISIBLE : View.INVISIBLE);

        btn_fav_no_activado.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                btn_fav_no_activado.setVisibility(View.INVISIBLE);
                btn_fav_activado.setVisibility(View.VISIBLE);

                tarea.setEsFav(true);

               // if(tarea != null){
                    TareaLab.get(ActivityTarea.this).updateTarea(tarea);
                    Toast.makeText(ActivityTarea.this, "Tarea añadida a Favoritos", Toast.LENGTH_SHORT).show();
               // }

                try{
                    listenerTareas.seleccionarTareasFavAdd(tarea);

                } catch (NullPointerException e){
                    Log.e("ERROR","NULL PONTER EXCEPTION EN LINEA 172");
                }
            }
        });

        btn_fav_activado.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                btn_fav_activado.setVisibility(View.INVISIBLE);
                btn_fav_no_activado.setVisibility(View.VISIBLE);

                tarea.setEsFav(false);

                //if(tarea != null){
                    TareaLab.get(ActivityTarea.this).updateTarea(tarea);
                    Toast.makeText(ActivityTarea.this, "Tarea eliminada de Favoritos", Toast.LENGTH_SHORT).show();

                /*} else {

                }*/

                try{
                    listenerTareas.seleccionarTareasFavRemove(tarea);

                } catch (NullPointerException e){
                    Log.e("ERROR","NULL PONTER EXCEPTION EN LINEA 172");
                }
            }
        });
    }
}
