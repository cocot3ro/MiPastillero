package com.a23pablooc.proxectofct.domain.model

import lombok.EqualsAndHashCode
import java.util.Date

@EqualsAndHashCode
data class MedicamentoCalendarioItem(
    val id: Int,
    val medicamento: MedicamentoItem,
    val idUsuario: Int,
    val fecha: Date,
    val hora: Date,
    val seHaTomado: Boolean
)