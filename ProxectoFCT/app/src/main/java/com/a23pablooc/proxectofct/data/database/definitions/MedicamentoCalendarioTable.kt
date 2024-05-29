package com.a23pablooc.proxectofct.data.database.definitions

object MedicamentoCalendarioTable {
    const val TABLE_NAME = "tbl_medicamentos_calendario"

    object Columns {
        const val PK_MEDICAMENTO_CALENDARIO = "pk_medicamento_calendario"
        const val FK_MEDICAMENTO = "fk_medicamento"
        const val FK_USUARIO = "fk_usuario"
        const val FECHA = "fecha"
        const val HORA = "hora"
        const val TOMADO = "tomado"
    }

    object Indexes {
        const val IDX_MEDICAMENTO_CALENDARIO_FK_MEDICAMENTO = "idx_medicamento_calendario_fk_medicamento"
        const val IDX_MEDICAMENTO_CALENDARIO_FK_USUARIO = "idx_medicamento_calendario_fk_usuario"
    }
}