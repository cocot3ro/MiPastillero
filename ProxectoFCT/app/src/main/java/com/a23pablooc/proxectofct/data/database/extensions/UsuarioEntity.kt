package com.a23pablooc.proxectofct.data.database.extensions

import com.a23pablooc.proxectofct.data.database.entities.UsuarioEntity
import com.a23pablooc.proxectofct.domain.model.UsuarioItem

fun UsuarioEntity.toDomain(): UsuarioItem {
    return UsuarioItem(
        pkUsuario = pkUsuario,
        nombre = nombre
    )
}