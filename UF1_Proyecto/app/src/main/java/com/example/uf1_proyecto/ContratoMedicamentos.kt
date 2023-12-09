package com.example.uf1_proyecto

import android.provider.BaseColumns

/**
 * Clase que representa la tabla de medicamentos
 * @property NOMBRE_TABLA nombre de la tabla
 * @property Columnas columnas de la tabla
 */
object ContratoMedicamentos {
    const val NOMBRE_TABLA = "medicamentos"

    object Columnas : BaseColumns {
        const val _ID = "nombre"
        const val COLUMN_COD = "codNacional"
        const val COLUMN_FICHA_TECNICA = "fichaTecnica"
        const val COLUMN_PROSPECTO = "prospecto"
    }
}