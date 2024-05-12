package com.a23pablooc.proxectofct.data.database.entities

import androidx.room.Embedded
import androidx.room.Relation
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoFavoritoTable
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoTable

data class MedicamentoFavoritoAndMedicamento(
    @Embedded
    val medicamentoFavoritoEntity: MedicamentoFavoritoEntity,

    @Relation(
        parentColumn = MedicamentoFavoritoTable.Columns.ID,
        entityColumn = MedicamentoTable.Columns.ID
    )
    val medicamento: MedicamentoEntity
)