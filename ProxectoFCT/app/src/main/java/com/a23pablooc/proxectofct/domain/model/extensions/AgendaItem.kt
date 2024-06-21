package com.a23pablooc.proxectofct.domain.model.extensions

import com.a23pablooc.proxectofct.data.database.entities.AgendaEntity
import com.a23pablooc.proxectofct.domain.model.AgendaItem

fun AgendaItem.toDatabase(): AgendaEntity {
    return AgendaEntity(
        pkFecha = pkFecha,
        fkUsuario = fkUsuario,
        descripcion = descripcion
    )
}