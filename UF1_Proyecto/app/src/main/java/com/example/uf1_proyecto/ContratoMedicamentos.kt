package com.example.uf1_proyecto

import android.provider.BaseColumns

object ContratoMedicamentos {
    const val NOMBRE_TABLA = "medicamentos"

    object Columnas : BaseColumns {
        const val COLUMN_COD_NACIONAL = "codNacional"
        const val COLUMN_NOMBRE = "nombre"
        const val COLUMN_FICHA_TECNICA = "fichaTecnica"
        const val COLUMN_PROSPECTO = "prospecto"
    }
}