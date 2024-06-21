package com.a23pablooc.proxectofct.data.database.definitions

object AgendaTableDefinition {
    const val TABLE_NAME = "tbl_agenda"

    object Columns {
        const val PK_FECHA = "${TABLE_NAME}__pk_fecha"
        const val FK_USUARIO = "${TABLE_NAME}__fk_usuario"
        const val DESCRIPCION = "${TABLE_NAME}__descripcion"
    }

    object Indexes {
        const val IDX_AGENDA_FK_USUARIO = "${TABLE_NAME}__idx_agenda_fk_usuario"
    }
}