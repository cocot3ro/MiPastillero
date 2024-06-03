package com.a23pablooc.proxectofct.data.database.definitions

object HistorialTableDefinition {
    const val TABLE_NAME = "tbl_historial"

    object Columns {
        const val PK_HISTORIAL = "${TABLE_NAME}_pk_historial"
        const val FK_MEDICAMENTO = "${TABLE_NAME}_fk_medicamento"
        const val FK_USUARIO = "${TABLE_NAME}_fk_usuario"
        const val FECHA_INICIO = "${TABLE_NAME}_fecha_inicio"
        const val FECHA_FIN = "${TABLE_NAME}_fecha_fin"
    }

    object Indexes {
        const val IDX_HISTORIAL_FK_USUARIO = "${TABLE_NAME}_idx_historial_fk_usuario"
        const val IDX_HISTORIAL_FK_MEDICAMENTO = "${TABLE_NAME}_idx_historial_fk_medicamento"
    }
}