package com.cocot3ro.mipastillero.domain.model

import java.util.Date

data class AgendaItem(
    var pkFecha: Date,
    var fkUsuario: Long,
    var descripcion: String,
)