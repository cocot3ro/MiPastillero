package com.a23pablooc.proxectofct.domain.model

import lombok.EqualsAndHashCode

@EqualsAndHashCode
data class UsuarioItem(
    val pkUsuario: Int,
    val nombre: String
)