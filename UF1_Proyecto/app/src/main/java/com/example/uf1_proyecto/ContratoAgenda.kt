package com.example.uf1_proyecto

import android.provider.BaseColumns

object ContratoAgenda {
    const val NOMBRE_TABLA = "Agenda"

    object Columnas : BaseColumns {
        const val COLUMN_FECHA = "fecha"
        const val COLUMN_DESCRIPCION = "descripcion"
    }
}