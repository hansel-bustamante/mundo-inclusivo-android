package com.example.mundoinclusivo.utils;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mundoinclusivo.database.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SyncManager {

    private Context context;
    private DatabaseHelper dbHelper;
    private String token;
    // RECUERDA CAMBIAR LA IP
    private String URL_DOWNLOAD = "http://10.0.2.2:8000/api/sync/download";
    // URL PARA SUBIR
    private String URL_UPLOAD = "http://10.0.2.2:8000/api/sync/upload";

    public SyncManager(Context context, String token) {
        this.context = context;
        this.dbHelper = new DatabaseHelper(context);
        this.token = token;
    }

    public void downloadData() {
        Toast.makeText(context, "Iniciando descarga...", Toast.LENGTH_SHORT).show();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL_DOWNLOAD, null,
                response -> {
                    try {
                        boolean status = response.getBoolean("status");
                        if (status) {
                            JSONObject data = response.getJSONObject("data");

                            List<ContentValues> listPersonas = parsePersonas(data.getJSONArray("personas"));
                            List<ContentValues> listActividades = parseActividades(data.getJSONArray("actividades"));
                            List<ContentValues> listSesiones = parseSesiones(data.getJSONArray("sesiones"));
                            List<ContentValues> listCodigos = parseCodigos(data.getJSONArray("codigos"));
                            List<ContentValues> listInstituciones = parseInstituciones(data.getJSONArray("instituciones"));

                            // 1. NUEVO: Parsear participa_en
                            List<ContentValues> listParticipaEn = parseParticipaEn(data.getJSONArray("participa_en"));

                            // 1. NUEVO PARSER
                            List<ContentValues> listAsistencias = parseAsistencias(data.getJSONArray("asistencias"));
                            JSONArray jsonCis = data.getJSONArray("global_cis");
                            List<String> listGlobalCis = new ArrayList<>();
                            for(int i=0; i<jsonCis.length(); i++) {
                                listGlobalCis.add(jsonCis.getString(i));
                            }
                            // 2. PASAR AL HELPER ACTUALIZADO
                            dbHelper.saveSyncData(listPersonas, listActividades, listSesiones, listCodigos, listInstituciones, listParticipaEn, listAsistencias, listGlobalCis);

                            Toast.makeText(context, "¡Sincronización Completada!", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Log.e("SyncError", "Error parsing: " + e.getMessage());
                        Toast.makeText(context, "Error procesando datos", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    String err = (error.getMessage() != null) ? error.getMessage() : "Error de conexión";
                    Toast.makeText(context, "Fallo descarga: " + err, Toast.LENGTH_SHORT).show();
                    Log.e("SyncError", err);
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                // Enviamos el Token para que Laravel sepa quiénes somos
                params.put("Authorization", "Bearer " + token);
                params.put("Accept", "application/json");
                return params;
            }
        };

        // Aumentar tiempo de espera (la descarga puede ser pesada)
        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(context).add(request);
    }
    // MÉTODO MAESTRO: El botón llama a este
    public void syncData() {
        // Primero intentamos subir
        uploadData();
    }

    private void uploadData() {
        // 1. Recolectar datos
        List<Map<String, String>> newPersonas = dbHelper.getNewPersonas();
        List<Map<String, Object>> newParticipa = dbHelper.getNewParticipaEn();
        List<Map<String, Object>> newAsistencias = dbHelper.getNewAsistencias();
        List<Map<String, Object>> newSesiones = dbHelper.getNewSesiones(); // <--- NUEVO
// A. OBTENER ACTIVIDADES NUEVAS
        List<Map<String, Object>> newActividades = dbHelper.getNewActividades(); // <--- NUEVO

        // Verificar si hay algo que subir (incluyendo actividades)
        if (newPersonas.isEmpty() && newParticipa.isEmpty() && newAsistencias.isEmpty() && newSesiones.isEmpty() && newActividades.isEmpty()) {
            Toast.makeText(context, "Nada pendiente. Buscando actualizaciones...", Toast.LENGTH_SHORT).show();
            downloadData();
            return;
        }

        Toast.makeText(context, "Subiendo datos...", Toast.LENGTH_SHORT).show();

        JSONObject payload = new JSONObject();
        try {
            payload.put("personas", new JSONArray(newPersonas));
            payload.put("participa_en", new JSONArray(newParticipa));
            payload.put("asistencias", new JSONArray(newAsistencias));
            payload.put("sesiones", new JSONArray(newSesiones)); // <--- NUEVO
            payload.put("actividades", new JSONArray(newActividades));
        } catch (Exception e) { e.printStackTrace(); }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL_UPLOAD, payload,
                response -> {
                    try {
                        if (response.getBoolean("status")) {
                            // A. Mapeo Personas
                            JSONObject mapPersonas = response.optJSONObject("id_mapping");
                            if (mapPersonas != null) {
                                JSONArray ids = mapPersonas.names();
                                if (ids != null) {
                                    for (int i = 0; i < ids.length(); i++) {
                                        String local = ids.getString(i);
                                        int server = mapPersonas.getInt(local);
                                        dbHelper.updatePersonIds(local, server);
                                    }
                                }
                            }

                            // B. Mapeo Sesiones (NUEVO E IMPORTANTE)
                            JSONObject mapSesiones = response.optJSONObject("id_mapping_sesiones");
                            if (mapSesiones != null) {
                                JSONArray ids = mapSesiones.names();
                                if (ids != null) {
                                    for (int i = 0; i < ids.length(); i++) {
                                        String local = ids.getString(i);
                                        int server = mapSesiones.getInt(local);
                                        dbHelper.updateSessionIds(local, server);
                                    }
                                }
                            }

                            // C. Mapeo Actividades (NUEVO)
                            JSONObject mapActividades = response.optJSONObject("id_mapping_actividades");
                            if (mapActividades != null) {
                                JSONArray ids = mapActividades.names();
                                if (ids != null) {
                                    for (int i = 0; i < ids.length(); i++) {
                                        String local = ids.getString(i);
                                        int server = mapActividades.getInt(local);
                                        dbHelper.updateActividadIds(local, server);
                                    }
                                }
                            }

                            dbHelper.markAllAsSynced();
                            Toast.makeText(context, "Subida exitosa. Actualizando...", Toast.LENGTH_SHORT).show();
                            downloadData();
                        }
                    } catch (Exception e) { Log.e("Sync", "Error: " + e.getMessage()); }
                },
                error -> { Toast.makeText(context, "Error al subir", Toast.LENGTH_SHORT).show(); }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + token);
                return params;
            }
        };
        Volley.newRequestQueue(context).add(request);
    }

    // --- PARSERS (Ayudantes para convertir JSON a SQLite) ---

    private List<ContentValues> parsePersonas(JSONArray jsonArray) throws Exception {
        List<ContentValues> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            ContentValues cv = new ContentValues();
            cv.put(DatabaseHelper.COL_PER_SERVER_ID, obj.getInt("id_persona")); // ID Real
            cv.put(DatabaseHelper.COL_PER_NOMBRE, obj.getString("nombre"));
            cv.put(DatabaseHelper.COL_PER_PATERNO, obj.getString("apellido_paterno"));
            cv.put(DatabaseHelper.COL_PER_MATERNO, obj.optString("apellido_materno", ""));
            cv.put(DatabaseHelper.COL_PER_CI, obj.getString("carnet_identidad"));
            cv.put(DatabaseHelper.COL_PER_FECHA, obj.getString("fecha_nacimiento"));
            cv.put(DatabaseHelper.COL_PER_GENERO, obj.getString("genero"));
            // cv.put(DatabaseHelper.COL_PER_CELULAR, ...); // Agregar si viene del server
            cv.put(DatabaseHelper.COL_PER_AREA, obj.getString("area_intervencion_id"));
            cv.put(DatabaseHelper.COL_IS_NEW, 0); // Viene del server, no es nuevo local
            list.add(cv);
        }
        return list;
    }

    private List<ContentValues> parseActividades(JSONArray jsonArray) throws Exception {
        List<ContentValues> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            ContentValues cv = new ContentValues();
            cv.put(DatabaseHelper.COL_ACT_SERVER_ID, obj.getInt("id_actividad"));
            cv.put(DatabaseHelper.COL_ACT_NOMBRE, obj.getString("nombre"));
            cv.put(DatabaseHelper.COL_ACT_FECHA, obj.getString("fecha"));
            cv.put(DatabaseHelper.COL_ACT_LUGAR, obj.getString("lugar"));
            cv.put(DatabaseHelper.COL_ACT_CODIGO, obj.getString("codigo_actividad_id"));
            cv.put(DatabaseHelper.COL_ACT_AREA, obj.getString("area_intervencion_id"));
            cv.put(DatabaseHelper.COL_ACT_IS_NEW, 0);
            list.add(cv);
        }
        return list;
    }

    // ... Puedes implementar parseSesiones, parseCodigos, parseInstituciones de forma similar ...
    // Aquí te dejo el de Codigos como ejemplo rápido:
    private List<ContentValues> parseCodigos(JSONArray jsonArray) throws Exception {
        List<ContentValues> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            ContentValues cv = new ContentValues();
            cv.put(DatabaseHelper.COL_COD_ID, obj.getString("codigo_actividad"));
            cv.put(DatabaseHelper.COL_COD_NOMBRE, obj.getString("nombre_actividad"));
            list.add(cv);
        }
        return list;
    }

    private List<ContentValues> parseInstituciones(JSONArray jsonArray) throws Exception {
        List<ContentValues> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            ContentValues cv = new ContentValues();
            cv.put(DatabaseHelper.COL_INST_ID, obj.getInt("id_institucion"));
            cv.put(DatabaseHelper.COL_INST_NOMBRE, obj.getString("nombre_institucion"));
            list.add(cv);
        }
        return list;
    }

    private List<ContentValues> parseSesiones(JSONArray jsonArray) throws Exception {
        List<ContentValues> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            ContentValues cv = new ContentValues();
            cv.put(DatabaseHelper.COL_SES_ID, obj.getInt("id_sesion"));
            cv.put(DatabaseHelper.COL_SES_ACT, obj.getInt("id_actividad"));
            cv.put(DatabaseHelper.COL_SES_TEMA, obj.getString("tema"));
            cv.put(DatabaseHelper.COL_SES_FECHA, obj.getString("fecha"));
            list.add(cv);
        }
        return list;
    }

    // 3. NUEVO MÉTODO PARSER
    private List<ContentValues> parseParticipaEn(JSONArray jsonArray) throws Exception {
        List<ContentValues> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            ContentValues cv = new ContentValues();
            cv.put(DatabaseHelper.COL_PE_PERSONA, obj.getInt("id_persona"));
            cv.put(DatabaseHelper.COL_PE_ACTIVIDAD, obj.getInt("id_actividad"));
            cv.put(DatabaseHelper.COL_PE_DISC, obj.getInt("tiene_discapacidad"));
            cv.put(DatabaseHelper.COL_PE_FAM, obj.getInt("es_familiar"));
            cv.put(DatabaseHelper.COL_PE_FIRMA, obj.getInt("firma"));
            cv.put(DatabaseHelper.COL_PE_IS_NEW, 0); // Viene del server
            list.add(cv);
        }
        return list;
    }

    private List<ContentValues> parseAsistencias(JSONArray jsonArray) throws Exception {
        List<ContentValues> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            ContentValues cv = new ContentValues();
            cv.put(DatabaseHelper.COL_ASIS_SESION, obj.getInt("id_sesion"));
            cv.put(DatabaseHelper.COL_ASIS_PERSONA, obj.getInt("id_persona"));
            cv.put(DatabaseHelper.COL_ASIS_FIRMA, obj.getInt("firma"));
            cv.put(DatabaseHelper.COL_ASIS_OBS, obj.optString("observaciones", ""));
            cv.put(DatabaseHelper.COL_ASIS_IS_NEW, 0); // Viene del server
            list.add(cv);
        }
        return list;
    }
}