package com.cocot3ro.mipastillero.domain.model

import java.util.Date

data class NotificacionItem(
    var pkNotificacion: Int,
    val fkUsuario: Long,
    var fkMedicamentoActivo: MedicamentoActivoItem,
    var timeStamp: Date
)