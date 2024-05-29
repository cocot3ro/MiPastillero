package com.a23pablooc.proxectofct.domain.model

import java.util.Date

data class AgendaItem(
    var pkAgenda: Int,
    var descripcion: String,
    var fecha: Date
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AgendaItem

        return ((pkAgenda != other.pkAgenda)
                || (descripcion != other.descripcion)
                || (fecha != other.fecha))
    }

    override fun hashCode(): Int {
        var result = pkAgenda
        result = 31 * result + descripcion.hashCode()
        result = 31 * result + fecha.hashCode()
        return result
    }
}