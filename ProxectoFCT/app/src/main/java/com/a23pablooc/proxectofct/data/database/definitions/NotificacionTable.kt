package com.a23pablooc.proxectofct.data.database.definitions

object NotificacionTable {
    const val TABLE_NAME = "tbl_notificaciones"

    object Columns {
        const val ID = "id"
        const val FK_MEDICAMENTO = "FK_medicamento"
        const val FK_USUARIO = "FK_usuario"
        const val FECHA = "fecha"
        const val HORA = "hora"
    }
}