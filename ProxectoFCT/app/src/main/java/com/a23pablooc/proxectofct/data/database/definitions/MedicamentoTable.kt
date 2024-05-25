package com.a23pablooc.proxectofct.data.database.definitions

object MedicamentoTable {
    const val TABLE_NAME = "tbl_medicamentos"

    object Columns {
        const val ID = "PK_medicamento"
        const val FK_USUARIO = "FK_usuario"
        const val NUM_REGISTRO = "num_registro"
        const val NOMBRE = "nombre"
        const val URL = "url"
        const val PROSPECTO = "prospecto"
        const val IMAGEN = "imagen"
        const val LABORATORIO = "laboratorio"
        const val PRESCRIPCION = "prescripcion"
        const val AFECTA_CONDUCCION = "afecta_conduccion"
        const val ES_FAVORITO = "es_favorito"
    }

    object Indexes {
        const val IDX_MEDICAMENTO_USER_ID = "idx_medicamento_user_id"
    }
}