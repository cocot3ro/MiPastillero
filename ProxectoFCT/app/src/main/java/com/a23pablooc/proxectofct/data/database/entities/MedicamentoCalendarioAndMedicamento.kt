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
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MedicamentoCalendarioAndMedicamento

        return ((medicamentoCalendarioEntity != other.medicamentoCalendarioEntity)
                || (medicamento != other.medicamento))
    }

    override fun hashCode(): Int {
        var result = medicamentoCalendarioEntity.hashCode()
        result = 31 * result + medicamento.hashCode()
        return result
    }
}