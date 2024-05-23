package com.a23pablooc.proxectofct.domain.model

import java.util.Date

data class AgendaItem(
    val id: Int,
    val usuario: UsuarioItem,
    val fecha: Date,
    val descripcion: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AgendaItem

        if (id != other.id) return false
        if (usuario != other.usuario) return false
        if (fecha != other.fecha) return false
        if (descripcion != other.descripcion) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + usuario.hashCode()
        result = 31 * result + fecha.hashCode()
        result = 31 * result + descripcion.hashCode()
        return result
    }
}