package com.example.uf1_proyecto

import android.provider.BaseColumns
import com.example.uf1_proyecto.ContratoMedicamentos.Columnas
import com.example.uf1_proyecto.ContratoMedicamentos.NOMBRE_TABLA

/**
 * Clase que representa la tabla de medicamentos
 * @property NOMBRE_TABLA nombre de la tabla
 * @property Columnas columnas de la tabla
 */
object ContratoMedicamentos {
    const val NOMBRE_TABLA = "medicamentos"

    object Columnas : BaseColumns {
        const val _ID = "nombre"
        const val COLUMN_COD_NACIONAL = "codNacional"
        const val COLUMN_NUM_REGISTRO = "numRegistro"
        const val COLUMN_URL = "url"
        const val COLUMN_FICHA_TECNICA = "fichaTecnica"
        const val COLUMN_PROSPECTO = "prospecto"
        const val COLUMN_IMAGEN = "imagen"
        const val COLUMN_LABORATORIO = "laboratorio"
        const val COLUMN_DOSIS = "dosis"
        const val COLUMN_PRINCIPIOS_ACTIVOS = "principiosActivos"
        const val COLUMN_RECETA = "receta"
    }
}