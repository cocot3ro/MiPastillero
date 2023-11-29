package com.example.uf1_proyecto

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        const val DB_NAME = "pillbox.db"
        const val DB_VERSION = 1

        private var instance: DBHelper? = null

        fun getInstance(context: Context): DBHelper {
            if (instance == null) {
                synchronized(DBHelper::class) {
                    if (instance == null) {
                        instance = DBHelper(context.applicationContext)
                    }
                }
            }
            return instance!!
        }

        private const val CREATE_MEDICATION_TABLE = """
            CREATE TABLE ${ContratoMedicamentos.NOMBRE_TABLA} (
                ${ContratoMedicamentos.Columnas.COLUMN_COD_NACIONAL} INTEGER PRIMARY KEY,
                ${ContratoMedicamentos.Columnas.COLUMN_NOMBRE} TEXT,
                ${ContratoMedicamentos.Columnas.COLUMN_FICHA_TECNICA} TEXT,
                ${ContratoMedicamentos.Columnas.COLUMN_PROSPECTO} TEXT
            )
        """

        private const val CREATE_ACTIVE_TABLE = """
            CREATE TABLE ${ContratoActivos.NOMBRE_TABLA} (
                ${ContratoActivos.Columnas.COLUMN_COD_NACIONAL} INTEGER,
                ${ContratoActivos.Columnas.COLUMN_INICIO} INTEGER,
                ${ContratoActivos.Columnas.COLUMN_FIN} INTEGER,
                PRIMARY KEY (${ContratoActivos.Columnas.COLUMN_COD_NACIONAL}, ${ContratoActivos.Columnas.COLUMN_INICIO}, ${ContratoActivos.Columnas.COLUMN_FIN}),
                FOREIGN KEY (${ContratoActivos.Columnas.COLUMN_COD_NACIONAL}) REFERENCES ${ContratoMedicamentos.NOMBRE_TABLA}(${ContratoMedicamentos.Columnas.COLUMN_COD_NACIONAL})
            )
        """

        private const val CREATE_FAVORITE_TABLE = """
            CREATE TABLE ${ContratoFavoritos.NOMBRE_TABLA} (
                ${ContratoFavoritos.Columnas.COLUMN_COD_NACIONAL} INTEGER PRIMARY KEY,
                FOREIGN KEY (${ContratoFavoritos.Columnas.COLUMN_COD_NACIONAL}) REFERENCES ${ContratoMedicamentos.NOMBRE_TABLA}(${ContratoMedicamentos.Columnas.COLUMN_COD_NACIONAL})
            )
        """

        private const val CREATE_AGENDA_TABLE = """
            CREATE TABLE ${ContratoAgenda.NOMBRE_TABLA} (
                ${ContratoAgenda.Columnas.COLUMN_FECHA} INTEGER PRIMARY KEY,
                ${ContratoAgenda.Columnas.COLUMN_DESCRIPCION} TEXT
            )
        """

        private const val CREATE_HISTORY_TABLE = """
            CREATE TABLE ${ContratoHistorial.NOMBRE_TABLA} (
                ${ContratoHistorial.Columnas.COLUMN_COD_NACIONAL} INTEGER,
                ${ContratoHistorial.Columnas.COLUMN_INICIO} INTEGER,
                ${ContratoHistorial.Columnas.COLUMN_FIN} INTEGER,
                PRIMARY KEY (${ContratoHistorial.Columnas.COLUMN_COD_NACIONAL}, ${ContratoHistorial.Columnas.COLUMN_INICIO}, ${ContratoHistorial.Columnas.COLUMN_FIN}),
                FOREIGN KEY (${ContratoHistorial.Columnas.COLUMN_COD_NACIONAL}) REFERENCES ${ContratoMedicamentos.NOMBRE_TABLA}(${ContratoMedicamentos.Columnas.COLUMN_COD_NACIONAL})
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

    fun insertIntoMedicamentos(medicamento: Medicamento) {
        val db = writableDatabase

        val values = ContentValues().apply {
            put(ContratoMedicamentos.Columnas.COLUMN_COD_NACIONAL, medicamento.codNacional)
            put(ContratoMedicamentos.Columnas.COLUMN_NOMBRE, medicamento.nombre)
            put(ContratoMedicamentos.Columnas.COLUMN_FICHA_TECNICA, medicamento.fichaTecnica)
            put(ContratoMedicamentos.Columnas.COLUMN_PROSPECTO, medicamento.prospecto)
        }

        db.insert(ContratoMedicamentos.NOMBRE_TABLA, null, values)

        db.close()
    }

    fun insertIntoActivos(medicamento: Medicamento) {
        val db = writableDatabase

        if (!existeEnMedicamentos(medicamento)) {
            insertIntoMedicamentos(medicamento)
        }

        val values = ContentValues().apply {
            put(ContratoActivos.Columnas.COLUMN_COD_NACIONAL, medicamento.codNacional)
            put(ContratoActivos.Columnas.COLUMN_INICIO, medicamento.fechaInicio)
            put(ContratoActivos.Columnas.COLUMN_FIN, medicamento.fechaFin)
        }

        db.close()
    }

    fun insertIntoFavoritos(medicamento: Medicamento) {
        val db = writableDatabase

        if (!existeEnMedicamentos(medicamento)) {
            insertIntoMedicamentos(medicamento)
        }

        val values = ContentValues().apply {
            put(ContratoFavoritos.Columnas.COLUMN_COD_NACIONAL, medicamento.codNacional)
        }

        db.insert(ContratoFavoritos.NOMBRE_TABLA, null, values)

        db.close()
    }

    fun insertIntoAgenda(fecha: Long, descripcion: String) {
        val db = writableDatabase

        if (!existeEnAgenda(fecha)) {
            val values = ContentValues().apply {
                put(ContratoAgenda.Columnas.COLUMN_FECHA, fecha)
                put(ContratoAgenda.Columnas.COLUMN_DESCRIPCION, descripcion)
            }
            db.insert(ContratoAgenda.NOMBRE_TABLA, null, values)
        } else {
            val values = ContentValues().apply {
                put(ContratoAgenda.Columnas.COLUMN_DESCRIPCION, descripcion)
            }
            db.update(
                ContratoAgenda.NOMBRE_TABLA,
                values,
                "${ContratoAgenda.Columnas.COLUMN_FECHA} = ?",
                arrayOf(fecha.toString())
            )
        }

        db.close()
    }

    fun insertIntoHistorial(medicamento: Medicamento) {
        val db = writableDatabase

        val values = ContentValues().apply {
            put(ContratoHistorial.Columnas.COLUMN_COD_NACIONAL, medicamento.codNacional)
            put(ContratoHistorial.Columnas.COLUMN_INICIO, medicamento.fechaInicio)
            put(ContratoHistorial.Columnas.COLUMN_FIN, medicamento.fechaInicio)
        }

        db.insert(ContratoHistorial.NOMBRE_TABLA, null, values)

        db.close()
    }

    fun existeEnAgenda(fecha: Long): Boolean {
        val db = readableDatabase
        val cursor = db.query(
            ContratoAgenda.NOMBRE_TABLA,
            null,
            "${ContratoAgenda.Columnas.COLUMN_FECHA} = ?",
            arrayOf(fecha.toString()),
            null,
            null,
            null
        )

        val existe = cursor.moveToFirst()

        cursor.close()
        db.close()

        return existe
    }

    fun existeEnMedicamentos(medicamento: Medicamento): Boolean {
        val db = readableDatabase
        val cursor = db.query(
            ContratoMedicamentos.NOMBRE_TABLA,
            null,
            "${ContratoMedicamentos.Columnas.COLUMN_COD_NACIONAL} = ?",
            arrayOf(medicamento.codNacional.toString()),
            null,
            null,
            null
        )

        val existe = cursor.moveToFirst()

        cursor.close()
        db.close()

        return existe
    }

    fun getWeek(start: Long, end: Long): MutableList<Medicamento> {
        val list: MutableList<Medicamento> = mutableListOf()

        val db = readableDatabase
        val cursor = db.query(
            ContratoMedicamentos.NOMBRE_TABLA + " INNER JOIN " + ContratoActivos.NOMBRE_TABLA +
                    " ON " + ContratoMedicamentos.NOMBRE_TABLA + "." + ContratoMedicamentos.Columnas.COLUMN_COD_NACIONAL +
                    " = " + ContratoActivos.NOMBRE_TABLA + "." + ContratoActivos.Columnas.COLUMN_COD_NACIONAL,
            null,
            "(${ContratoActivos.Columnas.COLUMN_INICIO} <= ? AND ${ContratoActivos.Columnas.COLUMN_FIN} >= ?) OR " +
                    "(${ContratoActivos.Columnas.COLUMN_INICIO} <= ? AND ${ContratoActivos.Columnas.COLUMN_FIN} >= ?) OR " +
                    "(${ContratoActivos.Columnas.COLUMN_INICIO} <= ? AND ${ContratoActivos.Columnas.COLUMN_FIN} >= ?) OR " +
                    "(${ContratoActivos.Columnas.COLUMN_INICIO} >= ? AND ${ContratoActivos.Columnas.COLUMN_FIN} <= ?)",
            arrayOf(
                start.toString(),
                end.toString(),
                start.toString(),
                start.toString(),
                end.toString(),
                end.toString(),
                start.toString(),
                end.toString()
            ),
            null,
            null,
            null
        )

        val colCodNacional: Int
        val colNombre: Int
        val colFichaTecnica: Int
        val colProspecto: Int
        val colFechaInicio: Int
        val colFechaFin: Int

        if (cursor.moveToFirst()) {
            colCodNacional = cursor.getColumnIndex(ContratoMedicamentos.Columnas.COLUMN_COD_NACIONAL)
            colNombre = cursor.getColumnIndex(ContratoMedicamentos.Columnas.COLUMN_NOMBRE)
            colFichaTecnica = cursor.getColumnIndex(ContratoMedicamentos.Columnas.COLUMN_FICHA_TECNICA)
            colProspecto = cursor.getColumnIndex(ContratoMedicamentos.Columnas.COLUMN_PROSPECTO)
            colFechaInicio = cursor.getColumnIndex(ContratoActivos.Columnas.COLUMN_INICIO)
            colFechaFin = cursor.getColumnIndex(ContratoActivos.Columnas.COLUMN_FIN)

            do {
                list.add(Medicamento(cursor.getInt(colCodNacional), cursor.getString(colNombre), cursor.getString(colFichaTecnica), cursor.getString(colProspecto), cursor.getLong(colFechaInicio), cursor.getLong(colFechaFin)))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return list
    }
}