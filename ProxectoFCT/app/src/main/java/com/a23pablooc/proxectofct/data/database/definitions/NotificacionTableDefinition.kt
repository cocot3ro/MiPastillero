package com.a23pablooc.proxectofct.data.database.definitions

object NotificacionTableDefinition {
    const val TABLE_NAME = "tbl_notificaciones"

    object Columns {
        const val PK_NOTIFICACION = "${TABLE_NAME}_pk_notificacion"
        const val FK_MEDICAMENTO_ACTIVO = "${TABLE_NAME}_fk_medicamento_activo"
        const val FK_USUARIO = "${TABLE_NAME}_fk_usuario"
        const val FECHA = "${TABLE_NAME}_fecha"
        const val HORA = "${TABLE_NAME}_hora"
    }

    object Indexes {
        const val IDX_NOTIFICACION_FK_MEDICAMENTO = "${TABLE_NAME}_idx_notificacion_fk_medicamento"
        const val IDX_NOTIFICACION_FK_USUARIO = "${TABLE_NAME}_idx_notificacion_fk_usuario"
    }
}