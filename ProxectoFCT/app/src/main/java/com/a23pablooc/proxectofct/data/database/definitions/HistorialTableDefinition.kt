package com.a23pablooc.proxectofct.data.database.definitions

object HistorialTableDefinition {
    const val TABLE_NAME = "tbl_historial"

    object Columns {
        const val PK_HISTORIAL = TABLE_NAME + "_" + "pk_historial"
        const val FK_MEDICAMENTO = TABLE_NAME + "_" + "fk_medicamento"
        const val FK_USUARIO = TABLE_NAME + "_" + "fk_usuario"
        const val FECHA_INICIO = TABLE_NAME + "_" + "fecha_inicio"
        const val FECHA_FIN = TABLE_NAME + "_" + "fecha_fin"
    }

    object Indexes {
        const val IDX_HISTORIAL_FK_USUARIO = TABLE_NAME + "_" + "idx_historial_fk_usuario"
        const val IDX_HISTORIAL_FK_MEDICAMENTO = TABLE_NAME + "_" + "idx_historial_fk_medicamento"
    }
}