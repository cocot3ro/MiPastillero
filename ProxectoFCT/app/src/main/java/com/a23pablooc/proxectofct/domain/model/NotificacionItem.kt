package com.a23pablooc.proxectofct.domain.model

import java.util.Date

data class NotificacionItem(
    val id: Int,
    val idMedicamento: Int,
    val idUsuario: Int,
    val fecha: Date,
    val hora: Date
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NotificacionItem

        if (id != other.id) return false
        if (idMedicamento != other.idMedicamento) return false
        if (idUsuario != other.idUsuario) return false
        if (fecha != other.fecha) return false
        if (hora != other.hora) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + idMedicamento
        result = 31 * result + idUsuario
        result = 31 * result + fecha.hashCode()
        result = 31 * result + hora.hashCode()
        return result
    }
}