package com.a23pablooc.proxectofct.domain.model

import java.util.Date

data class AgendaItem(
    var pkAgenda: Int,
    var descripcion: String,
    var fecha: Date
)