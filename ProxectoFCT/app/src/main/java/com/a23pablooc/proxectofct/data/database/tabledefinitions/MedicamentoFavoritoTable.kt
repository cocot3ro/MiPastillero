package com.a23pablooc.proxectofct.data.database.tabledefinitions

object MedicamentoFavoritoTable {
const val TABLE_NAME = "medicamentos_favoritos"

    object Columns {
        const val ID = "PK_medicamento_favorito"
        const val ID_MEDICAMENTO = "FK_medicamento"
        const val ID_USUARIO = "FK_usuario"
    }
}