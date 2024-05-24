package com.a23pablooc.proxectofct.data.database.definitions

object HistorialTable {
    const val TABLE_NAME = "tbl_historial"

    object Columns {
        const val ID = "id"
        const val FK_MEDICAMENTO = "fk_medicamento"
        const val FK_USUARIO = "fk_usuario"
        const val FECHA_INICIO = "fecha_inicio"
        const val FECHA_FIN = "fecha_fin"
    }

    object Indexes {
        const val IDX_HISTORIAL_FK_USUARIO = "idx_historial_fk_usuario"
        const val IDX_HISTORIAL_FK_MEDICAMENTO = "idx_historial_fk_medicamento"
    }
}