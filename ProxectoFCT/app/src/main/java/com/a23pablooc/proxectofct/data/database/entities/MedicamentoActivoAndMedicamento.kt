package com.a23pablooc.proxectofct.data.database.entities

import androidx.room.Embedded
import androidx.room.Relation
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoActivoTable
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoTable

data class MedicamentoActivoAndMedicamento(
    @Embedded
    val medicamentoActivoEntity: MedicamentoActivoEntity,

    @Relation(
        parentColumn = MedicamentoActivoTable.Columns.PK_MEDICAMENTO_ACTIVO,
        entityColumn = MedicamentoTable.Columns.PK_COD_NACIONAL_MEDICAMENTO
    )
    val medicamento: MedicamentoEntity
)