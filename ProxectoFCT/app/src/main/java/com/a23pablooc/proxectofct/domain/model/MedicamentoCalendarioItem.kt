package com.a23pablooc.proxectofct.domain.model

import java.util.Date

data class MedicamentoCalendarioItem(
    var pkMedicamentoCalendario: Int,
    var fkMedicamento: MedicamentoItem,
    var fecha: Date,
    var hora: Date,
    var seHaTomado: Boolean
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MedicamentoCalendarioItem

        return ((pkMedicamentoCalendario != other.pkMedicamentoCalendario)
                || (fkMedicamento != other.fkMedicamento)
                || (fecha != other.fecha)
                || (hora != other.hora)
                || (seHaTomado != other.seHaTomado))
    }

    override fun hashCode(): Int {
        var result = pkMedicamentoCalendario
        result = 31 * result + fkMedicamento.hashCode()
        result = 31 * result + fecha.hashCode()
        result = 31 * result + hora.hashCode()
        result = 31 * result + seHaTomado.hashCode()
        return result
    }
}