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
        entityColumn = MedicamentoTable.Columns.PK_COD_NACIONAL
    )
    val medicamento: MedicamentoEntity
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MedicamentoActivoAndMedicamento

        return ((medicamentoActivoEntity != other.medicamentoActivoEntity)
                || (medicamento != other.medicamento))
    }

    override fun hashCode(): Int {
        var result = medicamentoActivoEntity.hashCode()
        result = 31 * result + medicamento.hashCode()
        return result
    }
}