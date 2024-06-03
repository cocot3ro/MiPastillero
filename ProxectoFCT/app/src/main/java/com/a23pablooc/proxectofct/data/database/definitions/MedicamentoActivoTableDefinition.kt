package com.a23pablooc.proxectofct.data.database.definitions

object MedicamentoActivoTableDefinition {
    const val TABLE_NAME = "tbl_medicamentos_activos"

    object Columns {
        const val PK_MEDICAMENTO_ACTIVO = "${TABLE_NAME}_pk_medicamento_activo"
        const val FK_MEDICAMENTO = "${TABLE_NAME}_fk_medicamento"
        const val FK_USUARIO = "${TABLE_NAME}_fk_usuario"
        const val FECHA_INICIO = "${TABLE_NAME}_fecha_inicio"
        const val FECHA_FIN = "${TABLE_NAME}_fecha_fin"
        const val HORARIO = "${TABLE_NAME}_horario"
        const val DOSIS = "${TABLE_NAME}_dosis"
        const val TOMAS = "${TABLE_NAME}_tomas"
    }

    object Indexes {
        const val IDX_MEDICAMENTO_ACTIVO_FK_MEDICAMENTO = "${TABLE_NAME}_idx_medicamento_activo_fk_medicamento"
        const val IDX_MEDICAMENTO_ACTIVO_FK_USUARIO = "${TABLE_NAME}_idx_medicamento_activo_fk_usuario"
    }
}