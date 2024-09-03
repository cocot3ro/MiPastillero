package com.cocot3ro.mipastillero.domain.model.extensions

import com.cocot3ro.mipastillero.data.database.entities.UsuarioEntity
import com.cocot3ro.mipastillero.domain.model.UsuarioItem

fun UsuarioItem.toDatabase(): UsuarioEntity {
    return UsuarioEntity(
        pkUsuario = pkUsuario,
        nombre = nombre
    )
}