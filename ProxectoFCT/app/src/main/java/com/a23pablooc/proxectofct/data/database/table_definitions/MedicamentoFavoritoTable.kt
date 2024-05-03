package com.a23pablooc.proxectofct.data.database.table_definitions

object MedicamentoFavoritoTable {
const val TABLE_NAME = "medicamentos_favoritos"

    object Columns {
        const val ID = "PK_medicamento_favorito"
        const val FK_MEDICAMENTO = "FK_medicamento"
        const val FK_USUARIO = "FK_usuario"
    }
}