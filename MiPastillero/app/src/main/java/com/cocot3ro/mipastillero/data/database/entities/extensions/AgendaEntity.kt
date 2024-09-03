package com.cocot3ro.mipastillero.data.database.entities.extensions

import com.cocot3ro.mipastillero.data.database.entities.AgendaEntity
import com.cocot3ro.mipastillero.domain.model.AgendaItem

fun AgendaEntity.toDomain(): AgendaItem {
    return AgendaItem(
        pkFecha = pkFecha,
        fkUsuario = fkUsuario,
        descripcion = descripcion
    )
}