package com.example.uf1_proyecto

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MiDBHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        const val DB_NAME = "pillbox.db"
        const val DB_VERSION = 1

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
}