package com.cocot3ro.mipastillero.data.database.definitions

object NotificacionTableDefinition {
    const val TABLE_NAME = "tbl_notificaciones"

    object Columns {
        const val PK_NOTIFICACION = "${TABLE_NAME}__pk_notificacion"
        const val FK_MEDICAMENTO_ACTIVO = "${TABLE_NAME}__fk_medicamento_activo"
        const val FK_USUARIO = "${TABLE_NAME}__fk_usuario"
        const val TIME_STAMP = "${TABLE_NAME}__time_stamp"
    }

    object Indexes {
        const val IDX_NOTIFICACION_FK_MEDICAMENTO = "${TABLE_NAME}__idx_notificacion_fk_medicamento"
        const val IDX_NOTIFICACION_FK_USUARIO = "${TABLE_NAME}__idx_notificacion_fk_usuario"
    }
}