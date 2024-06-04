package com.a23pablooc.proxectofct.data.database.definitions

object AgendaTableDefinition {
    const val TABLE_NAME = "tbl_agenda"

    object Columns {
        const val PK_AGENDA = "${TABLE_NAME}__id_agenda"
        const val FK_USUARIO = "${TABLE_NAME}__fk_usuario"
        const val FECHA = "${TABLE_NAME}__fecha"
        const val HORA = "${TABLE_NAME}__hora"
    }

    object Indexes {
        const val IDX_AGENDA_FK_USUARIO = "${TABLE_NAME}__idx_agenda_fk_usuario"
    }
}