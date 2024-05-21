package com.a23pablooc.proxectofct.old_model

import android.provider.BaseColumns

/**
 * Clase que representa la tabla de favoritos
 * @property NOMBRE_TABLA nombre de la tabla
 * @property Columnas columnas de la tabla
 */
object ContratoFavoritos {
    const val NOMBRE_TABLA = "favoritos"

    object Columnas : BaseColumns {
        const val _ID = "nombre"
    }
}