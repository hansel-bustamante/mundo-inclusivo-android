package com.example.mundoinclusivo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mundoinclusivo.utils.SyncManager;

public class SyncFragment extends Fragment {

    private Button btnSync;
    private ProgressBar progressBar;
    private TextView tvStatus;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sync, container, false);

        btnSync = view.findViewById(R.id.btnSync);
        progressBar = view.findViewById(R.id.progressBarSync);
        tvStatus = view.findViewById(R.id.tvSyncStatus);

        // Inicialmente ocultamos el progreso
        progressBar.setVisibility(View.GONE);
        tvStatus.setText("");

        btnSync.setOnClickListener(v -> iniciarSincronizacion());

        return view;
    }

    private void iniciarSincronizacion() {
        // 1. Obtener Token
        SharedPreferences prefs = getActivity().getSharedPreferences("sesion", Context.MODE_PRIVATE);
        String token = prefs.getString("token", "");

        if (token.isEmpty()) {
            Toast.makeText(getContext(), "Error: No hay sesión activa", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. Cambiar UI a "Cargando"
        btnSync.setEnabled(false);
        btnSync.setText("Sincronizando...");
        progressBar.setVisibility(View.VISIBLE);
        tvStatus.setText("Conectando con el servidor...");

        // 3. Ejecutar SyncManager
        SyncManager syncManager = new SyncManager(getContext(), token);

        // --- CAMBIO CLAVE AQUÍ ---
        // Usamos syncData() en lugar de downloadData() para que haga Upload -> Download
        syncManager.syncData();

        // 4. Restaurar UI (Simulación)
        // Como Volley es asíncrono, lo ideal sería usar una Interface (Callback),
        // pero este delay de 4 segundos sirve para dar feedback visual al usuario mientras el proceso corre en fondo.
        new android.os.Handler().postDelayed(() -> {
            if (isAdded()) { // Verificar que el fragmento siga vivo para no causar crash
                btnSync.setEnabled(true);
                btnSync.setText("Sincronizar Ahora");
                progressBar.setVisibility(View.GONE);
                tvStatus.setText("Proceso iniciado (Verifique mensajes en pantalla)");
            }
        }, 4000);
    }
}