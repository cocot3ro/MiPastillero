package com.cocot3ro.mipastillero.data.database.entities.extensions

import com.cocot3ro.mipastillero.data.database.entities.NotificacionEntity
import com.cocot3ro.mipastillero.domain.model.MedicamentoActivoItem
import com.cocot3ro.mipastillero.domain.model.NotificacionItem

fun NotificacionEntity.toDomain(med: MedicamentoActivoItem): NotificacionItem {
    return NotificacionItem(
        pkNotificacion = pkNotificacion,
        fkUsuario = fkUsuario,
        fkMedicamentoActivo = med,
        timeStamp = timeStamp
    )
}