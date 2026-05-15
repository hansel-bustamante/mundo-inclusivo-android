package com.example.mundoinclusivo;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.mundoinclusivo.database.DatabaseHelper;

public class HomeFragment extends Fragment {

    private TextView tvNombre, tvRol, tvStatusText, tvCountPersonas, tvCountAsistencias;
    private ImageView imgStatusIcon;
    private Button btnCheck;
    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Inicializar Vistas
        tvNombre = view.findViewById(R.id.tvNombreUsuario);
        tvRol = view.findViewById(R.id.tvRolUsuario);
        tvStatusText = view.findViewById(R.id.tvStatusText);
        imgStatusIcon = view.findViewById(R.id.imgStatusIcon);
        btnCheck = view.findViewById(R.id.btnCheckConnection);
        tvCountPersonas = view.findViewById(R.id.tvCountPersonas);
        tvCountAsistencias = view.findViewById(R.id.tvCountAsistencias);

        dbHelper = new DatabaseHelper(getContext());

        // 1. Cargar Datos del Usuario
        cargarUsuario();

        // 2. Verificar Conexión Inicial
        verificarConexion();

        // 3. Acción Botón Verificar
        btnCheck.setOnClickListener(v -> verificarConexion());

        // 4. Cargar Estadísticas
        cargarEstadisticas();

        return view;
    }

    private void cargarUsuario() {
        SharedPreferences prefs = getActivity().getSharedPreferences("sesion", Context.MODE_PRIVATE);
        String nombre = prefs.getString("nombre_usuario", "Usuario"); // Asegúrate de guardar 'nombre_usuario' en Login

        // Si no guardaste el nombre bonito, usa el token o pon "Registrador"
        if(nombre.equals("Usuario")) nombre = "Registrador";

        tvNombre.setText(nombre);
        tvRol.setText("Registrador de Campo");
    }

    private void verificarConexion() {
        if (isConnected()) {
            tvStatusText.setText("Online");
            tvStatusText.setTextColor(0xFF4CAF50); // Verde
            imgStatusIcon.setImageResource(R.drawable.ic_wifi_on); // Asegúrate de tener este icono
            imgStatusIcon.setColorFilter(0xFF4CAF50);
        } else {
            tvStatusText.setText("Offline");
            tvStatusText.setTextColor(0xFFF44336); // Rojo
            imgStatusIcon.setImageResource(R.drawable.ic_wifi_off); // Asegúrate de tener este icono
            imgStatusIcon.setColorFilter(0xFFF44336);
        }
    }

    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }
        return false;
    }

    private void cargarEstadisticas() {
        // Consultamos a la BD cuántos nuevos hay
        int personasNuevas = dbHelper.getNewPersonas().size();
        int asistenciasNuevas = dbHelper.getNewAsistencias().size(); // + getNewParticipaEn().size() si quieres sumar todo

        tvCountPersonas.setText(String.valueOf(personasNuevas));
        tvCountAsistencias.setText(String.valueOf(asistenciasNuevas));
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarEstadisticas(); // Recargar al volver a esta pantalla
        verificarConexion();
    }
}