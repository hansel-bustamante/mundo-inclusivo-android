package com.example.mundoinclusivo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.mundoinclusivo.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ParticipantesActivity extends AppCompatActivity {

    String idActividad, nombreActividad;
    DatabaseHelper dbHelper;
    RecyclerView rvParticipantes;
    TextView tvTitulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participantes);

        idActividad = getIntent().getStringExtra("ID_ACTIVIDAD");
        nombreActividad = getIntent().getStringExtra("NOMBRE_ACTIVIDAD");

        tvTitulo = findViewById(R.id.tvTituloAct);
        tvTitulo.setText(nombreActividad);

        rvParticipantes = findViewById(R.id.rvParticipantes);
        rvParticipantes.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DatabaseHelper(this);

        // Cargar lista inicial
        cargarParticipantes();

        // Botón Ver Sesiones (Navegación)
        findViewById(R.id.btnVerSesiones).setOnClickListener(v -> {
            Intent intent = new Intent(this, SesionesActivity.class);
            intent.putExtra("ID_ACTIVIDAD", idActividad);
            intent.putExtra("NOMBRE_ACTIVIDAD", nombreActividad);
            startActivity(intent);
        });

        // Botón Agregar Participante (Lógica)
        findViewById(R.id.btnAgregarParticipante).setOnClickListener(v -> mostrarDialogoAgregar());
    }

    private void cargarParticipantes() {
        // 1. Consultar base de datos
        List<Map<String, String>> lista = dbHelper.getParticipantesPorActividad(idActividad);

        // 2. Verificar si hay datos
        if (lista.isEmpty()) {
            Toast.makeText(this, "Aún no hay participantes vinculados.", Toast.LENGTH_SHORT).show();
        }

        // 3. CONFIGURAR EL ADAPTADOR
        com.example.mundoinclusivo.adapters.ParticipantesAdapter adapter =
                new com.example.mundoinclusivo.adapters.ParticipantesAdapter(lista);

        rvParticipantes.setAdapter(adapter);
    }

    // 1. PRIMER DIÁLOGO: SELECCIONAR PERSONA
    private void mostrarDialogoAgregar() {
        List<Map<String, String>> disponibles = dbHelper.getPersonasDisponibles(idActividad);

        if (disponibles.isEmpty()) {
            Toast.makeText(this, "No hay personas disponibles para agregar", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] nombres = new String[disponibles.size()];
        String[] ids = new String[disponibles.size()];

        for (int i = 0; i < disponibles.size(); i++) {
            nombres[i] = disponibles.get(i).get("nombre");
            ids[i] = disponibles.get(i).get("id");
        }

        new AlertDialog.Builder(this)
                .setTitle("Seleccionar Persona")
                .setItems(nombres, (dialog, which) -> {
                    String idPersona = ids[which];
                    String nombrePersona = nombres[which];

                    // AL SELECCIONAR -> ABRIMOS EL SEGUNDO DIÁLOGO DE DETALLES
                    mostrarFormularioDetalle(idPersona, nombrePersona);
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    // 2. SEGUNDO DIÁLOGO: COMPLETAR DATOS (Institución, Discapacidad, etc.)
    private void mostrarFormularioDetalle(String idPersona, String nombrePersona) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_vincular_participante, null);

        // Referencias UI
        TextView tvNombre = view.findViewById(R.id.tvNombreParticipante);
        Spinner spinnerInst = view.findViewById(R.id.spinnerInstitucion);
        android.widget.CheckBox cbDisc = view.findViewById(R.id.cbDiscapacidad);
        android.widget.CheckBox cbFam = view.findViewById(R.id.cbFamiliar);
        android.widget.CheckBox cbFirma = view.findViewById(R.id.cbFirma);

        tvNombre.setText(nombrePersona);

        // A. Cargar Instituciones en Spinner
        List<Map<String, String>> instituciones = dbHelper.getAllInstituciones();
        List<String> nombresInst = new ArrayList<>();
        List<Integer> idsInst = new ArrayList<>();

        for (Map<String, String> inst : instituciones) {
            nombresInst.add(inst.get("nombre"));
            idsInst.add(Integer.parseInt(inst.get("id")));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, nombresInst);
        spinnerInst.setAdapter(adapter);

        // B. Pre-seleccionar Institución si la persona ya tiene una
        int idInstActual = dbHelper.getInstitucionDePersona(idPersona);
        if (idInstActual != -1) {
            int posicion = idsInst.indexOf(idInstActual);
            if (posicion >= 0) {
                spinnerInst.setSelection(posicion);
            }
        }

        builder.setView(view)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    // Obtener datos del formulario
                    int posicionSpinner = spinnerInst.getSelectedItemPosition();
                    int idInstitucionSeleccionada = idsInst.get(posicionSpinner);

                    boolean tieneDisc = cbDisc.isChecked();
                    boolean esFam = cbFam.isChecked();
                    boolean registraFirma = cbFirma.isChecked();

                    // Guardar en BD
                    boolean exito = dbHelper.vincularParticipanteCompleto(
                            idPersona, idActividad, idInstitucionSeleccionada, tieneDisc, esFam, registraFirma
                    );

                    if (exito) {
                        Toast.makeText(this, "Participante añadido correctamente", Toast.LENGTH_SHORT).show();
                        cargarParticipantes(); // Refrescar lista de atrás
                    } else {
                        Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null);

        builder.create().show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarParticipantes();
    }
}