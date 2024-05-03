package com.a23pablooc.proxectofct.data.database.table_definitions

object HistorialTable {
    const val TABLE_NAME = "historial"

    object Columns {
        const val ID = "id"
        const val FK_MEDICAMENTO = "fk_medicamento"
        const val FECHA_INICIO = "fecha_inicio"
        const val FECHA_FIN = "fecha_fin"

    }
}