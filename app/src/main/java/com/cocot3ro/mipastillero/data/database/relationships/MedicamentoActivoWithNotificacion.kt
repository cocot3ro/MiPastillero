package com.cocot3ro.mipastillero.data.database.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.cocot3ro.mipastillero.data.database.definitions.MedicamentoActivoTableDefinition
import com.cocot3ro.mipastillero.data.database.definitions.NotificacionTableDefinition
import com.cocot3ro.mipastillero.data.database.entities.MedicamentoActivoEntity
import com.cocot3ro.mipastillero.data.database.entities.NotificacionEntity

data class MedicamentoActivoWithNotificacion(
    @Embedded
    val medicamentoActivo: MedicamentoActivoEntity,

    @Relation(
        parentColumn = MedicamentoActivoTableDefinition.Columns.PK_MEDICAMENTO_ACTIVO,
        entityColumn = NotificacionTableDefinition.Columns.FK_MEDICAMENTO_ACTIVO
    )
    val notificaciones: List<NotificacionEntity>
)