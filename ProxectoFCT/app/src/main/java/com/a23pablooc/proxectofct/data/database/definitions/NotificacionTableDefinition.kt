package com.a23pablooc.proxectofct.data.database.definitions

object NotificacionTableDefinition {
    const val TABLE_NAME = "tbl_notificaciones"

    object Columns {
        const val PK_NOTIFICACION = TABLE_NAME + "_" + "pk_notificacion"
        const val FK_MEDICAMENTO_ACTIVO = TABLE_NAME + "_" + "fk_medicamento_activo"
        const val FK_USUARIO = TABLE_NAME + "_" + "fk_usuario"
        const val FECHA = TABLE_NAME + "_" + "fecha"
        const val HORA = TABLE_NAME + "_" + "hora"
    }

    object Indexes {
        const val IDX_NOTIFICACION_FK_MEDICAMENTO = TABLE_NAME + "_" + "idx_notificacion_fk_medicamento"
        const val IDX_NOTIFICACION_FK_USUARIO = TABLE_NAME + "_" + "idx_notificacion_fk_usuario"
    }
}