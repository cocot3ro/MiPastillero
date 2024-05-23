package com.a23pablooc.proxectofct.old.old_model

import android.provider.BaseColumns

/**
 * Clase que representa la tabla de Agenda
 * @property NOMBRE_TABLA nombre de la tabla
 * @property Columnas columnas de la tabla
 */
@Deprecated("old")
object ContratoAgenda {
    const val NOMBRE_TABLA = "Agenda"

    object Columnas : BaseColumns {
        const val _ID = "fecha"
        const val COLUMN_DESCRIPCION = "descripcion"
    }
}