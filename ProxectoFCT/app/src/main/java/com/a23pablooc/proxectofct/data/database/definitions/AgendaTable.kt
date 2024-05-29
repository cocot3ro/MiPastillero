package com.a23pablooc.proxectofct.data.database.definitions

object AgendaTable {
    const val TABLE_NAME = "tbl_agenda"

    object Columns {
        const val PK_AGENDA = "id_agenda"
        const val FK_USUARIO = "fk_usuario"
        const val FECHA = "fecha"
        const val HORA = "hora"
    }

    object Indexes {
        const val IDX_AGENDA_FK_USUARIO = "idx_agenda_fk_usuario"
    }
}