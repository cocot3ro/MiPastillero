package com.a23pablooc.proxectofct.data.database.definitions

object MedicamentoHistorialTableDefinition {
    const val TABLE_NAME = "tbl_medicamento_historial"

    object Columns {
        const val PK_MEDICAMENTO_HISTORIAL = "${TABLE_NAME}__pk_historial"
        const val FK_USUARIO = "${TABLE_NAME}__fk_usuario"
        const val FK_MEDICAMENTO_ACTIVO = "${TABLE_NAME}__fk_medicamento_activo"
    }

    object Indexes {
        const val IDX_MEDICAMENTO_HISTORIAL_FK_USUARIO = "${TABLE_NAME}__idx_historial_fk_usuario"
    }

    object Prefixes {
        const val PFX_MEDICAMENTO = "pfx_medicamento"
    }
}