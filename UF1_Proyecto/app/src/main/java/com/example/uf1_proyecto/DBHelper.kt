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
        private const val DB_VERSION = 61

        /**
         * Instancia única de la clase [DBHelper]
         */
        @Volatile
        private var instance: DBHelper? = null

        /**
         * Devuelve la instancia única de la clase [DBHelper]
         */
        fun getInstance(context: Context): DBHelper = instance ?: synchronized(this) {
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
         * Sentencia SQL para crear la tabla de calendario
         */
        private const val CREATE_CALENDAR_TABLE = """
            CREATE TABLE IF NOT EXISTS ${ContratoCalendario.NOMBRE_TABLA} (
                ${ContratoCalendario.Columnas._ID} TEXT,
                ${ContratoCalendario.Columnas.COLUMN_FECHA} INTEGER,
                ${ContratoCalendario.Columnas.COLUMN_HORA} INTEGER,
                ${ContratoCalendario.Columnas.COLUMN_SE_HA_TOMADO} INTEGER,
                PRIMARY KEY (${ContratoCalendario.Columnas._ID}, ${ContratoCalendario.Columnas.COLUMN_FECHA}, ${ContratoCalendario.Columnas.COLUMN_HORA}),
                FOREIGN KEY (${ContratoCalendario.Columnas._ID}) REFERENCES ${ContratoActivos.NOMBRE_TABLA}(${ContratoActivos.Columnas._ID})
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
                ${ContratoHistorial.Columnas.COLUMN_HORARIO} TEXT,
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
        db.execSQL(CREATE_CALENDAR_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS ${ContratoActivos.NOMBRE_TABLA}")
        db.execSQL("DROP TABLE IF EXISTS ${ContratoFavoritos.NOMBRE_TABLA}")
        db.execSQL("DROP TABLE IF EXISTS ${ContratoAgenda.NOMBRE_TABLA}")
        db.execSQL("DROP TABLE IF EXISTS ${ContratoHistorial.NOMBRE_TABLA}")
        db.execSQL("DROP TABLE IF EXISTS ${ContratoCalendario.NOMBRE_TABLA}")
        db.execSQL("DROP TABLE IF EXISTS ${ContratoMedicamentos.NOMBRE_TABLA}")

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
     * Añade un medicamento activo a la base de datos
     * @param medicamento medicamento a insertar
     * @return true si se ha insertado correctamente, false si no
     */
    fun insertIntoActivos(medicamento: Medicamento): Boolean {
        if (!existeEnMedicamentos(medicamento)) {
            insertIntoMedicamentos(medicamento)
        }

        insertIntoCalendario(medicamento)

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

    private fun insertIntoCalendario(medicamento: Medicamento) {
        var dia = medicamento.fechaInicio!!
        while (dia <= medicamento.fechaFin!!) {

            for (hora in medicamento.horario!!) {
                val values = ContentValues().apply {
                    put(ContratoCalendario.Columnas._ID, medicamento.nombre)
                    put(ContratoCalendario.Columnas.COLUMN_FECHA, dia)
                    put(ContratoCalendario.Columnas.COLUMN_HORA, hora)
                    put(ContratoCalendario.Columnas.COLUMN_SE_HA_TOMADO, 0)
                }

                writableDatabase.use { db ->
                    db.insert(ContratoCalendario.NOMBRE_TABLA, null, values)
                }
            }

            dia = DateTimeUtils.nextDay(dia)
        }
    }

    /**
     * Elimina un medicamento activo de la base de datos
     * @param medicamento medicamento a eliminar
     * @return true si se ha eliminado correctamente, false si no
     */
    fun deleteFromActivos(medicamento: Medicamento): Boolean {
        deleteFromCalendario(medicamento)
        writableDatabase.use { db ->
            return db.delete(
                ContratoActivos.NOMBRE_TABLA,
                "${ContratoActivos.Columnas._ID} = ? AND " + "${ContratoActivos.Columnas.COLUMN_INICIO} = ? AND " + "${ContratoActivos.Columnas.COLUMN_FIN} = ?",
                arrayOf(
                    medicamento.nombre,
                    medicamento.fechaInicio.toString(),
                    medicamento.fechaFin.toString()
                )
            ) != 0
        }
    }

    private fun deleteFromCalendario(medicamento: Medicamento) {
        var dia = medicamento.fechaInicio!!
        while (dia <= medicamento.fechaFin!!) {

            writableDatabase.use { db ->
                db.delete(
                    ContratoCalendario.NOMBRE_TABLA,
                    "${ContratoCalendario.Columnas._ID} = ? AND ${ContratoCalendario.Columnas.COLUMN_FECHA} = ?",
                    arrayOf(medicamento.nombre, dia.toString())
                )
            }

            dia = DateTimeUtils.nextDay(dia)
        }
    }

    /**
     * Devuelve los medicamentos activos
     */
    fun getActivos(): List<Medicamento> {
        val dia = DateTimeUtils.getTodayAsMillis().toString()
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
                    val colNombre = cursor.getColumnIndex(ContratoMedicamentos.Columnas._ID)

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
                            MedicamentoBuilder().setNombre(cursor.getString(colNombre))
                                .setCodNacional(cursor.getInt(colCodNacional))
                                .setFichaTecnica(cursor.getString(colFichaTecnica))
                                .setProspecto(cursor.getString(colProspecto))
                                .setFechaInicio(cursor.getLong(colFechaInicio))
                                .setFechaFin(cursor.getLong(colFechaFin))
                                .setHorario(
                                    Gson().fromJson(
                                        cursor.getString(colHorario),
                                        object : TypeToken<Set<Long>>() {}.type
                                    )
                                ).setFavorito(existeEnFavoritos(cursor.getString(colNombre)))
                                .build()
                        )
                    } while (cursor.moveToNext())
                }
            }
        }

        return listaActivos
    }

    /**
     * Devuelve los medicamentos activos agrupados por hora
     * @param dia día del que se quiere obtener los medicamentos
     */
    fun getActivosCalendario(dia: Long): Map<Long, List<Medicamento>> {
        val map = sortedMapOf<Long, MutableList<Medicamento>>()

        readableDatabase.use { db ->
            db.query(
                ContratoMedicamentos.NOMBRE_TABLA + " INNER JOIN " + ContratoActivos.NOMBRE_TABLA + " ON " + ContratoMedicamentos.NOMBRE_TABLA + "." + ContratoMedicamentos.Columnas._ID + " = " + ContratoActivos.NOMBRE_TABLA + "." + ContratoActivos.Columnas._ID
                        + " INNER JOIN " + ContratoCalendario.NOMBRE_TABLA + " ON " + ContratoMedicamentos.NOMBRE_TABLA + "." + ContratoMedicamentos.Columnas._ID + " = " + ContratoCalendario.NOMBRE_TABLA + "." + ContratoCalendario.Columnas._ID,
                null,
                "${ContratoCalendario.Columnas.COLUMN_FECHA} = ?",
                arrayOf(dia.toString()),
                null,
                null,
                ContratoActivos.Columnas.COLUMN_INICIO
            ).use { cursor ->
                if (cursor.moveToFirst()) {
                    val colNombre = cursor.getColumnIndex(ContratoMedicamentos.Columnas._ID)

                    val colHora = cursor.getColumnIndex(ContratoCalendario.Columnas.COLUMN_HORA)

                    val colSeHaTomado =
                        cursor.getColumnIndex(ContratoCalendario.Columnas.COLUMN_SE_HA_TOMADO)

                    do {
                        val medicamento =
                            MedicamentoBuilder().setNombre(cursor.getString(colNombre))
                                .setSeHaTomado(cursor.getInt(colSeHaTomado) == 1)
                                .build()

                        if (map.containsKey(cursor.getLong(colHora))) {
                            map[cursor.getLong(colHora)]!!.add(medicamento)
                        } else {
                            map[cursor.getLong(colHora)] = mutableListOf(medicamento)
                        }
                    } while (cursor.moveToNext())
                }
            }
        }

        return map
    }

    private fun updateCalendario(med: Medicamento, hora: Long, dia: Long, update: Int): Boolean {
        val values = ContentValues().apply {
            put(ContratoCalendario.Columnas.COLUMN_SE_HA_TOMADO, update)
        }

        writableDatabase.use { db ->
            val i = db.update(
                ContratoCalendario.NOMBRE_TABLA,
                values,
                "${ContratoCalendario.Columnas._ID} = ? AND ${ContratoCalendario.Columnas.COLUMN_FECHA} = ? AND ${ContratoCalendario.Columnas.COLUMN_HORA} = ?",
                arrayOf(med.nombre, dia.toString(), hora.toString())
            )

            return i != 0
        }
    }

    fun desmarcarToma(med: Medicamento, hora: Long, dia: Long): Boolean {
        return updateCalendario(med, hora, dia, 0)
    }

    fun marcarToma(med: Medicamento, hora: Long, dia: Long): Boolean {
        return updateCalendario(med, hora, dia, 1)
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
    private fun insertIntoHistorial(medicamento: Medicamento): Boolean {
        val values = ContentValues().apply {
            put(ContratoHistorial.Columnas._ID, medicamento.nombre)
            put(ContratoHistorial.Columnas.COLUMN_INICIO, medicamento.fechaInicio)
            put(ContratoHistorial.Columnas.COLUMN_FIN, medicamento.fechaFin)
            put(ContratoHistorial.Columnas.COLUMN_HORARIO, Gson().toJson(medicamento.horario))
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
                    val colNombre = cursor.getColumnIndex(ContratoMedicamentos.Columnas._ID)

                    val colCodNacional =
                        cursor.getColumnIndex(ContratoMedicamentos.Columnas.COLUMN_COD)

                    val colFichaTecnica =
                        cursor.getColumnIndex(ContratoMedicamentos.Columnas.COLUMN_FICHA_TECNICA)

                    val colProspecto =
                        cursor.getColumnIndex(ContratoMedicamentos.Columnas.COLUMN_PROSPECTO)

                    do {
                        listaFavoritos.add(
                            MedicamentoBuilder().setNombre(cursor.getString(colNombre))
                                .setCodNacional(cursor.getInt(colCodNacional))
                                .setFichaTecnica(cursor.getString(colFichaTecnica))
                                .setProspecto(cursor.getString(colProspecto)).setFavorito(true)
                                .build()
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

    fun comprobarTerminados() {
        val dia = DateTimeUtils.getTodayAsMillis()
        val listaTerminados = mutableListOf<Medicamento>()
        readableDatabase.use { db ->
            db.query(
                "${ContratoMedicamentos.NOMBRE_TABLA} INNER JOIN ${ContratoActivos.NOMBRE_TABLA} ON ${ContratoMedicamentos.NOMBRE_TABLA}.${ContratoMedicamentos.Columnas._ID} = ${ContratoActivos.NOMBRE_TABLA}.${ContratoActivos.Columnas._ID}",
                null,
                "${ContratoActivos.Columnas.COLUMN_FIN} < ?",
                arrayOf(dia.toString()),
                null,
                null,
                null
            ).use { cursor ->
                if (cursor.moveToFirst()) {
                    val colNombre = cursor.getColumnIndex(ContratoMedicamentos.Columnas._ID)

                    val colFechaInicio =
                        cursor.getColumnIndex(ContratoActivos.Columnas.COLUMN_INICIO)

                    val colFechaFin = cursor.getColumnIndex(ContratoActivos.Columnas.COLUMN_FIN)

                    val colHorario = cursor.getColumnIndex(ContratoActivos.Columnas.COLUMN_HORARIO)

                    do {
                        listaTerminados.add(
                            MedicamentoBuilder()
                                .setNombre(cursor.getString(colNombre))
                                .setFechaInicio(cursor.getLong(colFechaInicio))
                                .setFechaFin(cursor.getLong(colFechaFin))
                                .setHorario(
                                    Gson().fromJson(
                                        cursor.getString(colHorario),
                                        object : TypeToken<Set<Long>>() {}.type
                                    )
                                ).build()
                        )

                    } while (cursor.moveToNext())
                }
            }
        }

        if (listaTerminados.isNotEmpty()) {
            for (med in listaTerminados) {
                deleteFromActivos(med)
                insertIntoHistorial(med)
            }
        }
    }

    fun getHistorial(): Map<Long, List<Medicamento>> {
        val historial = sortedMapOf<Long, MutableList<Medicamento>>()

        readableDatabase.use { db ->
            db.query(
                "${ContratoMedicamentos.NOMBRE_TABLA} INNER JOIN ${ContratoHistorial.NOMBRE_TABLA} ON ${ContratoMedicamentos.NOMBRE_TABLA}.${ContratoMedicamentos.Columnas._ID} = ${ContratoHistorial.NOMBRE_TABLA}.${ContratoHistorial.Columnas._ID}",
                null,
                null,
                null,
                null,
                null,
                null
            ).use { cursor ->
                if (cursor.moveToFirst()) {
                    val colNombre = cursor.getColumnIndex(ContratoMedicamentos.Columnas._ID)

                    val colCodNacional =
                        cursor.getColumnIndex(ContratoMedicamentos.Columnas.COLUMN_COD)

                    val colFichaTecnica =
                        cursor.getColumnIndex(ContratoMedicamentos.Columnas.COLUMN_FICHA_TECNICA)

                    val colProspecto =
                        cursor.getColumnIndex(ContratoMedicamentos.Columnas.COLUMN_PROSPECTO)

                    val colFechaInicio =
                        cursor.getColumnIndex(ContratoHistorial.Columnas.COLUMN_INICIO)

                    val colFechaFin = cursor.getColumnIndex(ContratoHistorial.Columnas.COLUMN_FIN)

                    val colHorario =
                        cursor.getColumnIndex(ContratoHistorial.Columnas.COLUMN_HORARIO)

                    do {
                        val medicamento = MedicamentoBuilder()
                            .setNombre(cursor.getString(colNombre))
                            .setFichaTecnica(cursor.getString(colFichaTecnica))
                            .setProspecto(cursor.getString(colProspecto))
                            .setFechaInicio(cursor.getLong(colFechaInicio))
                            .setFechaFin(cursor.getLong(colFechaFin))
                            .setHorario(
                                Gson().fromJson(
                                    cursor.getString(colHorario),
                                    object : TypeToken<Set<Long>>() {}.type
                                )
                            )
                            .build()

                        if (historial.containsKey(cursor.getLong(colFechaInicio))) {
                            historial[cursor.getLong(colFechaInicio)]!!.add(medicamento)
                        } else {
                            historial[cursor.getLong(colFechaInicio)] = mutableListOf(medicamento)
                        }
                    } while (cursor.moveToNext())
                }
            }
        }

        return historial
    }

}