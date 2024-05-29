package com.a23pablooc.proxectofct.data.database.entities

import androidx.room.Embedded
import androidx.room.Relation
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoCalendarioTable
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoTable

data class MedicamentoCalendarioAndMedicamento(
    @Embedded
    val medicamentoCalendarioEntity: MedicamentoCalendarioEntity,

    @Relation(
        parentColumn = MedicamentoCalendarioTable.Columns.PK_MEDICAMENTO_CALENDARIO,
        entityColumn = MedicamentoTable.Columns.PK_COD_NACIONAL
    )
    val medicamento: MedicamentoEntity
)