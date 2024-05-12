package com.a23pablooc.proxectofct.data.database.entities

import androidx.room.Embedded
import androidx.room.Relation
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoActivoTable
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoTable

data class MedicamentoActivoAndMedicamento(
    @Embedded
    val medicamentoActivoEntity: MedicamentoActivoEntity,
    
    @Relation(
        parentColumn = MedicamentoActivoTable.Columns.ID,
        entityColumn = MedicamentoTable.Columns.ID
    )
    val medicamento: MedicamentoEntity
)