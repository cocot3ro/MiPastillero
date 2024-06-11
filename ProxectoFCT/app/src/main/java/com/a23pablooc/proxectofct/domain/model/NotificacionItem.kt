package com.a23pablooc.proxectofct.domain.model

import java.util.Date

data class NotificacionItem(
    var pkNotificacion: Long,
    var fkMedicamento: MedicamentoItem,
    val fkUsuario: Long,
    var fecha: Date,
    var hora: Date
)