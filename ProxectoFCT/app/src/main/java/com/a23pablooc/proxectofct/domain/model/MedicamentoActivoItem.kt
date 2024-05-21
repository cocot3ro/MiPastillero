package com.a23pablooc.proxectofct.domain.model

import lombok.EqualsAndHashCode
import java.util.Date

@EqualsAndHashCode
data class MedicamentoActivoItem(
    val id: Int,
    val idMedicamento: Int,
    val idUsuario: Int,
    val fechaInicio: Date,
    val fechaFin: Date,
    val horario: Set<Date>,
    val dosis: String
)