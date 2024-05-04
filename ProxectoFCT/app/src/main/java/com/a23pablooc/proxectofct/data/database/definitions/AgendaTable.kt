package com.a23pablooc.proxectofct.data.database.definitions

object AgendaTable {
    const val TABLE_NAME = "tbl_agenda"

    object Columns {
        const val ID = "id"
        const val FK_USUARIO = "fk_usuario"
        const val FECHA = "fecha"
        const val HORA = "hora"
    }
}