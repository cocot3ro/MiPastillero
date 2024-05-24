package com.a23pablooc.proxectofct.domain.model

data class HistorialItem(
    val id: Int,
    val medicamento: MedicamentoItem,
    val fechaInicio: String,
    val fechaFin: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HistorialItem

        if (id != other.id) return false
        if (medicamento != other.medicamento) return false
        if (fechaInicio != other.fechaInicio) return false
        if (fechaFin != other.fechaFin) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + medicamento.hashCode()
        result = 31 * result + fechaInicio.hashCode()
        result = 31 * result + fechaFin.hashCode()
        return result
    }
}