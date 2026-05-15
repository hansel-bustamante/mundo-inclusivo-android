package com.example.mundoinclusivo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mundoinclusivo.database.DatabaseHelper;

import java.util.Calendar;

public class AgregarPersonaActivity extends AppCompatActivity {

    private EditText etNombre, etPaterno, etMaterno, etCI, etFechaNac, etCelular, etProcedencia;
    private Spinner spinnerGenero;
    private Button btnGuardar;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_persona);

        // Inicializar Vistas
        etNombre = findViewById(R.id.etNombre);
        etPaterno = findViewById(R.id.etPaterno);
        etMaterno = findViewById(R.id.etMaterno);
        etCI = findViewById(R.id.etCI);
        etFechaNac = findViewById(R.id.etFechaNac);
        etCelular = findViewById(R.id.etCelular);
        etProcedencia = findViewById(R.id.etProcedencia);
        spinnerGenero = findViewById(R.id.spinnerGenero);
        btnGuardar = findViewById(R.id.btnGuardarPersona);

        dbHelper = new DatabaseHelper(this);

        // Configurar Spinner
        String[] generos = {"M", "F"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, generos);
        spinnerGenero.setAdapter(adapter);

        // DatePicker
        etFechaNac.setOnClickListener(v -> showDatePicker());

        // Guardar
        btnGuardar.setOnClickListener(v -> guardarPersona());
    }

    private void showDatePicker() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(this, (view, y, m, d) -> {
            etFechaNac.setText(y + "-" + String.format("%02d", (m + 1)) + "-" + String.format("%02d", d));
        }, year, month, day);

        dpd.show();
    }

    private void guardarPersona() {
        String nombre = etNombre.getText().toString().trim();
        String paterno = etPaterno.getText().toString().trim();
        String materno = etMaterno.getText().toString().trim();
        String ci = etCI.getText().toString().trim(); // <--- Importante el trim()
        String fecha = etFechaNac.getText().toString().trim();
        String genero = spinnerGenero.getSelectedItem().toString();
        String celular = etCelular.getText().toString().trim();
        String procedencia = etProcedencia.getText().toString().trim();

        // 1. VALIDACIÓN DUPLICADOS (Bloqueante)
        if (dbHelper.existePersonaPorCI(ci)) {
            // ALERTA VISUAL CLARA
            etCI.setError("¡Este C.I. ya existe!");
            etCI.requestFocus();

            // Mensaje flotante
            Toast.makeText(this, "ERROR: La persona con CI " + ci + " ya está registrada.", Toast.LENGTH_LONG).show();

            return; // <--- ESTO ES CRÍTICO: DETIENE LA EJECUCIÓN
        }

        // 2. NUEVA VALIDACIÓN: DUPLICADOS
        if (dbHelper.existePersonaPorCI(ci)) {
            // Mostramos error visual en el campo CI
            etCI.setError("Este C.I. ya está registrado");
            etCI.requestFocus(); // Movemos el cursor ahí
            Toast.makeText(this, "Error: La persona ya existe en la base de datos", Toast.LENGTH_LONG).show();
            return; // DETENEMOS EL GUARDADO
        }

        // 3. Obtener Área y Guardar
        SharedPreferences prefs = getSharedPreferences("sesion", MODE_PRIVATE);
        String areaId = prefs.getString("area", "M1");

        boolean exito = dbHelper.addPersonaLocal(nombre, paterno, materno, ci, fecha, genero, celular, procedencia, areaId);

        if (exito) {
            Toast.makeText(this, "Persona Registrada Correctamente", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error al guardar en base de datos", Toast.LENGTH_SHORT).show();
        }
    }
}
