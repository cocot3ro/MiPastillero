package com.a23pablooc.proxectofct.data.database.entities

import androidx.room.Embedded
import androidx.room.Relation
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoActivoTableDefinition
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoTableDefinition

data class MedicamentoWithMedicamentoActivo(
    @Embedded
    val medicamento: MedicamentoEntity,

    @Relation(
        parentColumn = MedicamentoTableDefinition.Columns.PK_COD_NACIONAL_MEDICAMENTO,
        entityColumn = MedicamentoActivoTableDefinition.Columns.FK_MEDICAMENTO
    )
    val medicamentosActivos: List<MedicamentoActivoEntity>
)