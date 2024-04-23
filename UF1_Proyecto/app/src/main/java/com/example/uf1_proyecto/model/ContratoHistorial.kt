package com.example.uf1_proyecto.model

import android.provider.BaseColumns

/**
 * Clase que representa la tabla de Historial
 * @property NOMBRE_TABLA nombre de la tabla
 * @property Columnas columnas de la tabla
 */
object ContratoHistorial {
    const val NOMBRE_TABLA = "Historial"

    object Columnas : BaseColumns {
        const val _ID = "nombre"
        const val COLUMN_INICIO = "fechaInicio"
        const val COLUMN_FIN = "fechaFin"
        const val COLUMN_HORARIO = "horario"
    }
}