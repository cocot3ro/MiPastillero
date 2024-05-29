package com.a23pablooc.proxectofct.domain.model

data class MedicamentoItem(
    var pkMedicamento: Int,
    var url: String,
    var nombre: String,
    var alias: String,
    var prospecto: String,
    var numRegistro: String,
    var laboratorio: String,
    var esFavorito: Boolean,
    var apiImagen: ByteArray,
    var prescripcion: String,
    var customImage: ByteArray,
    var afectaConduccion: Boolean
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MedicamentoItem

        return ((pkMedicamento != other.pkMedicamento)
                || (url != other.url)
                || (nombre != other.nombre)
                || (prospecto != other.prospecto)
                || (numRegistro != other.numRegistro)
                || (laboratorio != other.laboratorio)
                || (esFavorito != other.esFavorito)
                || (!apiImagen.contentEquals(other.apiImagen))
                || (prescripcion != other.prescripcion)
                || (!customImage.contentEquals(other.customImage))
                || (afectaConduccion != other.afectaConduccion))
    }

    override fun hashCode(): Int {
        var result = pkMedicamento
        result = 31 * result + url.hashCode()
        result = 31 * result + nombre.hashCode()
        result = 31 * result + prospecto.hashCode()
        result = 31 * result + numRegistro.hashCode()
        result = 31 * result + laboratorio.hashCode()
        result = 31 * result + esFavorito.hashCode()
        result = 31 * result + apiImagen.contentHashCode()
        result = 31 * result + prescripcion.hashCode()
        result = 31 * result + customImage.contentHashCode()
        result = 31 * result + afectaConduccion.hashCode()
        return result
    }
}