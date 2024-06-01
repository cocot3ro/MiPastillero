package com.a23pablooc.proxectofct.domain.model

import java.util.Date

data class MedicamentoActivoItem(
    var pkMedicamentoActivo: Int,
    var fkMedicamento: MedicamentoItem,
    var fechaInicio: Date,
    var fechaFin: Date,
    var horario: MutableSet<Date>,
    var dosis: String,
    var tomas: MutableMap<Date, MutableMap<Date, Boolean>>
)