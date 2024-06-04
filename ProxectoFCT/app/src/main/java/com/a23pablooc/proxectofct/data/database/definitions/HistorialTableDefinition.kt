package com.a23pablooc.proxectofct.data.database.definitions

object HistorialTableDefinition {
    const val TABLE_NAME = "tbl_historial"

    object Columns {
        const val PK_HISTORIAL = "${TABLE_NAME}__pk_historial"
        const val FK_USUARIO = "${TABLE_NAME}__fk_usuario"
    }

    object Indexes {
        const val IDX_HISTORIAL_FK_USUARIO = "${TABLE_NAME}__idx_historial_fk_usuario"
    }

    object Prefixes {
        const val MEDICAMENTO = "${TABLE_NAME}__medicamento-"
        const val MEDICAMENTO_ACTIVO = "${TABLE_NAME}__medicamento_activo-"
    }
}