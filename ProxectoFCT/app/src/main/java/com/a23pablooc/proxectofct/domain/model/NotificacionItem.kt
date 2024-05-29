package com.a23pablooc.proxectofct.domain.model

import java.util.Date

data class NotificacionItem(
    var pkNotificacion: Int,
    var fkMedicamento: MedicamentoItem,
    var fecha: Date,
    var hora: Date
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NotificacionItem

        return ((pkNotificacion != other.pkNotificacion)
                || (fkMedicamento != other.fkMedicamento)
                || (fecha != other.fecha)
                || (hora != other.hora))
    }

    override fun hashCode(): Int {
        var result = pkNotificacion
        result = 31 * result + fkMedicamento.hashCode()
        result = 31 * result + fecha.hashCode()
        result = 31 * result + hora.hashCode()
        return result
    }
}