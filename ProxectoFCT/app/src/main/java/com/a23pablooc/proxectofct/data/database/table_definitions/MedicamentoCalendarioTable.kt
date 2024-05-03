package com.a23pablooc.proxectofct.data.database.table_definitions

object MedicamentoCalendarioTable {
    const val TABLE_NAME = "medicamentos_calendario"

    object Columns {
        const val ID = "PK_medicamento_calendario"
        const val FK_MEDICAMENTO = "FK_medicamento"
        const val FK_USUARIO = "FK_usuario"
        const val FECHA = "fecha"
        const val HORA = "hora"
        const val TOMADO = "tomado"
    }
}