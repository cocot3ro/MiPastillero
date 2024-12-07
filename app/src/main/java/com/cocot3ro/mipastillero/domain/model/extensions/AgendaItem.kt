package com.cocot3ro.mipastillero.domain.model.extensions

import com.cocot3ro.mipastillero.data.database.entities.AgendaEntity
import com.cocot3ro.mipastillero.domain.model.AgendaItem

fun AgendaItem.toDatabase(): AgendaEntity {
    return AgendaEntity(
        pkFecha = pkFecha,
        fkUsuario = fkUsuario,
        descripcion = descripcion
    )
}