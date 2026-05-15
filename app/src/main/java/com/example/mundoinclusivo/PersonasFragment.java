package com.example.mundoinclusivo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mundoinclusivo.adapters.PersonasAdapter;
import com.example.mundoinclusivo.database.DatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;
import java.util.Map;

public class PersonasFragment extends Fragment {

    private RecyclerView rvPersonas;
    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personas, container, false);

        rvPersonas = view.findViewById(R.id.rvPersonas);
        rvPersonas.setLayoutManager(new LinearLayoutManager(getContext()));

        FloatingActionButton fab = view.findViewById(R.id.fabAgregarPersona);

        dbHelper = new DatabaseHelper(getContext());

        // Acción del botón flotante: Ir al formulario
        fab.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), AgregarPersonaActivity.class));
        });

        cargarLista();

        return view;
    }

    private void cargarLista() {
        List<Map<String, String>> lista = dbHelper.getAllPersonas();
        PersonasAdapter adapter = new PersonasAdapter(lista);
        rvPersonas.setAdapter(adapter);
    }

    // Al volver del formulario, recargamos la lista para ver al nuevo arriba
    @Override
    public void onResume() {
        super.onResume();
        cargarLista();
    }
}