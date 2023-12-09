package com.example.uf1_proyecto

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DBHelper private constructor(context: Context) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        private const val DB_NAME = "pillbox.db"

        // TODO: Cambiar a version 1 al finalizar proyecto
        private const val DB_VERSION = 31

        /**
         * Instancia única de la clase [DBHelper]
         */
        @Volatile
        private var instance: DBHelper? = null

        /**
         * Devuelve la instancia única de la clase [DBHelper]
         * @param context contexto de la aplicación
         */
        fun getInstance(context: Context): DBHelper =
            instance ?: synchronized(this) {
                instance ?: DBHelper(context.applicationContext).also { instance = it }
            }

        /**
         * Sentencia SQL para crear la tabla de medicamentos
         */
        private const val CREATE_MEDICATION_TABLE = """
            CREATE TABLE IF NOT EXISTS ${ContratoMedicamentos.NOMBRE_TABLA} (
                ${ContratoMedicamentos.Columnas._ID} TEXT PRIMARY KEY,
                ${ContratoMedicamentos.Columnas.COLUMN_COD} INTEGER DEFAULT -1,
                ${ContratoMedicamentos.Columnas.COLUMN_FICHA_TECNICA} TEXT,
                ${ContratoMedicamentos.Columnas.COLUMN_PROSPECTO} TEXT
            )
        """

        /**
         * Sentencia SQL para crear la tabla de activos
         */
        private const val CREATE_ACTIVE_TABLE = """
            CREATE TABLE IF NOT EXISTS ${ContratoActivos.NOMBRE_TABLA} (
                ${ContratoActivos.Columnas._ID} TEXT,
                ${ContratoActivos.Columnas.COLUMN_INICIO} INTEGER,
                ${ContratoActivos.Columnas.COLUMN_FIN} INTEGER,
                ${ContratoActivos.Columnas.COLUMN_HORARIO} TEXT,
                PRIMARY KEY (${ContratoActivos.Columnas._ID}, ${ContratoActivos.Columnas.COLUMN_INICIO}, ${ContratoActivos.Columnas.COLUMN_FIN}),
                FOREIGN KEY (${ContratoActivos.Columnas._ID}) REFERENCES ${ContratoMedicamentos.NOMBRE_TABLA}(${ContratoMedicamentos.Columnas._ID})
            )
        """

        /**
         * Sentencia SQL para crear la tabla de favoritos
         */
        private const val CREATE_FAVORITE_TABLE = """
            CREATE TABLE IF NOT EXISTS ${ContratoFavoritos.NOMBRE_TABLA} (
                ${ContratoFavoritos.Columnas._ID} TEXT PRIMARY KEY,
                FOREIGN KEY (${ContratoFavoritos.Columnas._ID}) REFERENCES ${ContratoMedicamentos.NOMBRE_TABLA}(${ContratoMedicamentos.Columnas._ID})
            )
        """

        /**
         * Sentencia SQL para crear la tabla de agenda
         */
        private const val CREATE_AGENDA_TABLE = """
            CREATE TABLE IF NOT EXISTS ${ContratoAgenda.NOMBRE_TABLA} (
                ${ContratoAgenda.Columnas._ID} INTEGER PRIMARY KEY,
                ${ContratoAgenda.Columnas.COLUMN_DESCRIPCION} TEXT
            )
        """

        /**
         * Sentencia SQL para crear la tabla de historial
         */
        private const val CREATE_HISTORY_TABLE = """
            CREATE TABLE IF NOT EXISTS ${ContratoHistorial.NOMBRE_TABLA} (
                ${ContratoHistorial.Columnas._ID} TEXT,
                ${ContratoHistorial.Columnas.COLUMN_INICIO} INTEGER,
                ${ContratoHistorial.Columnas.COLUMN_FIN} INTEGER,
                PRIMARY KEY (${ContratoHistorial.Columnas._ID}, ${ContratoHistorial.Columnas.COLUMN_INICIO}, ${ContratoHistorial.Columnas.COLUMN_FIN}),
                FOREIGN KEY (${ContratoHistorial.Columnas._ID}) REFERENCES ${ContratoMedicamentos.NOMBRE_TABLA}(${ContratoMedicamentos.Columnas._ID})
            )
        """
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_MEDICATION_TABLE)
        db.execSQL(CREATE_ACTIVE_TABLE)
        db.execSQL(CREATE_FAVORITE_TABLE)
        db.execSQL(CREATE_AGENDA_TABLE)
        db.execSQL(CREATE_HISTORY_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS ${ContratoMedicamentos.NOMBRE_TABLA}")
        db.execSQL("DROP TABLE IF EXISTS ${ContratoActivos.NOMBRE_TABLA}")
        db.execSQL("DROP TABLE IF EXISTS ${ContratoFavoritos.NOMBRE_TABLA}")
        db.execSQL("DROP TABLE IF EXISTS ${ContratoAgenda.NOMBRE_TABLA}")
        db.execSQL("DROP TABLE IF EXISTS ${ContratoHistorial.NOMBRE_TABLA}")

        onCreate(db)
    }

    /**
     * Inserta un medicamento en la tabla medicamentos
     * @param medicamento medicamento a insertar
     * @return true si se ha insertado correctamente, false si no
     */
    private fun insertIntoMedicamentos(medicamento: Medicamento) {

        val values = ContentValues().apply {
            put(ContratoMedicamentos.Columnas._ID, medicamento.nombre)
            put(ContratoMedicamentos.Columnas.COLUMN_COD, medicamento.codNacional)
            put(ContratoMedicamentos.Columnas.COLUMN_FICHA_TECNICA, medicamento.fichaTecnica)
            put(ContratoMedicamentos.Columnas.COLUMN_PROSPECTO, medicamento.prospecto)
        }

        writableDatabase.use { db ->
            db.insert(ContratoMedicamentos.NOMBRE_TABLA, null, values)
        }
    }

    /**
     * Inserta un medicamento en la tabla activos
     * @param medicamento medicamento a insertar
     * @return true si se ha insertado correctamente, false si no
     */
    fun insertIntoActivos(medicamento: Medicamento): Boolean {
        if (!existeEnMedicamentos(medicamento)) {
            insertIntoMedicamentos(medicamento)
        }

        val values = ContentValues().apply {
            put(ContratoActivos.Columnas._ID, medicamento.nombre)
            put(ContratoActivos.Columnas.COLUMN_INICIO, medicamento.fechaInicio)
            put(ContratoActivos.Columnas.COLUMN_FIN, medicamento.fechaFin)
            put(ContratoActivos.Columnas.COLUMN_HORARIO, Gson().toJson(medicamento.horario))
        }

        writableDatabase.use { db ->
            return db.insert(ContratoActivos.NOMBRE_TABLA, null, values) != -1L
        }
    }

    /**
     * Elimina un medicamento de la tabla activos
     * @param medicamento medicamento a eliminar
     * @return true si se ha eliminado correctamente, false si no
     */
    fun deleteFromActivos(medicamento: Medicamento): Boolean {
        writableDatabase.use { db ->
            return db.delete(
                ContratoActivos.NOMBRE_TABLA,
                "${ContratoActivos.Columnas._ID} = ? AND " +
                        "${ContratoActivos.Columnas.COLUMN_INICIO} = ? AND " +
                        "${ContratoActivos.Columnas.COLUMN_FIN} = ?",
                arrayOf(
                    medicamento.nombre,
                    medicamento.fechaInicio.toString(),
                    medicamento.fechaFin.toString()
                )
            ) != 0
        }
    }

    /**
     * Inserta un medicamento en la tabla de favoritos
     * @param medicamento medicamento a insertar
     * @return true si se ha insertado correctamente, false si no
     */
    fun insertIntoFavoritos(medicamento: Medicamento): Boolean {
        if (!existeEnMedicamentos(medicamento)) {
            insertIntoMedicamentos(medicamento)
        }

        val values = ContentValues().apply {
            put(ContratoFavoritos.Columnas._ID, medicamento.nombre)
        }

        writableDatabase.use { db ->
            return db.insert(ContratoFavoritos.NOMBRE_TABLA, null, values) != -1L
        }
    }

    /**
     * Elimina un medicamento de la tabla de favoritos
     * @param medicamento medicamento a eliminar
     * @return true si se ha eliminado correctamente, false si no
     */
    fun deleteFromFavoritos(medicamento: Medicamento): Boolean {
        writableDatabase.use { db ->
            return db.delete(
                ContratoFavoritos.NOMBRE_TABLA,
                "${ContratoFavoritos.Columnas._ID} = ?",
                arrayOf(medicamento.nombre)
            ) != 0
        }
    }

    /**
     * Inserta una entrada en la agenda
     * @param fecha fecha de la entrada
     * @param descripcion descripción de la entrada
     * @return true si se ha insertado correctamente, false si no
     */
    fun insertIntoAgenda(fecha: Long, descripcion: String): Boolean {
        if (!existeEnAgenda(fecha)) {
            val values = ContentValues().apply {
                put(ContratoAgenda.Columnas._ID, fecha)
                put(ContratoAgenda.Columnas.COLUMN_DESCRIPCION, descripcion)
            }

            writableDatabase.use { db ->
                return db.insert(ContratoAgenda.NOMBRE_TABLA, null, values) != -1L
            }
        } else {
            val values = ContentValues().apply {
                put(ContratoAgenda.Columnas.COLUMN_DESCRIPCION, descripcion)
            }

            writableDatabase.use { db ->
                return db.update(
                    ContratoAgenda.NOMBRE_TABLA,
                    values,
                    "${ContratoAgenda.Columnas._ID} = ?",
                    arrayOf(fecha.toString())
                ) != 0
            }
        }
    }

    /**
     * Inserta una entrada en el historial
     * @param medicamento medicamento a insertar
     * @return true si se ha insertado correctamente, false si no
     */
    fun insertIntoHistorial(medicamento: Medicamento): Boolean {
        val values = ContentValues().apply {
            put(ContratoHistorial.Columnas._ID, medicamento.nombre)
            put(ContratoHistorial.Columnas.COLUMN_INICIO, medicamento.fechaInicio)
            put(ContratoHistorial.Columnas.COLUMN_FIN, medicamento.fechaInicio)
        }

        writableDatabase.use { db ->
            return db.insert(ContratoHistorial.NOMBRE_TABLA, null, values) != -1L
        }
    }

    /**
     * Comprueba si existe una entrada en la agenda
     * @param fecha fecha de la entrada
     * @return true si existe, false si no
     */
    private fun existeEnAgenda(fecha: Long): Boolean {
        readableDatabase.use { db ->
            db.query(
                ContratoAgenda.NOMBRE_TABLA,
                null,
                "${ContratoAgenda.Columnas._ID} = ?",
                arrayOf(fecha.toString()),
                null,
                null,
                null
            ).use { cursor ->
                return cursor.moveToFirst()
            }
        }
    }

    /**
     * Comprueba si existe un medicamento en la tabla de medicamentos
     * @param medicamento medicamento a comprobar
     * @return true si existe, false si no
     */
    private fun existeEnMedicamentos(medicamento: Medicamento): Boolean {
        readableDatabase.use { db ->
            db.query(
                ContratoMedicamentos.NOMBRE_TABLA,
                null,
                "${ContratoMedicamentos.Columnas._ID} = ?",
                arrayOf(medicamento.nombre),
                null,
                null,
                null
            ).use { cursor ->
                return cursor.moveToFirst()
            }
        }
    }

    /**
     * Comprueba si un medicamento está en la lista de favoritos
     * @param medicamento medicamento a comprobar
     * @return true si está en favoritos, false si no
     */
    fun existeEnFavoritos(medicamento: Medicamento): Boolean {
        return existeEnFavoritos(medicamento.nombre)
    }

    /**
     * Comprueba si un medicamento está en la lista de favoritos
     * @param nombre nombre del medicamento a comprobar
     * @return true si está en favoritos, false si no
     */
    private fun existeEnFavoritos(nombre: String): Boolean {
        readableDatabase.use { db ->
            db.query(
                ContratoMedicamentos.NOMBRE_TABLA + " INNER JOIN " + ContratoFavoritos.NOMBRE_TABLA + " ON " + ContratoMedicamentos.NOMBRE_TABLA + "." + ContratoMedicamentos.Columnas._ID + " = " + ContratoFavoritos.NOMBRE_TABLA + "." + ContratoFavoritos.Columnas._ID,
                null,
                "${ContratoFavoritos.NOMBRE_TABLA}.${ContratoFavoritos.Columnas._ID} = ?",
                arrayOf(nombre),
                null,
                null,
                null
            ).use { cursor ->
                return cursor.moveToFirst()
            }
        }
    }

    /**
     * Devuelve los medicamentos activos
     */
    fun getActivos(): List<Medicamento> {
        val dia = System.currentTimeMillis().toString()
        val listaActivos: MutableList<Medicamento> = mutableListOf()

        readableDatabase.use { db ->
            db.query(
                ContratoMedicamentos.NOMBRE_TABLA + " INNER JOIN " + ContratoActivos.NOMBRE_TABLA + " ON " + ContratoMedicamentos.NOMBRE_TABLA + "." + ContratoMedicamentos.Columnas._ID + " = " + ContratoActivos.NOMBRE_TABLA + "." + ContratoActivos.Columnas._ID,
                null,
                "(${ContratoActivos.Columnas.COLUMN_INICIO} <= ? AND ${ContratoActivos.Columnas.COLUMN_FIN} >= ?) OR ${ContratoActivos.Columnas.COLUMN_INICIO} >= ?",
                arrayOf(dia, dia, dia),
                null,
                null,
                ContratoActivos.Columnas.COLUMN_INICIO
            ).use { cursor ->
                if (cursor.moveToFirst()) {
                    val colNombre =
                        cursor.getColumnIndex(ContratoMedicamentos.Columnas._ID)

                    val colCodNacional =
                        cursor.getColumnIndex(ContratoMedicamentos.Columnas.COLUMN_COD)

                    val colFichaTecnica =
                        cursor.getColumnIndex(ContratoMedicamentos.Columnas.COLUMN_FICHA_TECNICA)

                    val colProspecto =
                        cursor.getColumnIndex(ContratoMedicamentos.Columnas.COLUMN_PROSPECTO)

                    val colFechaInicio =
                        cursor.getColumnIndex(ContratoActivos.Columnas.COLUMN_INICIO)

                    val colFechaFin = cursor.getColumnIndex(ContratoActivos.Columnas.COLUMN_FIN)

                    val colHorario = cursor.getColumnIndex(ContratoActivos.Columnas.COLUMN_HORARIO)

                    do {
                        listaActivos.add(
                            Medicamento(
                                cursor.getString(colNombre),
                                cursor.getInt(colCodNacional),
                                cursor.getString(colFichaTecnica),
                                cursor.getString(colProspecto),
                                cursor.getLong(colFechaInicio),
                                cursor.getLong(colFechaFin),
                                Gson().fromJson(
                                    cursor.getString(colHorario),
                                    object : TypeToken<List<Long>>() {}.type
                                ),
                                existeEnFavoritos(cursor.getString(colNombre))
                            )
                        )
                    } while (cursor.moveToNext())
                }
            }
        }

        return listaActivos
    }

    /**
     * Devuelve los medicamentos favoritos
     */
    fun getFavoritos(): List<Medicamento> {
        val listaFavoritos = mutableListOf<Medicamento>()

        readableDatabase.use { db ->
            db.query(
                ContratoMedicamentos.NOMBRE_TABLA + " INNER JOIN " + ContratoFavoritos.NOMBRE_TABLA + " ON " + ContratoMedicamentos.NOMBRE_TABLA + "." + ContratoMedicamentos.Columnas._ID + " = " + ContratoFavoritos.NOMBRE_TABLA + "." + ContratoFavoritos.Columnas._ID,
                null,
                null,
                null,
                null,
                null,
                null
            ).use { cursor ->
                if (cursor.moveToFirst()) {
                    val colNombre =
                        cursor.getColumnIndex(ContratoMedicamentos.Columnas._ID)

                    val colCodNacional =
                        cursor.getColumnIndex(ContratoMedicamentos.Columnas.COLUMN_COD)

                    val colFichaTecnica =
                        cursor.getColumnIndex(ContratoMedicamentos.Columnas.COLUMN_FICHA_TECNICA)

                    val colProspecto =
                        cursor.getColumnIndex(ContratoMedicamentos.Columnas.COLUMN_PROSPECTO)

                    do {
                        listaFavoritos.add(
                            Medicamento(
                                cursor.getString(colNombre),
                                cursor.getInt(colCodNacional),
                                cursor.getString(colFichaTecnica),
                                cursor.getString(colProspecto),
                                -1L,
                                -1L,
                                listOf(),
                                existeEnFavoritos(cursor.getString(colNombre))
                            )
                        )
                    } while (cursor.moveToNext())
                }
            }
        }

        return listaFavoritos
    }

    /**
     * Devuelve la entrada de la agenda correspondiente a la fecha
     * @param diaryCurrDate fecha de la entrada
     * @return texto de la entrada si existe, null si no existe
     */
    fun getDiaryEntry(diaryCurrDate: Long): String? {
        readableDatabase.use { db ->
            db.query(
                ContratoAgenda.NOMBRE_TABLA,
                null,
                "${ContratoAgenda.Columnas._ID} = ?",
                arrayOf(diaryCurrDate.toString()),
                null,
                null,
                null
            ).use { cursor ->
                if (cursor.moveToFirst()) {
                    val colDescripcion =
                        cursor.getColumnIndex(ContratoAgenda.Columnas.COLUMN_DESCRIPCION)

                    return cursor.getString(colDescripcion)
                }
            }
        }

        return null
    }

}
