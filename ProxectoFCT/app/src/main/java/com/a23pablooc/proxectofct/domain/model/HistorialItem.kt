package com.a23pablooc.proxectofct.domain.model

data class HistorialItem(
    val id: Int,
    val fkUsuario: Int,
    val fkMedicamento: Int,
    val fechaInicio: String,
    val fechaFin: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HistorialItem

        if (id != other.id) return false
        if (fkUsuario != other.fkUsuario) return false
        if (fkMedicamento != other.fkMedicamento) return false
        if (fechaInicio != other.fechaInicio) return false
        if (fechaFin != other.fechaFin) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + fkUsuario
        result = 31 * result + fkMedicamento
        result = 31 * result + fechaInicio.hashCode()
        result = 31 * result + fechaFin.hashCode()
        return result
    }
}