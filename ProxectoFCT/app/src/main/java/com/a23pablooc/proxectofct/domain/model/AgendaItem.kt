package com.a23pablooc.proxectofct.domain.model

import java.util.Date

data class AgendaItem(
    var pkFecha: Date,
    var fkUsuario: Long,
    var descripcion: String,
)