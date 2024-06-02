package com.a23pablooc.proxectofct.data.database.entities

import androidx.room.Embedded
import androidx.room.Relation
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoActivoTableDefinition
import com.a23pablooc.proxectofct.data.database.definitions.NotificacionTableDefinition

data class MedicamentoActivoWithNotificacion(
    @Embedded
    val medicamentoActivo: MedicamentoActivoEntity,

    @Relation(
        parentColumn = MedicamentoActivoTableDefinition.Columns.PK_MEDICAMENTO_ACTIVO,
        entityColumn = NotificacionTableDefinition.Columns.FK_MEDICAMENTO_ACTIVO
    )
    val notificaciones: List<NotificacionEntity>
)