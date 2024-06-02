package com.a23pablooc.proxectofct.data.database.entities

import androidx.room.Embedded
import androidx.room.Relation
import com.a23pablooc.proxectofct.data.database.definitions.HistorialTableDefinition
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoTableDefinition

data class HistorialAndMedicamento(
    @Embedded
    val historialEntity: HistorialEntity,

    @Relation(
        parentColumn = HistorialTableDefinition.Columns.PK_HISTORIAL,
        entityColumn = MedicamentoTableDefinition.Columns.PK_COD_NACIONAL_MEDICAMENTO
    )
    val medicamento: MedicamentoEntity
)