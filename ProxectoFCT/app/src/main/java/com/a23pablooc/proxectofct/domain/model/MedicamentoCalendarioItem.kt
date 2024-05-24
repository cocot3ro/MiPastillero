package com.a23pablooc.proxectofct.domain.model

import java.util.Date

data class MedicamentoCalendarioItem(
    var id: Int,
    var medicamento: MedicamentoItem,
    var fecha: Date,
    var hora: Date,
    var seHaTomado: Boolean
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MedicamentoCalendarioItem

        if (id != other.id) return false
        if (medicamento != other.medicamento) return false
        if (fecha != other.fecha) return false
        if (hora != other.hora) return false
        if (seHaTomado != other.seHaTomado) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + medicamento.hashCode()
        result = 31 * result + fecha.hashCode()
        result = 31 * result + hora.hashCode()
        result = 31 * result + seHaTomado.hashCode()
        return result
    }
}