package com.a23pablooc.proxectofct.data.database.tabledefinitions

object MedicamentoTable {
    const val TABLE_NAME = "medicamentos"

    object Columns {
        const val ID = "PK_medicamento"
        const val NUM_REGISTRO = "num_registro"
        const val NOMBRE = "nombre"
        const val URL = "url"
        const val PROSPECTO = "prospecto"
        const val IMAGEN = "imagen"
        const val LABORATORIO = "laboratorio"
        const val PRESCRIPCION = "prescripcion"
        const val AFECTA_CONDUCCION = "afecta_conduccion"
    }
}