package com.example.mundoinclusivo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import com.example.mundoinclusivo.adapters.SesionesAdapter;
import com.example.mundoinclusivo.database.DatabaseHelper;
import java.util.List;
import java.util.Map;
import android.widget.Button;


public class SesionesActivity extends AppCompatActivity {

    String idActividad, nombreActividad;
    RecyclerView rvSesiones;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sesiones); // Debes crear este XML

        // Recibir datos de la pantalla anterior
        idActividad = getIntent().getStringExtra("ID_ACTIVIDAD");
        nombreActividad = getIntent().getStringExtra("NOMBRE_ACTIVIDAD");

        TextView tvTitulo = findViewById(R.id.tvTituloActividad); // Pon este ID en tu XML
        if(tvTitulo != null) tvTitulo.setText(nombreActividad);

        rvSesiones = findViewById(R.id.rvSesiones); // Pon este ID en tu XML
        rvSesiones.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DatabaseHelper(this);
        cargarSesiones();

        Button btnCrear = findViewById(R.id.btnCrearSesion);
        btnCrear.setOnClickListener(v -> crearNuevaSesion());
    }

    private void cargarSesiones() {
        List<Map<String, String>> lista = dbHelper.getSesionesPorActividad(idActividad);

        SesionesAdapter adapter = new SesionesAdapter(lista, (idSesion, tema) -> {
            Intent intent = new Intent(this, AsistenciaActivity.class);
            intent.putExtra("ID_SESION", idSesion);
            intent.putExtra("TEMA_SESION", tema);

            // AGREGAR ESTA LÍNEA:
            intent.putExtra("ID_ACTIVIDAD", idActividad); // Variable global de la clase

            startActivity(intent);
        });

        rvSesiones.setAdapter(adapter);
    }

    private void crearNuevaSesion() {
        // 1. Contar cuántas hay para saber el número
        List<Map<String, String>> actuales = dbHelper.getSesionesPorActividad(idActividad);
        int siguienteNumero = actuales.size() + 1; // Si hay 0, crea la 1. Si hay 1, crea la 2.

        // 2. Guardar en BD
        boolean exito = dbHelper.crearSesionAutomatica(idActividad, siguienteNumero);

        if (exito) {
            Toast.makeText(this, "Sesión N° " + siguienteNumero + " creada", Toast.LENGTH_SHORT).show();
            cargarSesiones(); // Refrescar lista
        } else {
            Toast.makeText(this, "Error al crear sesión", Toast.LENGTH_SHORT).show();
        }
    }
}