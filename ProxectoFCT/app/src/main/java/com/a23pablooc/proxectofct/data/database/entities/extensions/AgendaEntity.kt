package com.a23pablooc.proxectofct.data.database.entities.extensions

import com.a23pablooc.proxectofct.data.database.entities.AgendaEntity
import com.a23pablooc.proxectofct.domain.model.AgendaItem

fun AgendaEntity.toDomain(): AgendaItem {
    return AgendaItem(
        pkFecha = pkFecha,
        fkUsuario = fkUsuario,
        descripcion = descripcion
    )
}