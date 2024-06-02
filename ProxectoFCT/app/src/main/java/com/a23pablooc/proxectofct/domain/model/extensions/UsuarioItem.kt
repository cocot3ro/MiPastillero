package com.a23pablooc.proxectofct.domain.model.extensions

import com.a23pablooc.proxectofct.data.database.entities.UsuarioEntity
import com.a23pablooc.proxectofct.domain.model.UsuarioItem

fun UsuarioItem.toDatabase(): UsuarioEntity {
    return UsuarioEntity(
        pkUsuario = pkUsuario,
        nombre = nombre
    )
}