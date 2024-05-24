package com.a23pablooc.proxectofct.data.database.definitions

object MedicamentoActivoTable {
    const val TABLE_NAME = "tbl_medicamentos_activos"

    object Columns {
        const val ID = "PK_medicamento_activo"
        const val FK_MEDICAMENTO = "FK_medicamento"
        const val FK_USUARIO = "FK_usuario"
        const val FECHA_INICIO = "fecha_inicio"
        const val FECHA_FIN = "fecha_fin"
        const val HORARIO = "horario"
        const val DOSIS = "dosis"
    }

    object Indexes {
        const val IDX_MEDICAMENTO_ACTIVO_FK_MEDICAMENTO = "idx_medicamento_activo_fk_medicamento"
        const val IDX_MEDICAMENTO_ACTIVO_FK_USUARIO = "idx_medicamento_activo_fk_usuario"
    }
}