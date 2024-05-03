package com.a23pablooc.proxectofct.domain.model

import com.a23pablooc.proxectofct.data.database.entities.MedicamentoEntity
import com.a23pablooc.proxectofct.data.model.MedicamentoModel

data class MedicamentoItem(
    val numRegistro: String,
    val nombre: String,
    val url: String,
    val prospecto: String,
    val imagen: ByteArray,
    val laboratorio: String,
    val prescripcion: String,
    val conduccion: Boolean
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MedicamentoItem

        if (numRegistro != other.numRegistro) return false
        if (nombre != other.nombre) return false
        if (url != other.url) return false
        if (prospecto != other.prospecto) return false
        if (!imagen.contentEquals(other.imagen)) return false
        if (laboratorio != other.laboratorio) return false
        if (prescripcion != other.prescripcion) return false
        if (conduccion != other.conduccion) return false

        return true
    }

    override fun hashCode(): Int {
        var result = numRegistro.hashCode()
        result = 31 * result + nombre.hashCode()
        result = 31 * result + url.hashCode()
        result = 31 * result + prospecto.hashCode()
        result = 31 * result + imagen.contentHashCode()
        result = 31 * result + laboratorio.hashCode()
        result = 31 * result + prescripcion.hashCode()
        result = 31 * result + conduccion.hashCode()
        return result
    }
}

fun MedicamentoModel.toDomain() = MedicamentoItem(
    numRegistro = numRegistro,
    nombre = nombre,
    url = url,
    prospecto = prospecto,
    imagen = imagen,
    laboratorio = laboratorio,
    prescripcion = prescripcion,
    conduccion = conduccion
)

fun MedicamentoEntity.toDomain() = MedicamentoItem(
    numRegistro = numRegistro,
    nombre = nombre,
    url = url,
    prospecto = prospecto,
    imagen = imagen,
    laboratorio = laboratorio,
    prescripcion = prescripcion,
    conduccion = afectaConduccion
)
