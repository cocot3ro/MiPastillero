package com.a23pablooc.proxectofct.data.database.definitions

object MedicamentoCalendarioTable {
    const val TABLE_NAME = "tbl_medicamentos_calendario"

    object Columns {
        const val ID = "PK_medicamento_calendario"
        const val FK_MEDICAMENTO = "FK_medicamento"
        const val FK_USUARIO = "FK_usuario"
        const val FECHA = "fecha"
        const val HORA = "hora"
        const val TOMADO = "tomado"
    }

    object Indexes {
        const val IDX_MEDICAMENTO_CALENDARIO_FK_MEDICAMENTO = "idx_medicamento_calendario_fk_medicamento"
        const val IDX_MEDICAMENTO_CALENDARIO_FK_USUARIO = "idx_medicamento_calendario_fk_usuario"
    }
}