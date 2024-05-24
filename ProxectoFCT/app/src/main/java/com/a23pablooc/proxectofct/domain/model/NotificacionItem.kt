package com.a23pablooc.proxectofct.domain.model

import java.util.Date

data class NotificacionItem(
    val id: Int,
    val medicamento: MedicamentoItem,
    val fecha: Date,
    val hora: Date
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NotificacionItem

        if (id != other.id) return false
        if (medicamento != other.medicamento) return false
        if (fecha != other.fecha) return false
        if (hora != other.hora) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + medicamento.hashCode()
        result = 31 * result + fecha.hashCode()
        result = 31 * result + hora.hashCode()
        return result
    }
}