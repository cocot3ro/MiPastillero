package com.a23pablooc.proxectofct.domain.model

data class HistorialItem(
    val pkHistorial: Int,
    val fkMedicamento: MedicamentoItem,
    val fechaInicio: String,
    val fechaFin: String
)