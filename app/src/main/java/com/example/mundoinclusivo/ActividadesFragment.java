package com.example.mundoinclusivo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mundoinclusivo.adapters.ActividadesAdapter;
import com.example.mundoinclusivo.database.DatabaseHelper;

import java.util.List;
import java.util.Map;

public class ActividadesFragment extends Fragment {

    private RecyclerView rvActividades;
    private TextView tvEmpty;
    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_actividades, container, false);

        rvActividades = view.findViewById(R.id.rvActividades);
        tvEmpty = view.findViewById(R.id.tvEmpty);

        // Layout Manager
        rvActividades.setLayoutManager(new LinearLayoutManager(getContext()));

        dbHelper = new DatabaseHelper(getContext());
        com.google.android.material.floatingactionbutton.FloatingActionButton fab = view.findViewById(R.id.fabNuevaActividad);
        fab.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), CrearActividadActivity.class));
        });
        cargarActividades();

        return view;
    }

    private void cargarActividades() {
        // Consultar SQLite
        List<Map<String, String>> lista = dbHelper.getAllActividades();

        if (lista.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            rvActividades.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            rvActividades.setVisibility(View.VISIBLE);

            // Configurar Adapter
            // DENTRO DEL ADAPTER ON CLICK
            ActividadesAdapter adapter = new ActividadesAdapter(lista, actividad -> {

                // CAMBIO DE RUTA: Ahora vamos a ParticipantesActivity
                Intent intent = new Intent(getContext(), ParticipantesActivity.class); // <--- CAMBIO AQUÍ
                intent.putExtra("ID_ACTIVIDAD", actividad.get("server_id"));
                intent.putExtra("NOMBRE_ACTIVIDAD", actividad.get("nombre"));
                startActivity(intent);

            });

            rvActividades.setAdapter(adapter);
        }
    }

    // Recargar la lista si volvemos a esta pantalla
    @Override
    public void onResume() {
        super.onResume();
        cargarActividades();
    }
}