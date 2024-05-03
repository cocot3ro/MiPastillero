package com.a23pablooc.proxectofct.data.database.table_definitions

object MedicamentoActivoTable {
    const val TABLE_NAME = "medicamentos_activos"

    object Columns {
        const val ID = "PK_medicamento_activo"
        const val FK_MEDICAMENTO = "FK_medicamento"
        const val FK_USUARIO = "FK_usuario"
        const val FECHA_INICIO = "fecha_inicio"
        const val FECHA_FIN = "fecha_fin"
        const val HORARIO = "horario"
        const val DOSIS = "dosis"
    }
}