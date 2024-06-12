package com.a23pablooc.proxectofct.domain.model

data class MedicamentoHistorialItem(
    val pkMedicamentoHistorial: Long = 0,
    val fkUsuario: Long,
    val fkMedicamento: MedicamentoItem,
    val fkMedicamentoActivo: MedicamentoActivoItem
)