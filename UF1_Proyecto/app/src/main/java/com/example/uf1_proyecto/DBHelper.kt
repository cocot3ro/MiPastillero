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
        const val DB_NAME = "pillbox.db"

        // TODO: Cambiar a version 1 al finalizar proyecto
        const val DB_VERSION = 21

        @Volatile
        private var instance: DBHelper? = null

        fun getInstance(context: Context): DBHelper =
            instance ?: synchronized(this) {
                instance ?: DBHelper(context.applicationContext).also { instance = it }
            }

        private const val CREATE_MEDICATION_TABLE = """
            CREATE TABLE IF NOT EXISTS ${ContratoMedicamentos.NOMBRE_TABLA} (
                ${ContratoMedicamentos.Columnas._ID} INTEGER PRIMARY KEY,
                ${ContratoMedicamentos.Columnas.COLUMN_NOMBRE} TEXT,
                ${ContratoMedicamentos.Columnas.COLUMN_FICHA_TECNICA} TEXT,
                ${ContratoMedicamentos.Columnas.COLUMN_PROSPECTO} TEXT
            )
        """

        private const val CREATE_ACTIVE_TABLE = """
            CREATE TABLE IF NOT EXISTS ${ContratoActivos.NOMBRE_TABLA} (
                ${ContratoActivos.Columnas._ID} INTEGER,
                ${ContratoActivos.Columnas.COLUMN_INICIO} INTEGER,
                ${ContratoActivos.Columnas.COLUMN_FIN} INTEGER,
                ${ContratoActivos.Columnas.COLUMN_HORARIO} TEXT,
                PRIMARY KEY (${ContratoActivos.Columnas._ID}, ${ContratoActivos.Columnas.COLUMN_INICIO}, ${ContratoActivos.Columnas.COLUMN_FIN}),
                FOREIGN KEY (${ContratoActivos.Columnas._ID}) REFERENCES ${ContratoMedicamentos.NOMBRE_TABLA}(${ContratoMedicamentos.Columnas._ID})
            )
        """

        private const val CREATE_FAVORITE_TABLE = """
            CREATE TABLE IF NOT EXISTS ${ContratoFavoritos.NOMBRE_TABLA} (
                ${ContratoFavoritos.Columnas._ID} INTEGER PRIMARY KEY,
                FOREIGN KEY (${ContratoFavoritos.Columnas._ID}) REFERENCES ${ContratoMedicamentos.NOMBRE_TABLA}(${ContratoMedicamentos.Columnas._ID})
            )
        """

        private const val CREATE_AGENDA_TABLE = """
            CREATE TABLE IF NOT EXISTS ${ContratoAgenda.NOMBRE_TABLA} (
                ${ContratoAgenda.Columnas._ID} INTEGER PRIMARY KEY,
                ${ContratoAgenda.Columnas.COLUMN_DESCRIPCION} TEXT
            )
        """

        private const val CREATE_HISTORY_TABLE = """
            CREATE TABLE IF NOT EXISTS ${ContratoHistorial.NOMBRE_TABLA} (
                ${ContratoHistorial.Columnas._ID} INTEGER,
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

    private fun insertIntoMedicamentos(medicamento: Medicamento) {

        val values = ContentValues().apply {
            put(ContratoMedicamentos.Columnas._ID, medicamento.codNacional)
            put(ContratoMedicamentos.Columnas.COLUMN_NOMBRE, medicamento.nombre)
            put(ContratoMedicamentos.Columnas.COLUMN_FICHA_TECNICA, medicamento.fichaTecnica)
            put(ContratoMedicamentos.Columnas.COLUMN_PROSPECTO, medicamento.prospecto)
        }

        writableDatabase.use { db ->
            db.insert(ContratoMedicamentos.NOMBRE_TABLA, null, values)
        }
    }

    fun insertIntoActivos(medicamento: Medicamento): Boolean {
        if (!existeEnMedicamentos(medicamento)) {
            insertIntoMedicamentos(medicamento)
        }

        if (medicamento.isFavorite) {
            insertIntoFavoritos(medicamento)
        }

        val values = ContentValues().apply {
            put(ContratoActivos.Columnas._ID, medicamento.codNacional)
            put(ContratoActivos.Columnas.COLUMN_INICIO, medicamento.fechaInicio)
            put(ContratoActivos.Columnas.COLUMN_FIN, medicamento.fechaFin)
            put(ContratoActivos.Columnas.COLUMN_HORARIO, Gson().toJson(medicamento.horario))
        }

        writableDatabase.use { db ->
            return db.insert(ContratoActivos.NOMBRE_TABLA, null, values) != -1L
        }
    }

    fun deleteFromActivos(medicamento: Medicamento): Boolean {
        writableDatabase.use { db ->
            return db.delete(
                ContratoActivos.NOMBRE_TABLA,
                "${ContratoActivos.Columnas._ID} = ? AND " +
                        "${ContratoActivos.Columnas.COLUMN_INICIO} = ? AND " +
                        "${ContratoActivos.Columnas.COLUMN_FIN} = ?",
                arrayOf(
                    medicamento.codNacional.toString(),
                    medicamento.fechaInicio.toString(),
                    medicamento.fechaFin.toString()
                )
            ) != 0
        }
    }

    fun insertIntoFavoritos(medicamento: Medicamento): Boolean {
        if (!existeEnMedicamentos(medicamento)) {
            insertIntoMedicamentos(medicamento)
        }

        val values = ContentValues().apply {
            put(ContratoFavoritos.Columnas._ID, medicamento.codNacional)
        }

        writableDatabase.use { db ->
            return db.insert(ContratoFavoritos.NOMBRE_TABLA, null, values) != -1L
        }
    }

    fun deleteFromFavoritos(medicamento: Medicamento): Boolean {
        writableDatabase.use { db ->
            return db.delete(
                ContratoFavoritos.NOMBRE_TABLA,
                "${ContratoFavoritos.Columnas._ID} = ?",
                arrayOf(medicamento.codNacional.toString())
            ) != 0
        }
    }

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

    fun insertIntoHistorial(medicamento: Medicamento): Boolean {
        val values = ContentValues().apply {
            put(ContratoHistorial.Columnas._ID, medicamento.codNacional)
            put(ContratoHistorial.Columnas.COLUMN_INICIO, medicamento.fechaInicio)
            put(ContratoHistorial.Columnas.COLUMN_FIN, medicamento.fechaInicio)
        }

        writableDatabase.use { db ->
            return db.insert(ContratoHistorial.NOMBRE_TABLA, null, values) != -1L
        }
    }

    fun existeEnAgenda(fecha: Long): Boolean {
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

    fun existeEnMedicamentos(medicamento: Medicamento): Boolean {
        readableDatabase.use { db ->
            db.query(
                ContratoMedicamentos.NOMBRE_TABLA,
                null,
                "${ContratoMedicamentos.Columnas._ID} = ?",
                arrayOf(medicamento.codNacional.toString()),
                null,
                null,
                null
            ).use { cursor ->
                return cursor.moveToFirst()
            }
        }
    }

    fun existeEnFavoritos(medicamento: Medicamento): Boolean {
        return existeEnFavoritos(medicamento.codNacional)
    }

    private fun existeEnFavoritos(codNacional: Int): Boolean {
        readableDatabase.use { db ->
            db.query(
                ContratoMedicamentos.NOMBRE_TABLA + " INNER JOIN " + ContratoFavoritos.NOMBRE_TABLA + " ON " + ContratoMedicamentos.NOMBRE_TABLA + "." + ContratoMedicamentos.Columnas._ID + " = " + ContratoFavoritos.NOMBRE_TABLA + "." + ContratoFavoritos.Columnas._ID,
                null,
                "${
                    ContratoFavoritos.NOMBRE_TABLA
                }.${ContratoFavoritos.Columnas._ID} = ?",
                arrayOf(codNacional.toString()),
                null,
                null,
                null
            ).use { cursor ->
                return cursor.moveToFirst()
            }
        }
    }

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
                    val colCodNacional =
                        cursor.getColumnIndex(ContratoMedicamentos.Columnas._ID)

                    val colNombre =
                        cursor.getColumnIndex(ContratoMedicamentos.Columnas.COLUMN_NOMBRE)

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
                                cursor.getInt(colCodNacional),
                                cursor.getString(colNombre),
                                cursor.getString(colFichaTecnica),
                                cursor.getString(colProspecto),
                                cursor.getLong(colFechaInicio),
                                cursor.getLong(colFechaFin),
                                Gson().fromJson(
                                    cursor.getString(colHorario),
                                    object : TypeToken<List<Long>>() {}.type
                                ),
                                existeEnFavoritos(cursor.getInt(colCodNacional))
                            )
                        )
                    } while (cursor.moveToNext())
                }
            }
        }

        return listaActivos
    }

    // TODO: Borrar
    @Deprecated("Marked for removal")
    fun ejemplosActivos() {
        insertIntoActivos(
            Medicamento(
                1,
                "ASPIRINA C 400 mg/240 mg COMPRIMIDOS EFERVESCENTES1",
                "https://cima.aemps.es/cima/pdfs/ft/51347/FT_51347.pdf",
                "https://cima.aemps.es/cima/pdfs/p/51347/P_51347.pdf",
                1700866800000,
                1701730800000,
                listOf(-3600000, 25200000, 54000000),
                true
            )
        )

        insertIntoActivos(
            Medicamento(
                2,
                "ASPIRINA C 400 mg/240 mg COMPRIMIDOS EFERVESCENTES2",
                "https://cima.aemps.es/cima/pdfs/ft/51347/FT_51347.pdf",
                "https://cima.aemps.es/cima/pdfs/p/51347/P_51347.pdf",
                1700866800000,
                1701730800000,
                listOf(-3600000, 25200000, 54000000),
                false
            )
        )

        insertIntoActivos(
            Medicamento(
                3,
                "ASPIRINA C 400 mg/240 mg COMPRIMIDOS EFERVESCENTES3",
                "https://cima.aemps.es/cima/pdfs/ft/51347/FT_51347.pdf",
                "https://cima.aemps.es/cima/pdfs/p/51347/P_51347.pdf",
                1700866800000,
                1701730800000,
                listOf(-3600000, 25200000, 54000000),
                true
            )
        )

        insertIntoActivos(
            Medicamento(
                4,
                "ASPIRINA C 400 mg/240 mg COMPRIMIDOS EFERVESCENTES4",
                "https://cima.aemps.es/cima/pdfs/ft/51347/FT_51347.pdf",
                "https://cima.aemps.es/cima/pdfs/p/51347/P_51347.pdf",
                1700866800000,
                1701730800000,
                listOf(-3600000, 25200000, 54000000),
                false
            )
        )

        insertIntoActivos(
            Medicamento(
                5,
                "ASPIRINA C 400 mg/240 mg COMPRIMIDOS EFERVESCENTES5",
                "https://cima.aemps.es/cima/pdfs/ft/51347/FT_51347.pdf",
                "https://cima.aemps.es/cima/pdfs/p/51347/P_51347.pdf",
                1700866800000,
                1701730800000,
                listOf(-3600000, 25200000, 54000000),
                true
            )
        )

        insertIntoActivos(
            Medicamento(
                6,
                "ASPIRINA C 400 mg/240 mg COMPRIMIDOS EFERVESCENTES6",
                "https://cima.aemps.es/cima/pdfs/ft/51347/FT_51347.pdf",
                "https://cima.aemps.es/cima/pdfs/p/51347/P_51347.pdf",
                1700866800000,
                1701730800000,
                listOf(-3600000, 25200000, 54000000),
                false
            )
        )

        insertIntoActivos(
            Medicamento(
                7,
                "ASPIRINA C 400 mg/240 mg COMPRIMIDOS EFERVESCENTES7",
                "https://cima.aemps.es/cima/pdfs/ft/51347/FT_51347.pdf",
                "https://cima.aemps.es/cima/pdfs/p/51347/P_51347.pdf",
                1704063600000,
                1704495600000,
                listOf(-3600000, 25200000, 54000000),
                true
            )
        )

        insertIntoActivos(
            Medicamento(
                8,
                "ASPIRINA C 400 mg/240 mg COMPRIMIDOS EFERVESCENTES8",
                "https://cima.aemps.es/cima/pdfs/ft/51347/FT_51347.pdf",
                "https://cima.aemps.es/cima/pdfs/p/51347/P_51347.pdf",
                1704063600000,
                1704495600000,
                listOf(-3600000, 25200000, 54000000),
                false
            )
        )

        insertIntoActivos(
            Medicamento(
                9,
                "ASPIRINA C 400 mg/240 mg COMPRIMIDOS EFERVESCENTES9",
                "https://cima.aemps.es/cima/pdfs/ft/51347/FT_51347.pdf",
                "https://cima.aemps.es/cima/pdfs/p/51347/P_51347.pdf",
                1704063600000,
                1704495600000,
                listOf(-3600000, 25200000, 54000000),
                true
            )
        )
    }
}
