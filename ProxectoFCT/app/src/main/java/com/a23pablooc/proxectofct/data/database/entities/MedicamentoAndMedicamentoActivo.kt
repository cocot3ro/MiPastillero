package com.a23pablooc.proxectofct.data.database.entities

import androidx.room.Embedded
import androidx.room.Relation
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoActivoTable
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoTable

data class MedicamentoAndMedicamentoActivo(
    @Embedded
    val medicamento: MedicamentoEntity,

    @Relation(
        parentColumn = MedicamentoTable.Columns.PK_COD_NACIONAL_MEDICAMENTO,
        entityColumn = MedicamentoActivoTable.Columns.FK_MEDICAMENTO
    )
    val medicamentoActivo: MedicamentoActivoEntity
)