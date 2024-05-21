package com.a23pablooc.proxectofct.domain.model

import lombok.EqualsAndHashCode
import java.util.Date

@EqualsAndHashCode
data class AgendaItem(
    val id: Int,
    val usuario: UsuarioItem,
    val fecha: Date,
    val descripcion: String
)