package com.a23pablooc.proxectofct.domain.model

import lombok.EqualsAndHashCode
import java.util.Date

@EqualsAndHashCode
data class NotificacionItem(
    val id: Int,
    val medicamento: MedicamentoItem,
    val usuario: UsuarioItem,
    val fecha: Date,
    val hora: Date
)