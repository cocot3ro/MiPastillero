package com.a23pablooc.proxectofct.domain.model

data class MedicamentoFavoritoItem(
    val id: Int,
    val idMedicamento: Int,
    val idUsuario: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MedicamentoFavoritoItem

        if (id != other.id) return false
        if (idMedicamento != other.idMedicamento) return false
        if (idUsuario != other.idUsuario) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + idMedicamento
        result = 31 * result + idUsuario
        return result
    }
}