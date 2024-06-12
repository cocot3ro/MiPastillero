package com.a23pablooc.proxectofct.domain.model

import java.util.Date

data class MedicamentoActivoItem(
    var pkMedicamentoActivo: Long,
    var fkMedicamento: MedicamentoItem,
    val fkUsuario: Long,
    var fechaInicio: Date,
    var fechaFin: Date,
    var horario: MutableSet<Date>,
    var dosis: String,
    var tomas: MutableMap<Date, MutableMap<Date, Boolean>>
)