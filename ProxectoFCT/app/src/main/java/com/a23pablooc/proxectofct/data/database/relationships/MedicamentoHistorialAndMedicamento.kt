package com.a23pablooc.proxectofct.data.database.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoHistorialTableDefinition
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoActivoTableDefinition
import com.a23pablooc.proxectofct.data.database.entities.MedicamentoActivoEntity
import com.a23pablooc.proxectofct.data.database.entities.MedicamentoHistorialEntity

data class MedicamentoHistorialAndMedicamento(
    @Embedded val medicamentoHistorialEntity: MedicamentoHistorialEntity,

    @Relation(
        parentColumn = MedicamentoHistorialTableDefinition.Columns.PK_MEDICAMENTO_HISTORIAL,
        entityColumn = MedicamentoActivoTableDefinition.Columns.PK_MEDICAMENTO_ACTIVO,
    )
    val medicamentoActivo: MedicamentoActivoEntity
)