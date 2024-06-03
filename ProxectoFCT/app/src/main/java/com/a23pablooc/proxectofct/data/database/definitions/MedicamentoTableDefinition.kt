package com.a23pablooc.proxectofct.data.database.definitions

object MedicamentoTableDefinition {
    const val TABLE_NAME = "tbl_medicamentos"

    object Columns {
        const val PK_COD_NACIONAL_MEDICAMENTO = "${TABLE_NAME}_pk_cod_nacional_medicamento"
        const val FK_USUARIO = "${TABLE_NAME}_fk_usuario"
        const val NUM_REGISTRO = "${TABLE_NAME}_num_registro"
        const val NOMBRE = "${TABLE_NAME}_nombre"
        const val URL = "${TABLE_NAME}_url"
        const val PROSPECTO = "${TABLE_NAME}_prospecto"
        const val IMAGEN = "${TABLE_NAME}_imagen"
        const val LABORATORIO = "${TABLE_NAME}_laboratorio"
        const val PRESCRIPCION = "${TABLE_NAME}_prescripcion"
        const val AFECTA_CONDUCCION = "${TABLE_NAME}_afecta_conduccion"
        const val ES_FAVORITO = "${TABLE_NAME}_es_favorito"
    }

    object Indexes {
        const val IDX_MEDICAMENTO_USER_ID = "${TABLE_NAME}_idx_medicamento_user_id"
    }
}