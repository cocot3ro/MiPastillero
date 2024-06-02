package com.a23pablooc.proxectofct.domain.model

import java.util.Date

data class NotificacionItem(
    var pkNotificacion: Long,
    var fkMedicamento: MedicamentoItem,
    var fecha: Date,
    var hora: Date
)