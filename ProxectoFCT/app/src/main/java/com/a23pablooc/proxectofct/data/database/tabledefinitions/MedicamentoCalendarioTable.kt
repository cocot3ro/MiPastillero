package com.a23pablooc.proxectofct.data.database.tabledefinitions

object MedicamentoCalendarioTable {
    const val TABLE_NAME = "medicamentos_calendario"

    object Columns {
        const val ID = "PK_medicamento_calendario"
        const val ID_MEDICAMENTO = "FK_medicamento"
        const val ID_USUARIO = "FK_usuario"
        const val FECHA = "fecha"
        const val HORA = "hora"
        const val TOMADO = "tomado"
    }
}