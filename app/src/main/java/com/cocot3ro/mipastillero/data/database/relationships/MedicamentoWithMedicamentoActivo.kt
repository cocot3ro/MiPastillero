package com.cocot3ro.mipastillero.data.database.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.cocot3ro.mipastillero.data.database.definitions.MedicamentoActivoTableDefinition
import com.cocot3ro.mipastillero.data.database.definitions.MedicamentoTableDefinition
import com.cocot3ro.mipastillero.data.database.entities.MedicamentoActivoEntity
import com.cocot3ro.mipastillero.data.database.entities.MedicamentoEntity

data class MedicamentoWithMedicamentoActivo(
    @Embedded
    val medicamento: MedicamentoEntity,

    @Relation(
        parentColumn = MedicamentoTableDefinition.Columns.PK_COD_NACIONAL_MEDICAMENTO,
        entityColumn = MedicamentoActivoTableDefinition.Columns.FK_MEDICAMENTO
    )
    val medicamentosActivos: List<MedicamentoActivoEntity>
)