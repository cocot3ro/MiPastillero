package com.a23pablooc.proxectofct.data.database.entities.extensions

import com.a23pablooc.proxectofct.data.database.entities.NotificacionEntity
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem
import com.a23pablooc.proxectofct.domain.model.NotificacionItem

fun NotificacionEntity.toDomain(med: MedicamentoActivoItem): NotificacionItem {
    return NotificacionItem(
        pkNotificacion = pkNotificacion,
        fkUsuario = fkUsuario,
        fkMedicamentoActivo = med,
        fecha = fecha,
        hora = hora
    )
}