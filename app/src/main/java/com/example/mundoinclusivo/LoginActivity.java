package com.example.mundoinclusivo;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log; // Para logs robustos
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    EditText etUsuario, etPassword;
    Button btnLogin;

    // CORRECCIÓN IP: Usamos 10.0.2.2 para el emulador
    String URL_LOGIN = "http://10.0.2.2:8000/api/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkSession(); // Verificar si ya entró antes

        setContentView(R.layout.activity_login);

        etUsuario = findViewById(R.id.etUsuario);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        // CORRECCIÓN: Lambda simplificada (Sugerencia de Android Studio)
        btnLogin.setOnClickListener(v -> {
            String user = etUsuario.getText().toString().trim();
            String pass = etPassword.getText().toString().trim();

            if (!user.isEmpty() && !pass.isEmpty()) {
                login(user, pass);
            } else {
                Toast.makeText(this, "Llene los campos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void login(String user, String pass) {
        JSONObject params = new JSONObject();
        try {
            // IMPORTANTE: Estas claves deben ser IGUALES a las que pide Laravel
            params.put("nombre_usuario", user);
            params.put("contrasena", pass); // Asegúrate que en Laravel sea 'contrasena' o 'password' según tu controlador
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Mostrar cargando...
        Toast.makeText(this, "Conectando...", Toast.LENGTH_SHORT).show();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL_LOGIN, params,
                response -> {
                    try {
                        // Si Laravel responde éxito (200 OK)
                        boolean status = response.getBoolean("status");
                        if (status) {
                            String token = response.getString("token");

                            // Manejo seguro del área
                            String area = "ADMIN";
                            if (response.has("area_id") && !response.isNull("area_id")) {
                                area = response.getString("area_id");
                            }
                            // --- CORRECCIÓN AQUÍ ---
                            // 1. Obtenemos el nombre que nos manda Laravel (la clave es 'usuario')
                            String nombreUser = response.getString("usuario");

                            // 2. Ahora sí pasamos los 3 argumentos: token, area, nombre
                            saveSession(token, area, nombreUser);

                            Toast.makeText(this, "¡Bienvenido!", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(this, MainActivity.class));
                            finish();
                        } else {
                            // Laravel respondió pero dijo que no (ej. pass incorrecto)
                            String msg = response.has("message") ? response.getString("message") : "Error desconocido";
                            Toast.makeText(this, "Server: " + msg, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        Log.e("LoginError", "Error JSON: " + e.getMessage());
                        Toast.makeText(this, "Error procesando respuesta", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    // --- AQUÍ ESTÁ LA MAGIA DEL DEBUG ---
                    String mensajeError = "Error de conexión genérico";

                    if (error.networkResponse != null) {
                        try {
                            // Obtener el cuerpo del error que manda Laravel (HTML o JSON)
                            String body = new String(error.networkResponse.data, "UTF-8");
                            Log.e("LARAVEL_ERROR", body); // Míralo en el Logcat de Android Studio

                            // Si es error 401 (No autorizado)
                            if (error.networkResponse.statusCode == 401) {
                                mensajeError = "Usuario o contraseña incorrectos";
                            }
                            // Si es error 500 (Error de código en Laravel)
                            else if (error.networkResponse.statusCode == 500) {
                                mensajeError = "Error 500 en Laravel (Revisa Logcat)";
                            }
                            // Si es error 422 (Validación)
                            else if (error.networkResponse.statusCode == 422) {
                                mensajeError = "Faltan datos (422): " + body;
                            }
                            else {
                                mensajeError = "Error " + error.networkResponse.statusCode;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    Toast.makeText(this, mensajeError, Toast.LENGTH_LONG).show();
                }
        );

        Volley.newRequestQueue(this).add(request);
    }

    private void saveSession(String token, String area, String nombreUsuario) { // <--- 1. AGREGAR ARGUMENTO AQUÍ
        SharedPreferences preferences = getSharedPreferences("sesion", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("token", token);
        editor.putString("area", area);
        editor.putString("nombre_usuario", nombreUsuario); // <--- 2. GUARDARLO AQUÍ
        editor.putBoolean("isLogged", true);
        editor.apply();
    }

    private void checkSession() {
        SharedPreferences preferences = getSharedPreferences("sesion", MODE_PRIVATE);
        if (preferences.getBoolean("isLogged", false)) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
}