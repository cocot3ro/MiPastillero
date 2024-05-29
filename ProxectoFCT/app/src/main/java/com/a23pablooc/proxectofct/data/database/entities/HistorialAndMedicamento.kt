package com.a23pablooc.proxectofct.data.database.entities

import androidx.room.Embedded
import androidx.room.Relation
import com.a23pablooc.proxectofct.data.database.definitions.HistorialTable
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoTable

data class HistorialAndMedicamento(
    @Embedded
    val historialEntity: HistorialEntity,

    @Relation(
        parentColumn = HistorialTable.Columns.PK_HISTORIAL,
        entityColumn = MedicamentoTable.Columns.PK_COD_NACIONAL
    )
    val medicamento: MedicamentoEntity
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HistorialAndMedicamento

        return ((historialEntity != other.historialEntity)
                || (medicamento != other.medicamento))
    }

    override fun hashCode(): Int {
        var result = historialEntity.hashCode()
        result = 31 * result + medicamento.hashCode()
        return result
    }
}