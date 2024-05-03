package com.a23pablooc.proxectofct.data.database.table_definitions

object AgendaTable {
    const val TABLE_NAME = "agenda"

    object Columns {
        const val ID = "id"
        const val FK_USUARIO = "fk_usuario"
        const val FECHA = "fecha"
        const val HORA = "hora"
    }
}