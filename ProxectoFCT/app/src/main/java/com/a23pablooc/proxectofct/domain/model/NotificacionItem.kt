package com.a23pablooc.proxectofct.domain.model

import java.util.Date

data class NotificacionItem(
    var pkNotificacion: Int,
    val fkUsuario: Long,
    var fkMedicamentoActivo: MedicamentoActivoItem,
    var dia: Date,
    var hora: Date
)