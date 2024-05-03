package com.a23pablooc.proxectofct.domain.model

import com.a23pablooc.proxectofct.data.database.entities.UsuarioEntity

data class UsuarioItem(
    private val nombre: String
)

fun UsuarioEntity.toDomain() = UsuarioItem(nombre = nombre)