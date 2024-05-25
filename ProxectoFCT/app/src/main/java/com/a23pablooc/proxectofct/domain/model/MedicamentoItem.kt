package com.a23pablooc.proxectofct.domain.model

data class MedicamentoItem(
    val id: Int,
    var numRegistro: String,
    var nombre: String,
    var url: String,
    var prospecto: String,
    var imagen: ByteArray,
    var laboratorio: String,
    var prescripcion: String,
    var afectaConduccion: Boolean,
    var esFavorito: Boolean
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MedicamentoItem

        if (id != other.id) return false
        if (numRegistro != other.numRegistro) return false
        if (nombre != other.nombre) return false
        if (url != other.url) return false
        if (prospecto != other.prospecto) return false
        if (!imagen.contentEquals(other.imagen)) return false
        if (laboratorio != other.laboratorio) return false
        if (prescripcion != other.prescripcion) return false
        if (afectaConduccion != other.afectaConduccion) return false
        if (esFavorito != other.esFavorito) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + numRegistro.hashCode()
        result = 31 * result + nombre.hashCode()
        result = 31 * result + url.hashCode()
        result = 31 * result + prospecto.hashCode()
        result = 31 * result + imagen.contentHashCode()
        result = 31 * result + laboratorio.hashCode()
        result = 31 * result + prescripcion.hashCode()
        result = 31 * result + afectaConduccion.hashCode()
        result = 31 * result + esFavorito.hashCode()
        return result
    }
}