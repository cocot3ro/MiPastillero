package com.a23pablooc.proxectofct.data.database.definitions

object AgendaTableDefinition {
    const val TABLE_NAME = "tbl_agenda"

    object Columns {
        const val PK_AGENDA = TABLE_NAME + "_" + "id_agenda"
        const val FK_USUARIO = TABLE_NAME + "_" + "fk_usuario"
        const val FECHA = TABLE_NAME + "_" + "fecha"
        const val HORA = TABLE_NAME + "_" + "hora"
    }

    object Indexes {
        const val IDX_AGENDA_FK_USUARIO = TABLE_NAME + "_" + "idx_agenda_fk_usuario"
    }
}