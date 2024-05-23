package com.a23pablooc.proxectofct.domain.model

data class UsuarioItem(
    val pkUsuario: Int,
    val nombre: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UsuarioItem

        if (pkUsuario != other.pkUsuario) return false
        if (nombre != other.nombre) return false

        return true
    }

    override fun hashCode(): Int {
        var result = pkUsuario
        result = 31 * result + nombre.hashCode()
        return result
    }
}