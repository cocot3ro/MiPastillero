package com.a23pablooc.proxectofct.domain.model

import java.util.Date

data class NotificacionItem(
    var pkNotificacion: Int,
    var fkMedicamento: MedicamentoItem,
    var fecha: Date,
    var hora: Date
)