package com.a23pablooc.proxectofct.domain.model

import java.util.Date

data class MedicamentoCalendarioItem(
    var pkMedicamentoCalendario: Int,
    var fkMedicamento: MedicamentoItem,
    var fecha: Date,
    var hora: Date,
    var seHaTomado: Boolean
)