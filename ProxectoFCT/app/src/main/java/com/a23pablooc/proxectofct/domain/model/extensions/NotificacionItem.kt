package com.a23pablooc.proxectofct.domain.model.extensions

import com.a23pablooc.proxectofct.data.database.entities.NotificacionEntity
import com.a23pablooc.proxectofct.domain.model.NotificacionItem

fun NotificacionItem.toDatabase(): NotificacionEntity {
    return NotificacionEntity(
        pkNotificacion = pkNotificacion,
        fkMedicamentoActivo = fkMedicamentoActivo.pkMedicamentoActivo,
        fkUsuario = fkUsuario,
        timeStamp = timeStamp
    )
}