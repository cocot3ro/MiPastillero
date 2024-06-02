package com.a23pablooc.proxectofct.data.database.definitions

object MedicamentoTableDefinition {
    const val TABLE_NAME = "tbl_medicamentos"

    object Columns {
        const val PK_COD_NACIONAL_MEDICAMENTO = TABLE_NAME + "_" + "pk_cod_nacional_medicamento"
        const val FK_USUARIO = TABLE_NAME + "_" + "fk_usuario"
        const val NUM_REGISTRO = TABLE_NAME + "_" + "num_registro"
        const val NOMBRE = TABLE_NAME + "_" + "nombre"
        const val ALIAS = TABLE_NAME + "_" + "alias"
        const val URL = TABLE_NAME + "_" + "url"
        const val PROSPECTO = TABLE_NAME + "_" + "prospecto"
        const val IMAGEN = TABLE_NAME + "_" + "imagen"
        const val LABORATORIO = TABLE_NAME + "_" + "laboratorio"
        const val PRESCRIPCION = TABLE_NAME + "_" + "prescripcion"
        const val AFECTA_CONDUCCION = TABLE_NAME + "_" + "afecta_conduccion"
        const val ES_FAVORITO = TABLE_NAME + "_" + "es_favorito"
    }

    object Indexes {
        const val IDX_MEDICAMENTO_USER_ID = TABLE_NAME + "_" + "idx_medicamento_user_id"
    }
}