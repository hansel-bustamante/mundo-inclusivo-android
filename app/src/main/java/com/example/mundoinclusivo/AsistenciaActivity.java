package com.example.mundoinclusivo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import com.example.mundoinclusivo.adapters.AsistenciaAdapter;
import com.example.mundoinclusivo.database.DatabaseHelper;
import java.util.List;
import java.util.Map;

public class AsistenciaActivity extends AppCompatActivity {

    String idSesion, idActividad, temaSesion;
    RecyclerView rvAsistencia;
    DatabaseHelper dbHelper;
    TextView tvTitulo, tvSubtitulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asistencia); // CREAR ESTE XML ABAJO

        // Recibir datos
        idSesion = getIntent().getStringExtra("ID_SESION");
        idActividad = getIntent().getStringExtra("ID_ACTIVIDAD"); // <--- OJO AQUI
        temaSesion = getIntent().getStringExtra("TEMA_SESION");

        tvTitulo = findViewById(R.id.tvTituloAsis);
        tvSubtitulo = findViewById(R.id.tvSubtituloAsis);
        rvAsistencia = findViewById(R.id.rvAsistencia);

        tvTitulo.setText("Asistencia");
        tvSubtitulo.setText(temaSesion);

        rvAsistencia.setLayoutManager(new LinearLayoutManager(this));
        dbHelper = new DatabaseHelper(this);

        cargarLista();
    }

    private void cargarLista() {
        List<Map<String, String>> lista = dbHelper.getListaAsistencia(idActividad, idSesion);

        if (lista.isEmpty()) {
            Toast.makeText(this, "No hay participantes inscritos en esta actividad", Toast.LENGTH_LONG).show();
        }

        AsistenciaAdapter adapter = new AsistenciaAdapter(lista, (idPersona, isChecked) -> {
            // AL TOCAR EL CHECKBOX: Guardar en BD
            int firma = isChecked ? 1 : 0;
            dbHelper.guardarAsistenciaLocal(idSesion, idPersona, firma, "Móvil");
        });

        rvAsistencia.setAdapter(adapter);
    }
}