package com.cocot3ro.mipastillero.data.database.definitions

object MedicamentoActivoTableDefinition {
    const val TABLE_NAME = "tbl_medicamentos_activos"

    object Columns {
        const val PK_MEDICAMENTO_ACTIVO = "${TABLE_NAME}__pk_medicamento_activo"
        const val FK_MEDICAMENTO = "${TABLE_NAME}__fk_medicamento"
        const val FK_USUARIO = "${TABLE_NAME}__fk_usuario"
        const val FECHA_INICIO = "${TABLE_NAME}__fecha_inicio"
        const val FECHA_FIN = "${TABLE_NAME}__fecha_fin"
        const val HORARIO = "${TABLE_NAME}__horario"
        const val DOSIS = "${TABLE_NAME}__dosis"
        const val TOMAS = "${TABLE_NAME}__tomas"
    }

    object Indexes {
        const val IDX_MEDICAMENTO_ACTIVO_FK_MEDICAMENTO = "${TABLE_NAME}__idx_medicamento_activo_fk_medicamento"
        const val IDX_MEDICAMENTO_ACTIVO_FK_USUARIO = "${TABLE_NAME}__idx_medicamento_activo_fk_usuario"
    }
}