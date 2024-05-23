package com.a23pablooc.proxectofct.domain.model

import java.util.Date

data class MedicamentoActivoItem(
    val id: Int,
    val idMedicamento: Int,
    val idUsuario: Int,
    val fechaInicio: Date,
    val fechaFin: Date,
    val horario: Set<Date>,
    val dosis: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MedicamentoActivoItem

        if (id != other.id) return false
        if (idMedicamento != other.idMedicamento) return false
        if (idUsuario != other.idUsuario) return false
        if (fechaInicio != other.fechaInicio) return false
        if (fechaFin != other.fechaFin) return false
        if (horario != other.horario) return false
        if (dosis != other.dosis) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + idMedicamento
        result = 31 * result + idUsuario
        result = 31 * result + fechaInicio.hashCode()
        result = 31 * result + fechaFin.hashCode()
        result = 31 * result + horario.hashCode()
        result = 31 * result + dosis.hashCode()
        return result
    }
}