package com.example.mundoinclusivo.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "MundoInclusivoCompleto.db";
    private static final int DB_VERSION = 2;

    // ==========================================
    // 1. TABLA USUARIO
    // ==========================================
    public static final String TABLE_USUARIO = "usuario";
    public static final String COL_USR_ID = "id_persona";
    public static final String COL_USR_USER = "nombre_usuario";
    public static final String COL_USR_PASS = "contrasena";
    public static final String COL_USR_ROL = "rol";
    public static final String COL_USR_AREA = "area_intervencion_id";

    // ==========================================
    // 2. TABLA CODIGO_ACTIVIDAD
    // ==========================================
    public static final String TABLE_CODIGO_ACT = "codigo_actividad";
    public static final String COL_COD_ID = "codigo_actividad";
    public static final String COL_COD_NOMBRE = "nombre_actividad";

    // ==========================================
    // 3. TABLA INSTITUCION
    // ==========================================
    public static final String TABLE_INSTITUCION = "institucion";
    public static final String COL_INST_ID = "id_institucion";
    public static final String COL_INST_NOMBRE = "nombre_institucion";

    // ==========================================
    // 4. TABLA PERSONA (CORREGIDO)
    // ==========================================
    public static final String TABLE_PERSONA = "persona";
    public static final String COL_PER_ID = "id_persona";       // PK Local
    public static final String COL_PER_SERVER_ID = "server_id"; // ID real Laravel
    public static final String COL_PER_NOMBRE = "nombre";
    public static final String COL_PER_PATERNO = "apellido_paterno";
    public static final String COL_PER_MATERNO = "apellido_materno";
    public static final String COL_PER_CI = "carnet_identidad";
    public static final String COL_PER_FECHA = "fecha_nacimiento";
    public static final String COL_PER_GENERO = "genero";
    public static final String COL_PER_CELULAR = "celular";         // <--- FALTABA
    public static final String COL_PER_PROCEDENCIA = "procedencia"; // <--- FALTABA
    public static final String COL_PER_AREA = "area_intervencion_id";
    public static final String COL_IS_NEW = "is_new";

    // ==========================================
    // 5. TABLA ACTIVIDAD
    // ==========================================
    public static final String TABLE_ACTIVIDAD = "actividad";
    public static final String COL_ACT_ID = "id_actividad";
    public static final String COL_ACT_SERVER_ID = "server_id";
    public static final String COL_ACT_NOMBRE = "nombre";
    public static final String COL_ACT_FECHA = "fecha";
    public static final String COL_ACT_LUGAR = "lugar";
    public static final String COL_ACT_DESC = "descripcion";
    public static final String COL_ACT_CODIGO = "codigo_actividad_id";
    public static final String COL_ACT_AREA = "area_intervencion_id";
    public static final String COL_ACT_IS_NEW = "is_new";

    // ==========================================
    // 6. TABLA PARTICIPANTE
    // ==========================================
    public static final String TABLE_PARTICIPANTE = "participante";
    public static final String COL_PART_PERSONA = "id_persona";
    public static final String COL_PART_INST = "id_institucion";

    // ==========================================
    // 7. TABLA PARTICIPA_EN
    // ==========================================
    public static final String TABLE_PARTICIPA_EN = "participa_en";
    public static final String COL_PE_PERSONA = "id_persona";
    public static final String COL_PE_ACTIVIDAD = "id_actividad";
    public static final String COL_PE_DISC = "tiene_discapacidad";
    public static final String COL_PE_FAM = "es_familiar";
    public static final String COL_PE_FIRMA = "firma";
    public static final String COL_PE_IS_NEW = "is_new";

    // ==========================================
    // 8. TABLA SESION
    // ==========================================
    public static final String TABLE_SESION = "sesion";
    public static final String COL_SES_ID = "id_sesion";
    public static final String COL_SES_ACT = "id_actividad";
    public static final String COL_SES_TEMA = "tema";
    public static final String COL_SES_FECHA = "fecha";

    // ==========================================
    // 9. TABLA ASISTENCIA_SESION
    // ==========================================
    public static final String TABLE_ASISTENCIA = "asistencia_sesion";
    public static final String COL_ASIS_SESION = "id_sesion";
    public static final String COL_ASIS_PERSONA = "id_persona";
    public static final String COL_ASIS_FIRMA = "firma";
    public static final String COL_ASIS_OBS = "observaciones";
    public static final String COL_ASIS_IS_NEW = "is_new";
    // ... Al inicio de la clase ...
    // TABLA PARA VALIDACIÓN GLOBAL DE CI
    public static final String TABLE_VALIDACION_CI = "validacion_ci";
    public static final String COL_VAL_CI = "ci"; // Única columna

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 1. Usuario
        db.execSQL("CREATE TABLE " + TABLE_USUARIO + " (" +
                COL_USR_ID + " INTEGER PRIMARY KEY, " +
                COL_USR_USER + " TEXT, " +
                COL_USR_PASS + " TEXT, " +
                COL_USR_ROL + " TEXT, " +
                COL_USR_AREA + " TEXT)");

        // 2. Códigos
        db.execSQL("CREATE TABLE " + TABLE_CODIGO_ACT + " (" +
                COL_COD_ID + " TEXT PRIMARY KEY, " +
                COL_COD_NOMBRE + " TEXT)");

        // 3. Institución
        db.execSQL("CREATE TABLE " + TABLE_INSTITUCION + " (" +
                COL_INST_ID + " INTEGER PRIMARY KEY, " +
                COL_INST_NOMBRE + " TEXT)");

        // 4. Persona (USANDO LAS CONSTANTES CORRECTAS)
        db.execSQL("CREATE TABLE " + TABLE_PERSONA + " (" +
                COL_PER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_PER_SERVER_ID + " INTEGER, " +
                COL_PER_NOMBRE + " TEXT, " +
                COL_PER_PATERNO + " TEXT, " +
                COL_PER_MATERNO + " TEXT, " +
                COL_PER_CI + " TEXT, " +
                COL_PER_FECHA + " TEXT, " +
                COL_PER_GENERO + " TEXT, " +
                COL_PER_CELULAR + " TEXT, " +      // Corrección
                COL_PER_PROCEDENCIA + " TEXT, " +  // Corrección
                COL_PER_AREA + " TEXT, " +
                COL_IS_NEW + " INTEGER DEFAULT 0)");

        // 5. Actividad
        db.execSQL("CREATE TABLE " + TABLE_ACTIVIDAD + " (" +
                COL_ACT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_ACT_SERVER_ID + " INTEGER, " +
                COL_ACT_NOMBRE + " TEXT, " +
                COL_ACT_FECHA + " TEXT, " +
                COL_ACT_LUGAR + " TEXT, " +
                COL_ACT_DESC + " TEXT, " +
                COL_ACT_CODIGO + " TEXT, " +
                COL_ACT_AREA + " TEXT, " +
                COL_ACT_IS_NEW + " INTEGER DEFAULT 0)");

        // 6. Participante
        db.execSQL("CREATE TABLE " + TABLE_PARTICIPANTE + " (" +
                COL_PART_PERSONA + " INTEGER, " +
                COL_PART_INST + " INTEGER, " +
                "PRIMARY KEY (" + COL_PART_PERSONA + "))");

        // 7. Participa_En
        db.execSQL("CREATE TABLE " + TABLE_PARTICIPA_EN + " (" +
                COL_PE_PERSONA + " INTEGER, " +
                COL_PE_ACTIVIDAD + " INTEGER, " +
                COL_PE_DISC + " INTEGER, " +
                COL_PE_FAM + " INTEGER, " +
                COL_PE_FIRMA + " INTEGER, " +
                COL_PE_IS_NEW + " INTEGER DEFAULT 0, " +
                "PRIMARY KEY (" + COL_PE_PERSONA + ", " + COL_PE_ACTIVIDAD + "))");

        // 8. Sesión
        // AGREGAMOS 'is_new' A LA TABLA SESIÓN
        String createSesiones = "CREATE TABLE " + TABLE_SESION + " (" +
                COL_SES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + // ID local
                "server_id INTEGER, " + // ID real del server
                COL_SES_ACT + " INTEGER, " +
                COL_SES_TEMA + " TEXT, " +
                COL_SES_FECHA + " TEXT, " +
                "is_new INTEGER DEFAULT 0)"; // 1 = Creada offline
        db.execSQL(createSesiones);

        // 9. Asistencia
        db.execSQL("CREATE TABLE " + TABLE_ASISTENCIA + " (" +
                COL_ASIS_SESION + " INTEGER, " +
                COL_ASIS_PERSONA + " INTEGER, " +
                COL_ASIS_FIRMA + " INTEGER, " +
                COL_ASIS_OBS + " TEXT, " +
                COL_ASIS_IS_NEW + " INTEGER DEFAULT 0, " +
                "PRIMARY KEY (" + COL_ASIS_SESION + ", " + COL_ASIS_PERSONA + "))");

        db.execSQL("CREATE TABLE " + TABLE_VALIDACION_CI + " (" + COL_VAL_CI + " TEXT PRIMARY KEY)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ASISTENCIA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SESION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARTICIPA_EN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARTICIPANTE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTIVIDAD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERSONA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INSTITUCION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CODIGO_ACT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VALIDACION_CI);
        onCreate(db);
    }

    // ==========================================
    // MÉTODOS CRUD CORREGIDOS
    // ==========================================
    // VERIFICAR SI EXISTE PERSONA POR CI
    // VERIFICAR DUPLICADOS (Versión Robusta)
    // VALIDACIÓN ROBUSTA (Busca en TODO el sistema)
    public boolean existePersonaPorCI(String ci) {
        SQLiteDatabase db = this.getReadableDatabase();
        String ciLimpio = ci.trim();

        // 1. Buscamos en la tabla de Personas Local (por si acabamos de crear uno offline y no hemos sincronizado)
        Cursor cLocal = db.rawQuery("SELECT count(*) FROM " + TABLE_PERSONA + " WHERE " + COL_PER_CI + " = ?", new String[]{ciLimpio});
        int countLocal = 0;
        if (cLocal.moveToFirst()) countLocal = cLocal.getInt(0);
        cLocal.close();

        // 2. Buscamos en la Lista Global (Descargada del servidor)
        Cursor cGlobal = db.rawQuery("SELECT count(*) FROM " + TABLE_VALIDACION_CI + " WHERE " + COL_VAL_CI + " = ?", new String[]{ciLimpio});
        int countGlobal = 0;
        if (cGlobal.moveToFirst()) countGlobal = cGlobal.getInt(0);
        cGlobal.close();

        // Si existe en cualquiera de las dos, es duplicado
        return (countLocal > 0 || countGlobal > 0);
    }

    public boolean addPersonaLocal(String nombre, String paterno, String materno, String ci,
                                   String fechaNac, String genero, String celular, String procedencia, String areaId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        // CORRECCIÓN: Usamos las constantes COL_PER_...
        cv.put(COL_PER_NOMBRE, nombre);
        cv.put(COL_PER_PATERNO, paterno);
        cv.put(COL_PER_MATERNO, materno);
        cv.put(COL_PER_CI, ci);
        cv.put(COL_PER_FECHA, fechaNac);
        cv.put(COL_PER_GENERO, genero);
        cv.put(COL_PER_CELULAR, celular);
        cv.put(COL_PER_PROCEDENCIA, procedencia);
        cv.put(COL_PER_AREA, areaId);
        cv.put(COL_IS_NEW, 1); // 1 = Nuevo, pendiente de subir

        long result = db.insert(TABLE_PERSONA, null, cv);
        return result != -1;
    }

    @SuppressLint("Range")
    public List<Map<String, String>> getAllPersonas() {
        List<Map<String, String>> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // CORRECCIÓN: Cambiamos COL_LOCAL_ID por COL_PER_ID
        String query = "SELECT * FROM " + TABLE_PERSONA +
                " ORDER BY " + COL_IS_NEW + " DESC, " + COL_PER_ID + " DESC";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Map<String, String> persona = new HashMap<>();
                String nombre = cursor.getString(cursor.getColumnIndex(COL_PER_NOMBRE));
                String paterno = cursor.getString(cursor.getColumnIndex(COL_PER_PATERNO));
                String ci = cursor.getString(cursor.getColumnIndex(COL_PER_CI));
                int isNew = cursor.getInt(cursor.getColumnIndex(COL_IS_NEW));

                // Indicador visual
                String nombreCompleto = nombre + " " + paterno;
                if (isNew == 1) {
                    nombreCompleto = "★ " + nombreCompleto;
                }

                persona.put("nombre_completo", nombreCompleto);
                persona.put("ci", ci);
                lista.add(persona);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }

    // Método para el SyncManager (Subida)
    public Cursor getUnsyncedPersonas() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PERSONA + " WHERE " + COL_IS_NEW + " = 1", null);
    }

    // Método para marcar como sincronizado
    public void markAsSynced(int localId, int serverId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_IS_NEW, 0); // Ya no es nuevo
        cv.put(COL_PER_SERVER_ID, serverId); // Guardamos el ID que nos dio Laravel

        db.update(TABLE_PERSONA, cv, COL_PER_ID + "=?", new String[]{String.valueOf(localId)});
    }

    // MÉTODOS PARA SINCRONIZACIÓN (BAJADA)

    public void saveSyncData(List<ContentValues> personas,
                             List<ContentValues> actividades,
                             List<ContentValues> sesiones,
                             List<ContentValues> codigos,
                             List<ContentValues> instituciones,
                             List<ContentValues> participaEn,
                             List<ContentValues> asistencias,
                             List<String> globalCis) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            // 1. Limpieza de catálogos simples
            db.delete(TABLE_CODIGO_ACT, null, null);
            db.delete(TABLE_INSTITUCION, null, null);
            db.delete(TABLE_VALIDACION_CI, null, null);

            // 2. Limpieza de tablas dependientes (se regeneran completas desde el server)
            db.delete(TABLE_SESION, "is_new = 0", null);
            db.delete(TABLE_PERSONA, COL_IS_NEW + " = 0", null);
            db.delete(TABLE_PARTICIPA_EN, COL_PE_IS_NEW + " = 0", null);
            db.delete(TABLE_ASISTENCIA, COL_ASIS_IS_NEW + " = 0", null);

            // 3. --- LOGICA INTELIGENTE PARA ACTIVIDADES ---
            // Primero: Borramos actividades del server que YA NO vienen en la descarga (eliminadas en web)
            // Recolectamos los IDs que vienen del server
            StringBuilder idsServer = new StringBuilder();
            for (ContentValues cv : actividades) {
                if (idsServer.length() > 0) idsServer.append(",");
                idsServer.append(cv.getAsInteger("server_id")); // Asegúrate de usar la clave correcta del parser
            }
            if (idsServer.length() > 0) {
                // Borramos las que tienen server_id pero NO están en la lista nueva
                db.execSQL("DELETE FROM " + TABLE_ACTIVIDAD +
                        " WHERE " + COL_ACT_IS_NEW + "=0 AND " + COL_ACT_SERVER_ID + " NOT IN (" + idsServer + ")");
            }

            // Segundo: Insertamos o Fusionamos
            for (ContentValues cvServer : actividades) {
                int serverId = cvServer.getAsInteger(COL_ACT_SERVER_ID); // Ojo: key debe coincidir con tu parser
                String nombre = cvServer.getAsString(COL_ACT_NOMBRE);
                String fecha = cvServer.getAsString(COL_ACT_FECHA);

                // A. ¿Ya existe una actividad con este server_id? (Actualización normal)
                Cursor cId = db.rawQuery("SELECT " + COL_ACT_ID + " FROM " + TABLE_ACTIVIDAD + " WHERE " + COL_ACT_SERVER_ID + "=?", new String[]{String.valueOf(serverId)});
                boolean existsId = cId.moveToFirst();
                cId.close();

                if (existsId) {
                    db.update(TABLE_ACTIVIDAD, cvServer, COL_ACT_SERVER_ID + "=?", new String[]{String.valueOf(serverId)});
                } else {
                    // B. ¿Existe una LOCAL (is_new=1) con el mismo nombre y fecha? (CASO DUPLICADO)
                    // Si encontramos una, asumimos que es la misma que acabamos de subir.
                    Cursor cLocal = db.rawQuery("SELECT " + COL_ACT_ID + " FROM " + TABLE_ACTIVIDAD +
                                    " WHERE " + COL_ACT_NOMBRE + "=? AND " + COL_ACT_FECHA + "=? AND " + COL_ACT_IS_NEW + "=1",
                            new String[]{nombre, fecha});

                    if (cLocal.moveToFirst()) {
                        // ¡FUSIÓN! Actualizamos la local para que se convierta en la del servidor
                        int localId = cLocal.getInt(0);
                        // Importante: cvServer ya trae is_new=0 y el server_id correcto
                        db.update(TABLE_ACTIVIDAD, cvServer, COL_ACT_ID + "=?", new String[]{String.valueOf(localId)});
                    } else {
                        // C. No existe ni local ni remota -> Es nueva del servidor
                        db.insert(TABLE_ACTIVIDAD, null, cvServer);
                    }
                    cLocal.close();
                }
            }
            // ------------------------------------------------

            // 4. Inserciones masivas del resto (Igual que antes)
            for (ContentValues cv : codigos) db.insert(TABLE_CODIGO_ACT, null, cv);
            for (ContentValues cv : instituciones) db.insert(TABLE_INSTITUCION, null, cv);
            for (ContentValues cv : sesiones) db.insert(TABLE_SESION, null, cv);
            for (ContentValues cv : personas) db.insert(TABLE_PERSONA, null, cv);
            for (ContentValues cv : participaEn) db.insert(TABLE_PARTICIPA_EN, null, cv);
            for (ContentValues cv : asistencias) db.insert(TABLE_ASISTENCIA, null, cv);

            for (String ci : globalCis) {
                ContentValues cv = new ContentValues();
                cv.put(COL_VAL_CI, ci);
                db.insertWithOnConflict(TABLE_VALIDACION_CI, null, cv, SQLiteDatabase.CONFLICT_IGNORE);
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    // OBTENER LISTA DE ACTIVIDADES (Para el RecyclerView)
    @SuppressLint("Range")
    public List<Map<String, String>> getAllActividades() {
        List<Map<String, String>> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Consultamos la tabla ACTIVIDAD
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ACTIVIDAD + " ORDER BY " + COL_ACT_FECHA + " DESC", null);

        if (cursor.moveToFirst()) {
            do {
                Map<String, String> map = new HashMap<>();
                map.put("id", cursor.getString(cursor.getColumnIndex(COL_ACT_ID)));
                map.put("server_id", cursor.getString(cursor.getColumnIndex("server_id"))); // Importante para la asistencia
                map.put("nombre", cursor.getString(cursor.getColumnIndex(COL_ACT_NOMBRE)));
                map.put("fecha", cursor.getString(cursor.getColumnIndex(COL_ACT_FECHA)));
                map.put("lugar", cursor.getString(cursor.getColumnIndex(COL_ACT_LUGAR)));
                lista.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }

    // 1. OBTENER SESIONES POR ACTIVIDAD
    @SuppressLint("Range")
    public List<Map<String, String>> getSesionesPorActividad(String idActividad) {
        List<Map<String, String>> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Filtramos por ID de actividad
        String query = "SELECT * FROM " + TABLE_SESION +
                " WHERE " + COL_SES_ACT + " = ? ORDER BY " + COL_SES_FECHA + " ASC";

        Cursor cursor = db.rawQuery(query, new String[]{idActividad});

        if (cursor.moveToFirst()) {
            do {
                Map<String, String> map = new HashMap<>();
                map.put("id", cursor.getString(cursor.getColumnIndex(COL_SES_ID)));
                map.put("tema", cursor.getString(cursor.getColumnIndex(COL_SES_TEMA)));
                map.put("fecha", cursor.getString(cursor.getColumnIndex(COL_SES_FECHA)));
                lista.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }

    // 2. GUARDAR/ACTUALIZAR ASISTENCIA (Offline)
    public void guardarAsistenciaLocal(String idSesion, String idPersona, int firma, String obs) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_ASIS_SESION, idSesion);
        cv.put(COL_ASIS_PERSONA, idPersona);
        cv.put(COL_ASIS_FIRMA, firma);
        cv.put(COL_ASIS_OBS, obs);
        cv.put(COL_ASIS_IS_NEW, 1); // Pendiente de subir

        // Intentamos actualizar primero
        int rows = db.update(TABLE_ASISTENCIA, cv,
                COL_ASIS_SESION + "=? AND " + COL_ASIS_PERSONA + "=?",
                new String[]{idSesion, idPersona});

        // Si no existe, insertamos
        if (rows == 0) {
            db.insert(TABLE_ASISTENCIA, null, cv);
        }
    }

    // OBTENER LISTA PARA TOMAR ASISTENCIA
    @SuppressLint("Range")
    public List<Map<String, String>> getListaAsistencia(String idActividad, String idSesion) {
        List<Map<String, String>> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // SQL: Traeme todas las personas que participan en la Actividad X

        // CORRECCIÓN: TABLE_PARTICIPIPA_EN -> TABLE_PARTICIPA_EN
        String query = "SELECT p." + COL_PER_ID + ", p." + COL_PER_SERVER_ID + ", p." + COL_PER_NOMBRE + ", p." + COL_PER_PATERNO + ", p." + COL_PER_CI + ", " +
                "a." + COL_ASIS_FIRMA + " " +
                "FROM " + TABLE_PERSONA + " p " +
                "INNER JOIN " + TABLE_PARTICIPA_EN + " pe ON p." + COL_PER_SERVER_ID + " = pe." + COL_PE_PERSONA + " " + // <--- AQUI ESTABA EL ERROR
                "LEFT JOIN " + TABLE_ASISTENCIA + " a ON p." + COL_PER_SERVER_ID + " = a." + COL_ASIS_PERSONA + " AND a." + COL_ASIS_SESION + " = ? " +
                "WHERE pe." + COL_PE_ACTIVIDAD + " = ? " +
                "ORDER BY p." + COL_PER_PATERNO + " ASC";

        Cursor cursor = db.rawQuery(query, new String[]{idSesion, idActividad});

        if (cursor.moveToFirst()) {
            do {
                Map<String, String> map = new HashMap<>();
                // Usamos el server_id para vincular, pero si es nuevo local usamos el id local
                String idPersona = cursor.getString(cursor.getColumnIndex(COL_PER_SERVER_ID));
                if (idPersona == null || idPersona.equals("0")) {
                    idPersona = cursor.getString(cursor.getColumnIndex(COL_PER_ID)); // Fallback para creados offline
                }

                map.put("id_persona", idPersona);
                map.put("nombre_completo", cursor.getString(cursor.getColumnIndex(COL_PER_NOMBRE)) + " " + cursor.getString(cursor.getColumnIndex(COL_PER_PATERNO)));
                map.put("ci", cursor.getString(cursor.getColumnIndex(COL_PER_CI)));

                // Si a.firma es null (no hay registro), es 0. Si es 1, es 1.
                int firma = cursor.getInt(cursor.getColumnIndex(COL_ASIS_FIRMA));
                map.put("asistio", String.valueOf(firma));

                lista.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }

    // 1. OBTENER PARTICIPANTES (ORDENADOS: Recientes Arriba)
    @SuppressLint("Range")
    public List<Map<String, String>> getParticipantesPorActividad(String idActividad) {
        List<Map<String, String>> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // SQL TRUCO:
        // Usamos "pe.rowid" que es el ID oculto de SQLite que indica el orden de inserción.
        // Al ordenar DESC, los últimos agregados salen primero.

        String query = "SELECT nombre_completo, ci, is_new FROM (" +
                // Parte A: Personas sincronizadas (Server ID)
                "SELECT p." + COL_PER_NOMBRE + " || ' ' || p." + COL_PER_PATERNO + " as nombre_completo, " +
                "p." + COL_PER_CI + " as ci, " +
                "pe." + COL_PE_IS_NEW + " as is_new, " +
                "pe.rowid as r_id " + // Capturamos el rowid para ordenar
                "FROM " + TABLE_PERSONA + " p " +
                "INNER JOIN " + TABLE_PARTICIPA_EN + " pe ON p." + COL_PER_SERVER_ID + " = pe." + COL_PE_PERSONA +
                " WHERE pe." + COL_PE_ACTIVIDAD + " = ? " +

                " UNION " +

                // Parte B: Personas nuevas locales (Local ID)
                "SELECT p." + COL_PER_NOMBRE + " || ' ' || p." + COL_PER_PATERNO + " as nombre_completo, " +
                "p." + COL_PER_CI + " as ci, " +
                "pe." + COL_PE_IS_NEW + " as is_new, " +
                "pe.rowid as r_id " +
                "FROM " + TABLE_PERSONA + " p " +
                "INNER JOIN " + TABLE_PARTICIPA_EN + " pe ON p." + COL_PER_ID + " = pe." + COL_PE_PERSONA +
                " WHERE pe." + COL_PE_ACTIVIDAD + " = ? " +
                ") ORDER BY is_new DESC, r_id DESC"; // <--- AQUÍ ESTÁ LA MAGIA

        Cursor cursor = db.rawQuery(query, new String[]{idActividad, idActividad});

        if (cursor.moveToFirst()) {
            do {
                Map<String, String> map = new HashMap<>();
                map.put("nombre_completo", cursor.getString(cursor.getColumnIndex("nombre_completo")));
                map.put("ci", cursor.getString(cursor.getColumnIndex("ci")));

                // Opcional: Agregar un indicador visual si es nuevo
                if (cursor.getInt(cursor.getColumnIndex("is_new")) == 1) {
                    map.put("nombre_completo", "★ " + map.get("nombre_completo")); // Estrellita para los nuevos
                }

                lista.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }

    // 2. OBTENER PERSONAS *NO* INSCRITAS (Para agregarlas)
    @SuppressLint("Range")
    public List<Map<String, String>> getPersonasDisponibles(String idActividad) {
        List<Map<String, String>> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Traemos todas las personas
        String query = "SELECT * FROM " + TABLE_PERSONA + " ORDER BY " + COL_PER_PATERNO;
        Cursor cursor = db.rawQuery(query, null);

        // Filtramos en Java las que ya están (para simplificar la query SQLite compleja)
        // Nota: En apps grandes se hace con "NOT IN" en SQL, pero aquí está bien.
        // ... (Implementaremos la lógica de filtrado visual en el Dialog)

        if (cursor.moveToFirst()) {
            do {
                Map<String, String> map = new HashMap<>();
                // Guardamos ID (Server o Local)
                String serverId = cursor.getString(cursor.getColumnIndex(COL_PER_SERVER_ID));
                String localId = cursor.getString(cursor.getColumnIndex(COL_PER_ID));
                map.put("id", (serverId != null && !serverId.equals("0")) ? serverId : localId);

                map.put("nombre", cursor.getString(cursor.getColumnIndex(COL_PER_NOMBRE)) + " " + cursor.getString(cursor.getColumnIndex(COL_PER_PATERNO)));
                map.put("ci", cursor.getString(cursor.getColumnIndex(COL_PER_CI)));
                lista.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }

    // 3. VINCULAR PARTICIPANTE (Guardar en participa_en)
    public boolean vincularParticipanteLocal(String idPersona, String idActividad) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_PE_PERSONA, idPersona);
        cv.put(COL_PE_ACTIVIDAD, idActividad);
        cv.put(COL_PE_DISC, 0); // Default
        cv.put(COL_PE_FAM, 0);  // Default
        cv.put(COL_PE_FIRMA, 0);
        cv.put(COL_PE_IS_NEW, 1); // ¡Es nuevo local!

        long res = db.insert(TABLE_PARTICIPA_EN, null, cv);
        return res != -1;
    }

    // 4. CREAR NUEVA SESIÓN AUTOMÁTICA
    public boolean crearSesionAutomatica(String idActividad, int numeroSesion) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String fechaHoy = android.text.format.DateFormat.format("yyyy-MM-dd", new java.util.Date()).toString();

        cv.put(COL_SES_ACT, idActividad);
        cv.put(COL_SES_TEMA, "Sesión N° " + numeroSesion);
        cv.put(COL_SES_FECHA, fechaHoy);
        cv.put("is_new", 1); // <--- IMPORTANTE: Marcar como nueva

        long res = db.insert(TABLE_SESION, null, cv);
        return res != -1;
    }

    // A. OBTENER PERSONAS NUEVAS (Para subir)
    @SuppressLint("Range")
    public List<Map<String, String>> getNewPersonas() {
        List<Map<String, String>> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PERSONA + " WHERE " + COL_IS_NEW + " = 1", null);
        if (cursor.moveToFirst()) {
            do {
                Map<String, String> map = new HashMap<>();
                map.put("id_local", cursor.getString(cursor.getColumnIndex(COL_PER_ID))); // ID temporal
                map.put("nombre", cursor.getString(cursor.getColumnIndex(COL_PER_NOMBRE)));
                map.put("paterno", cursor.getString(cursor.getColumnIndex(COL_PER_PATERNO)));
                map.put("materno", cursor.getString(cursor.getColumnIndex(COL_PER_MATERNO)));
                map.put("ci", cursor.getString(cursor.getColumnIndex(COL_PER_CI)));
                map.put("fecha_nacimiento", cursor.getString(cursor.getColumnIndex(COL_PER_FECHA)));
                map.put("genero", cursor.getString(cursor.getColumnIndex(COL_PER_GENERO)));
                map.put("celular", cursor.getString(cursor.getColumnIndex(COL_PER_CELULAR)));
                map.put("procedencia", cursor.getString(cursor.getColumnIndex(COL_PER_PROCEDENCIA)));
                map.put("area_intervencion_id", cursor.getString(cursor.getColumnIndex(COL_PER_AREA)));
                list.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    // B. OBTENER VINCULACIONES NUEVAS (CON DATOS COMPLETOS)
    @SuppressLint("Range")
    public List<Map<String, Object>> getNewParticipaEn() {
        List<Map<String, Object>> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Hacemos un JOIN para traernos también la institución de la tabla PARTICIPANTE
        String query = "SELECT pe.*, p." + COL_PART_INST +
                " FROM " + TABLE_PARTICIPA_EN + " pe " +
                " LEFT JOIN " + TABLE_PARTICIPANTE + " p ON pe." + COL_PE_PERSONA + " = p." + COL_PART_PERSONA +
                " WHERE pe." + COL_PE_IS_NEW + " = 1";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Map<String, Object> map = new HashMap<>();
                map.put("id_persona", cursor.getInt(cursor.getColumnIndex(COL_PE_PERSONA)));
                map.put("id_actividad", cursor.getInt(cursor.getColumnIndex(COL_PE_ACTIVIDAD)));

                // NUEVOS CAMPOS:
                map.put("tiene_discapacidad", cursor.getInt(cursor.getColumnIndex(COL_PE_DISC)));
                map.put("es_familiar", cursor.getInt(cursor.getColumnIndex(COL_PE_FAM)));
                map.put("firma", cursor.getInt(cursor.getColumnIndex(COL_PE_FIRMA)));

                // INSTITUCIÓN (Puede ser null)
                int idInst = cursor.getInt(cursor.getColumnIndex(COL_PART_INST));
                if (idInst > 0) {
                    map.put("id_institucion", idInst);
                } else {
                    map.put("id_institucion", null);
                }

                list.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    // C. OBTENER ASISTENCIAS NUEVAS
    @SuppressLint("Range")
    public List<Map<String, Object>> getNewAsistencias() {
        List<Map<String, Object>> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ASISTENCIA + " WHERE " + COL_ASIS_IS_NEW + " = 1", null);
        if (cursor.moveToFirst()) {
            do {
                Map<String, Object> map = new HashMap<>();
                map.put("id_sesion", cursor.getInt(cursor.getColumnIndex(COL_ASIS_SESION)));
                map.put("id_persona", cursor.getInt(cursor.getColumnIndex(COL_ASIS_PERSONA)));
                map.put("firma", cursor.getInt(cursor.getColumnIndex(COL_ASIS_FIRMA)));
                list.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    // D. ACTUALIZAR ID LOCAL CON EL DEL SERVER (Post-Upload)
    public void updatePersonIds(String localId, int serverId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_PER_SERVER_ID, serverId);
        cv.put(COL_IS_NEW, 0); // Ya está sincronizado

        // 1. Actualizar tabla Persona
        db.update(TABLE_PERSONA, cv, COL_PER_ID + "=?", new String[]{localId});

        // 2. Actualizar referencias en Participa_En (Cambiamos el ID local viejo por el Server ID nuevo)
        ContentValues cvP = new ContentValues();
        cvP.put(COL_PE_PERSONA, serverId);
        cvP.put(COL_PE_IS_NEW, 0);
        db.update(TABLE_PARTICIPA_EN, cvP, COL_PE_PERSONA + "=?", new String[]{localId});

        // 3. Actualizar referencias en Asistencia
        ContentValues cvA = new ContentValues();
        cvA.put(COL_ASIS_PERSONA, serverId);
        cvA.put(COL_ASIS_IS_NEW, 0);
        db.update(TABLE_ASISTENCIA, cvA, COL_ASIS_PERSONA + "=?", new String[]{localId});
    }

    // E. MARCAR TODO COMO SINCRONIZADO (Para lo que no requirió cambio de ID)
    public void markAllAsSynced() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("is_new", 0);
        db.update(TABLE_ASISTENCIA, cv, "is_new=1", null);
        db.update(TABLE_PARTICIPA_EN, cv, "is_new=1", null);
    }

    // OBTENER SESIONES NUEVAS (Para subir)
    @SuppressLint("Range")
    public List<Map<String, Object>> getNewSesiones() {
        List<Map<String, Object>> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SESION + " WHERE is_new = 1", null);

        if (cursor.moveToFirst()) {
            do {
                Map<String, Object> map = new HashMap<>();
                map.put("id_local", cursor.getInt(cursor.getColumnIndex(COL_SES_ID)));
                map.put("id_actividad", cursor.getInt(cursor.getColumnIndex(COL_SES_ACT)));
                map.put("tema", cursor.getString(cursor.getColumnIndex(COL_SES_TEMA)));
                map.put("fecha", cursor.getString(cursor.getColumnIndex(COL_SES_FECHA)));
                list.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    // ACTUALIZAR ID DE SESIÓN (Post-Upload)
    // Cuando Laravel nos de el ID real, actualizamos la sesión Y las asistencias ligadas
    public void updateSessionIds(String localId, int serverId) {
        SQLiteDatabase db = this.getWritableDatabase();

        // 1. Actualizar la Sesión
        ContentValues cv = new ContentValues();
        cv.put("server_id", serverId);
        cv.put("is_new", 0);
        db.update(TABLE_SESION, cv, COL_SES_ID + "=?", new String[]{localId});

        // 2. Actualizar las Asistencias que apuntaban a ese ID local
        // Ahora deben apuntar al ID del server para futuras sincronizaciones
        ContentValues cvAsis = new ContentValues();
        cvAsis.put(COL_ASIS_SESION, serverId);
        db.update(TABLE_ASISTENCIA, cvAsis, COL_ASIS_SESION + "=?", new String[]{localId});
    }
    // 1. OBTENER LISTA DE INSTITUCIONES (Para el Spinner)
    @SuppressLint("Range")
    public List<Map<String, String>> getAllInstituciones() {
        List<Map<String, String>> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_INSTITUCION + " ORDER BY " + COL_INST_NOMBRE, null);

        // Opción vacía por defecto
        Map<String, String> defaultOption = new HashMap<>();
        defaultOption.put("id", "-1");
        defaultOption.put("nombre", "-- Sin Institución --");
        list.add(defaultOption);

        if (cursor.moveToFirst()) {
            do {
                Map<String, String> map = new HashMap<>();
                map.put("id", cursor.getString(cursor.getColumnIndex(COL_INST_ID)));
                map.put("nombre", cursor.getString(cursor.getColumnIndex(COL_INST_NOMBRE)));
                list.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    // 2. VER SI PERSONA YA TIENE INSTITUCIÓN (Devuelve el ID o -1)
    public int getInstitucionDePersona(String idPersona) {
        SQLiteDatabase db = this.getReadableDatabase();
        int idInstitucion = -1;

        Cursor cursor = db.rawQuery("SELECT " + COL_PART_INST + " FROM " + TABLE_PARTICIPANTE + " WHERE " + COL_PART_PERSONA + " = ?", new String[]{idPersona});

        if (cursor.moveToFirst()) {
            idInstitucion = cursor.getInt(0);
        }
        cursor.close();
        return idInstitucion;
    }

    // 3. VINCULACIÓN COMPLETA (Guarda en participa_en Y actualiza participante)
    public boolean vincularParticipanteCompleto(String idPersona, String idActividad,
                                                int idInstitucion, boolean disc, boolean fam, boolean firma) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            // A. Insertar en participa_en (Relación con Actividad)
            ContentValues cvAct = new ContentValues();
            cvAct.put(COL_PE_PERSONA, idPersona);
            cvAct.put(COL_PE_ACTIVIDAD, idActividad);
            cvAct.put(COL_PE_DISC, disc ? 1 : 0);
            cvAct.put(COL_PE_FAM, fam ? 1 : 0);
            cvAct.put(COL_PE_FIRMA, firma ? 1 : 0);
            cvAct.put(COL_PE_IS_NEW, 1);
            db.insert(TABLE_PARTICIPA_EN, null, cvAct);

            // B. Actualizar/Insertar Institución (Relación Persona-Institución)
            if (idInstitucion != -1) {
                ContentValues cvInst = new ContentValues();
                cvInst.put(COL_PART_PERSONA, idPersona);
                cvInst.put(COL_PART_INST, idInstitucion);

                // Intentamos actualizar, si no existe (0 filas), insertamos
                int rows = db.update(TABLE_PARTICIPANTE, cvInst, COL_PART_PERSONA + "=?", new String[]{idPersona});
                if (rows == 0) {
                    db.insert(TABLE_PARTICIPANTE, null, cvInst);
                }
            }

            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            db.endTransaction();
        }
    }

    // 1. OBTENER CÓDIGOS DE ACTIVIDAD (Para el Spinner)
    @SuppressLint("Range")
    public List<Map<String, String>> getAllCodigosActividad() {
        List<Map<String, String>> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CODIGO_ACT, null);

        if (cursor.moveToFirst()) {
            do {
                Map<String, String> map = new HashMap<>();
                map.put("id", cursor.getString(cursor.getColumnIndex(COL_COD_ID))); // Ej: "A1"
                map.put("nombre", cursor.getString(cursor.getColumnIndex(COL_COD_NOMBRE))); // Ej: "Taller"
                list.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    // 2. CREAR ACTIVIDAD LOCAL
    public boolean addActividadLocal(String nombre, String fecha, String lugar, String descripcion, String codigoId, String areaId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COL_ACT_NOMBRE, nombre);
        cv.put(COL_ACT_FECHA, fecha);
        cv.put(COL_ACT_LUGAR, lugar);
        cv.put(COL_ACT_DESC, descripcion);
        cv.put(COL_ACT_CODIGO, codigoId);
        cv.put(COL_ACT_AREA, areaId);
        cv.put(COL_ACT_IS_NEW, 1); // ¡Es nueva!

        long res = db.insert(TABLE_ACTIVIDAD, null, cv);
        return res != -1;
    }

    // ACTUALIZAR ID DE ACTIVIDAD (Post-Upload)
    public void updateActividadIds(String localId, int serverId) {
        SQLiteDatabase db = this.getWritableDatabase();

        // 1. Actualizar la tabla Actividad
        ContentValues cv = new ContentValues();
        cv.put(COL_ACT_SERVER_ID, serverId); // Ponemos el ID real en la columna server_id (no en la PK local)
        cv.put(COL_ACT_IS_NEW, 0);

        // ¡OJO! En SQLite definimos COL_ACT_ID como PK Autoincrement.
        // Lo ideal es tener una columna separada 'server_id' como hicimos con Personas.
        // Si usaste mi código anterior, la tabla Actividad tiene "server_id" INTEGER.

        db.update(TABLE_ACTIVIDAD, cv, COL_ACT_ID + "=?", new String[]{localId});

        // 2. Actualizar las Sesiones hijas que apuntaban a este ID local
        // Ahora deben apuntar al ID REAL del servidor para que futuras subidas funcionen
        // PERO CUIDADO: La tabla sesión local tiene col_ses_act.
        // Si actualizamos aquí, perdemos la referencia si no tenemos cuidado.
        // En este caso, la sincronización de subida ya mapeó las sesiones en el servidor.
        // Lo más sano aquí es actualizar la referencia local para futuras sesiones creadas offline.

        ContentValues cvSes = new ContentValues();
        cvSes.put(COL_SES_ACT, serverId);
        db.update(TABLE_SESION, cvSes, COL_SES_ACT + "=?", new String[]{localId});

        // 3. Actualizar Participa_En local
        ContentValues cvPe = new ContentValues();
        cvPe.put(COL_PE_ACTIVIDAD, serverId);
        db.update(TABLE_PARTICIPA_EN, cvPe, COL_PE_ACTIVIDAD + "=?", new String[]{localId});
    }
    // OBTENER ACTIVIDADES NUEVAS (Para subir al servidor)
    @SuppressLint("Range")
    public List<Map<String, Object>> getNewActividades() {
        List<Map<String, Object>> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Buscamos las que tengan is_new = 1
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ACTIVIDAD + " WHERE " + COL_ACT_IS_NEW + " = 1", null);

        if (cursor.moveToFirst()) {
            do {
                Map<String, Object> map = new HashMap<>();
                // Enviamos el ID local para que Laravel sepa cuál actualizar después
                map.put("id_local", cursor.getInt(cursor.getColumnIndex(COL_ACT_ID)));

                // Datos de la actividad
                map.put("nombre", cursor.getString(cursor.getColumnIndex(COL_ACT_NOMBRE)));
                map.put("fecha", cursor.getString(cursor.getColumnIndex(COL_ACT_FECHA)));
                map.put("lugar", cursor.getString(cursor.getColumnIndex(COL_ACT_LUGAR)));
                map.put("descripcion", cursor.getString(cursor.getColumnIndex(COL_ACT_DESC)));
                map.put("codigo_actividad_id", cursor.getString(cursor.getColumnIndex(COL_ACT_CODIGO)));
                map.put("area_intervencion_id", cursor.getString(cursor.getColumnIndex(COL_ACT_AREA)));

                list.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

}