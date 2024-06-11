package com.a23pablooc.proxectofct.domain.model

import java.util.Date

data class MedicamentoActivoItem(
    var pkMedicamentoActivo: Long,
    var fkMedicamento: MedicamentoItem,
    val fkUsuario: Long,
    var fechaInicio: Date,
    var fechaFin: Date,
    var horario: MutableSet<Date>,
    var dosis: String,
    var tomas: MutableMap<Date, MutableMap<Date, Boolean>>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MedicamentoActivoItem

        if (pkMedicamentoActivo != other.pkMedicamentoActivo) return false
        if (fkMedicamento != other.fkMedicamento) return false
        if (fkUsuario != other.fkUsuario) return false
        if (fechaInicio != other.fechaInicio) return false
        if (fechaFin != other.fechaFin) return false
        if (horario != other.horario) return false
        if (dosis != other.dosis) return false
        if (tomas != other.tomas) return false

        return true
    }

    override fun hashCode(): Int {
        var result = pkMedicamentoActivo.hashCode()
        result = 31 * result + fkMedicamento.hashCode()
        result = 31 * result + fkUsuario.hashCode()
        result = 31 * result + fechaInicio.hashCode()
        result = 31 * result + fechaFin.hashCode()
        result = 31 * result + horario.hashCode()
        result = 31 * result + dosis.hashCode()
        result = 31 * result + tomas.hashCode()
        return result
    }
}