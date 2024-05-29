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
)