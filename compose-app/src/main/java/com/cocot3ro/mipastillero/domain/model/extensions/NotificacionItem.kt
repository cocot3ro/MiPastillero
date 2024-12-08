package com.cocot3ro.mipastillero.domain.model.extensions

import com.cocot3ro.mipastillero.data.database.entities.NotificacionEntity
import com.cocot3ro.mipastillero.domain.model.NotificacionItem

fun NotificacionItem.toDatabase(): NotificacionEntity {
    return NotificacionEntity(
        pkNotificacion = pkNotificacion,
        fkMedicamentoActivo = fkMedicamentoActivo.pkMedicamentoActivo,
        fkUsuario = fkUsuario,
        timeStamp = timeStamp
    )
}