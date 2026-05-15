package com.example.mundoinclusivo;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.mundoinclusivo.database.DatabaseHelper;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class CrearActividadActivity extends AppCompatActivity {

    private EditText etNombre, etFecha, etLugar, etDesc;
    private Spinner spinnerTipo;
    private Button btnGuardar;
    private DatabaseHelper dbHelper;
    private List<String> codigosIds = new ArrayList<>(); // Para guardar el ID real (A1, B2)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_actividad);

        etNombre = findViewById(R.id.etNombreAct);
        etFecha = findViewById(R.id.etFechaAct);
        etLugar = findViewById(R.id.etLugarAct);
        etDesc = findViewById(R.id.etDescAct);
        spinnerTipo = findViewById(R.id.spinnerTipoAct);
        btnGuardar = findViewById(R.id.btnGuardarAct);

        dbHelper = new DatabaseHelper(this);

        cargarSpinner();

        etFecha.setOnClickListener(v -> showDatePicker());
        btnGuardar.setOnClickListener(v -> guardarActividad());
    }

    private void cargarSpinner() {
        List<Map<String, String>> codigos = dbHelper.getAllCodigosActividad();
        List<String> nombres = new ArrayList<>();

        if (codigos.isEmpty()) {
            nombres.add("Sin códigos (Sincronice primero)");
            btnGuardar.setEnabled(false);
        } else {
            for (Map<String, String> codigo : codigos) {
                nombres.add(codigo.get("id") + " - " + codigo.get("nombre"));
                codigosIds.add(codigo.get("id")); // Guardamos el ID "A1" en paralelo
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, nombres);
        spinnerTipo.setAdapter(adapter);
    }

    private void showDatePicker() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(this, (view, y, m, d) -> {
            etFecha.setText(y + "-" + String.format("%02d", (m + 1)) + "-" + String.format("%02d", d));
        }, year, month, day);
        dpd.show();
    }

    private void guardarActividad() {
        String nombre = etNombre.getText().toString().trim();
        String fecha = etFecha.getText().toString().trim();
        String lugar = etLugar.getText().toString().trim();
        String desc = etDesc.getText().toString().trim();

        if (nombre.isEmpty() || fecha.isEmpty() || lugar.isEmpty()) {
            Toast.makeText(this, "Complete los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener ID del código seleccionado
        int pos = spinnerTipo.getSelectedItemPosition();
        String codigoId = codigosIds.get(pos);

        // Obtener Área del usuario
        SharedPreferences prefs = getSharedPreferences("sesion", MODE_PRIVATE);
        String areaId = prefs.getString("area", "M1");

        boolean exito = dbHelper.addActividadLocal(nombre, fecha, lugar, desc, codigoId, areaId);

        if (exito) {
            Toast.makeText(this, "Actividad Creada", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show();
        }
    }
}