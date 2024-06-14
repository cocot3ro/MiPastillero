package com.a23pablooc.proxectofct.domain.model

import java.util.Date

data class NotificacionItem(
    var pkNotificacion: Long,
    val fkUsuario: Long,
    var fkMedicamentoActivo: MedicamentoActivoItem,
    var fecha: Date,
    var hora: Date,
    var notificado: Boolean
)