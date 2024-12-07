package com.cocot3ro.mipastillero.data.database.entities.extensions

import com.cocot3ro.mipastillero.data.database.entities.UsuarioEntity
import com.cocot3ro.mipastillero.domain.model.UsuarioItem

fun UsuarioEntity.toDomain(): UsuarioItem {
    return UsuarioItem(
        pkUsuario = pkUsuario,
        nombre = nombre,
    )
}