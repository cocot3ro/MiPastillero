package com.a23pablooc.proxectofct.data.database.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoActivoTableDefinition
import com.a23pablooc.proxectofct.data.database.definitions.NotificacionTableDefinition
import com.a23pablooc.proxectofct.data.database.entities.MedicamentoActivoEntity
import com.a23pablooc.proxectofct.data.database.entities.NotificacionEntity

data class MedicamentoActivoWithNotificacion(
    @Embedded
    val medicamentoActivo: MedicamentoActivoEntity,

    @Relation(
        parentColumn = MedicamentoActivoTableDefinition.Columns.PK_MEDICAMENTO_ACTIVO,
        entityColumn = NotificacionTableDefinition.Columns.FK_MEDICAMENTO_ACTIVO
    )
    val notificaciones: List<NotificacionEntity>
)