package com.a23pablooc.proxectofct.domain.model

data class HistorialItem(
    val pkHistorial: Int,
    val fkMedicamento: MedicamentoItem,
    val fechaInicio: String,
    val fechaFin: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HistorialItem

        return ((pkHistorial != other.pkHistorial)
                || (fkMedicamento != other.fkMedicamento)
                || (fechaInicio != other.fechaInicio)
                || (fechaFin != other.fechaFin))
    }

    override fun hashCode(): Int {
        var result = pkHistorial
        result = 31 * result + fkMedicamento.hashCode()
        result = 31 * result + fechaInicio.hashCode()
        result = 31 * result + fechaFin.hashCode()
        return result
    }
}