package com.a23pablooc.proxectofct.data.database.definitions

object NotificacionTable {
    const val TABLE_NAME = "tbl_notificaciones"

    object Columns {
        const val PK_NOTIFICACION = "pk_notificacion"
        const val FK_MEDICAMENTO = "fk_medicamento"
        const val FK_USUARIO = "fk_usuario"
        const val FECHA = "fecha"
        const val HORA = "hora"
    }

    object Indexes {
        const val IDX_NOTIFICACION_FK_MEDICAMENTO = "idx_notificacion_fk_medicamento"
        const val IDX_NOTIFICACION_FK_USUARIO = "idx_notificacion_fk_usuario"
    }
}