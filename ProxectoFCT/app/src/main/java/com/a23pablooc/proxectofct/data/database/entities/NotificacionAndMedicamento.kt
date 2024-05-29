package com.a23pablooc.proxectofct.data.database.entities

import androidx.room.Embedded
import androidx.room.Relation
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoTable
import com.a23pablooc.proxectofct.data.database.definitions.NotificacionTable

data class NotificacionAndMedicamento(
    @Embedded
    val notificacionEntity: NotificacionEntity,

    @Relation(
        parentColumn = NotificacionTable.Columns.PK_NOTIFICACION,
        entityColumn = MedicamentoTable.Columns.PK_COD_NACIONAL
    )
    val medicamento: MedicamentoEntity
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NotificacionAndMedicamento

        return ((notificacionEntity != other.notificacionEntity)
                || (medicamento != other.medicamento))
    }

    override fun hashCode(): Int {
        var result = notificacionEntity.hashCode()
        result = 31 * result + medicamento.hashCode()
        return result
    }
}