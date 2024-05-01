package com.a23pablooc.proxectofct.domain.model

data class MedicamentoActivoItem(
    val fechaInicio: Long,
    val fechaFin: Long,
    val horario: Set<Long>,
)