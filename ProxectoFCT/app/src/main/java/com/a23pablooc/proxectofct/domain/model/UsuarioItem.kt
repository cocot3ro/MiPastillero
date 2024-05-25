package com.a23pablooc.proxectofct.domain.model

data class UsuarioItem(
    val id: Int,
    val nombre: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UsuarioItem

        if (id != other.id) return false
        if (nombre != other.nombre) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + nombre.hashCode()
        return result
    }
}