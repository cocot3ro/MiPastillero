package com.a23pablooc.proxectofct.domain.model

data class MedicamentoFavoritoItem(
    val id: Int,
    val medicamento: MedicamentoItem,
    val usuario: UsuarioItem
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MedicamentoFavoritoItem

        if (id != other.id) return false
        if (medicamento != other.medicamento) return false
        if (usuario != other.usuario) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + medicamento.hashCode()
        result = 31 * result + usuario.hashCode()
        return result
    }
}