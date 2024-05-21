package com.a23pablooc.proxectofct.domain.model

import lombok.EqualsAndHashCode

@EqualsAndHashCode
data class HistorialItem(
    val id: Int,
    val usuario: UsuarioItem,
    val medicamento: MedicamentoItem,
    val fechaInicio: String,
    val fechaFin: String
)