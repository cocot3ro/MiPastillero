package com.a23pablooc.proxectofct.domain.model

data class HistorialItem(
    val pkHistorial: Long,
    val fkMedicamentoActivo: MedicamentoActivoItem
)