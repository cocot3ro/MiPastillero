package com.a23pablooc.proxectofct.data.database.definitions

object MedicamentoTableDefinition {
    const val TABLE_NAME = "tbl_medicamentos"

    object Columns {
        const val PK_COD_NACIONAL_MEDICAMENTO = "${TABLE_NAME}__pk_cod_nacional_medicamento"
        const val FK_USUARIO = "${TABLE_NAME}__fk_usuario"
        const val NUM_REGISTRO = "${TABLE_NAME}__num_registro"
        const val NOMBRE = "${TABLE_NAME}__nombre"
        const val URL = "${TABLE_NAME}__url"
        const val PROSPECTO = "${TABLE_NAME}__prospecto"
        const val IMAGEN = "${TABLE_NAME}__imagen"
        const val LABORATORIO = "${TABLE_NAME}__laboratorio"
        const val RECETA = "${TABLE_NAME}__receta"
        const val AFECTA_CONDUCCION = "${TABLE_NAME}__afecta_conduccion"
        const val ES_FAVORITO = "${TABLE_NAME}__es_favorito"
        const val PRESCRIPCION = "${TABLE_NAME}__prescripcion"
        const val PRINCIPIOS_ACTIVOS = "${TABLE_NAME}__principios_activos"
    }

    object Indexes {
        const val IDX_MEDICAMENTO_USER_ID = "${TABLE_NAME}__idx_medicamento_user_id"
    }
}