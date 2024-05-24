package com.a23pablooc.proxectofct.data.database.definitions

object MedicamentoFavoritoTable {
const val TABLE_NAME = "tbl_medicamentos_favoritos"

    object Columns {
        const val ID = "PK_medicamento_favorito"
        const val FK_MEDICAMENTO = "FK_medicamento"
        const val FK_USUARIO = "FK_usuario"
    }

    object Indexes {
        const val IDX_MEDICAMENTO_FAVORITO_FK_MEDICAMENTO = "idx_medicamento_favorito_fk_medicamento"
        const val IDX_MEDICAMENTO_FAVORITO_FK_USUARIO = "idx_medicamento_favorito_fk_usuario"
    }
}