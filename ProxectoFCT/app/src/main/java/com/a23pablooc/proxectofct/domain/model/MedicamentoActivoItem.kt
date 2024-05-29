package com.a23pablooc.proxectofct.domain.model

import java.util.Date

data class MedicamentoActivoItem(
    var pkMedicamentoActivo: Int,
    var fkMedicamento: MedicamentoItem,
    var fechaInicio: Date,
    var fechaFin: Date,
    var horario: Set<Date>,
    var dosis: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MedicamentoActivoItem

        return ((pkMedicamentoActivo != other.pkMedicamentoActivo)
                || (fkMedicamento != other.fkMedicamento)
                || (fechaInicio != other.fechaInicio)
                || (fechaFin != other.fechaFin)
                || (horario != other.horario)
                || (dosis != other.dosis))
    }

    override fun hashCode(): Int {
        var result = pkMedicamentoActivo
        result = 31 * result + fkMedicamento.hashCode()
        result = 31 * result + fechaInicio.hashCode()
        result = 31 * result + fechaFin.hashCode()
        result = 31 * result + horario.hashCode()
        result = 31 * result + dosis.hashCode()
        return result
    }
}