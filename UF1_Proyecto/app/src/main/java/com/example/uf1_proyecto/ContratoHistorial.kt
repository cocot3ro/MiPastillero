package com.example.uf1_proyecto

import android.provider.BaseColumns

object ContratoHistorial {
    const val NOMBRE_TABLA = "Historial"

    object Columnas : BaseColumns {
        const val COLUMN_COD_NACIONAL = "codNacional"
        const val COLUMN_INICIO = "fechaInicio"
        const val COLUMN_FIN = "fechaFin"
    }
}