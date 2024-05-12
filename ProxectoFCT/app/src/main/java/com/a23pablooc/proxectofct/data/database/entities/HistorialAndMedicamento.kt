package com.a23pablooc.proxectofct.data.database.entities

import androidx.room.Embedded
import androidx.room.Relation
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoTable
import com.a23pablooc.proxectofct.data.database.definitions.NotificacionTable

data class HistorialAndMedicamento(
    @Embedded
    val historialEntity: HistorialEntity,

    @Relation(
        parentColumn = NotificacionTable.Columns.ID,
        entityColumn = MedicamentoTable.Columns.ID
    )
    val medicamento: MedicamentoEntity
)