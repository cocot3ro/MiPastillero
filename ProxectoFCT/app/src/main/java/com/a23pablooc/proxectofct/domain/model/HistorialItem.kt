package com.a23pablooc.proxectofct.domain.model

data class HistorialItem(
    val pkHistorial: Long = 0,
    val fkMedicamento: MedicamentoItem,
    val fkMedicamentoActivo: MedicamentoActivoItem
)