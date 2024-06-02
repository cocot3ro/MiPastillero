package com.a23pablooc.proxectofct.data.database.entities

import androidx.room.Embedded
import androidx.room.Relation
import com.a23pablooc.proxectofct.data.database.definitions.HistorialTable
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoTable

data class HistorialAndMedicamento(
    @Embedded
    val historialEntity: HistorialEntity,

    @Relation(
        parentColumn = HistorialTable.Columns.PK_HISTORIAL,
        entityColumn = MedicamentoTable.Columns.PK_COD_NACIONAL_MEDICAMENTO
    )
    val medicamento: MedicamentoEntity
)