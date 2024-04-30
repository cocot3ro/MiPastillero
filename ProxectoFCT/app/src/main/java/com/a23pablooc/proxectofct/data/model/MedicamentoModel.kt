package com.a23pablooc.proxectofct.data.model

import com.a23pablooc.proxectofct.data.database.entities.MedicamentoEntity
import com.a23pablooc.proxectofct.domain.model.MedicamentoItem

data class MedicamentoModel(
    val numRegistro: String,
    val nombre: String,
    val url: String,
    val prospecto: String,
    val imagen: ByteArray,
    val laboratorio: String,
    val dosis: String,
    val prescripcion: String,
    val conduccion: Boolean,
    val favorito: Boolean
) {
    class Builder {
        private var numRegistro: String = ""
        private var nombre: String = ""
        private var url: String = ""
        private var prospecto: String = ""
        private var imagen: ByteArray = byteArrayOf()
        private var laboratorio: String = ""
        private var dosis: String = ""
        private var prescripcion: String = ""
        private var conduccion: Boolean = false
        private var favorito: Boolean = false

        fun numRegistro(numRegistro: String) = apply { this.numRegistro = numRegistro }
        fun nombre(nombre: String) = apply { this.nombre = nombre }
        fun url(url: String) = apply { this.url = url }
        fun prospecto(prospecto: String) = apply { this.prospecto = prospecto }
        fun imagen(imagen: ByteArray) = apply { this.imagen = imagen }
        fun laboratorio(laboratorio: String) = apply { this.laboratorio = laboratorio }
        fun dosis(dosis: String) = apply { this.dosis = dosis }
        fun prescripcion(prescripcion: String) = apply { this.prescripcion = prescripcion }
        fun conduccion(conduccion: Boolean) = apply { this.conduccion = conduccion }
        fun favorito(favorito: Boolean) = apply { this.favorito = favorito }

        fun build() = MedicamentoModel(
            numRegistro,
            nombre,
            url,
            prospecto,
            imagen,
            laboratorio,
            dosis,
            prescripcion,
            conduccion,
            favorito
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MedicamentoModel

        if (numRegistro != other.numRegistro) return false
        if (nombre != other.nombre) return false
        if (url != other.url) return false
        if (prospecto != other.prospecto) return false
        if (!imagen.contentEquals(other.imagen)) return false
        if (laboratorio != other.laboratorio) return false
        if (dosis != other.dosis) return false
        if (prescripcion != other.prescripcion) return false
        if (conduccion != other.conduccion) return false
        if (favorito != other.favorito) return false

        return true
    }

    override fun hashCode(): Int {
        var result = numRegistro.hashCode()
        result = 31 * result + nombre.hashCode()
        result = 31 * result + url.hashCode()
        result = 31 * result + prospecto.hashCode()
        result = 31 * result + imagen.contentHashCode()
        result = 31 * result + laboratorio.hashCode()
        result = 31 * result + dosis.hashCode()
        result = 31 * result + prescripcion.hashCode()
        result = 31 * result + conduccion.hashCode()
        result = 31 * result + favorito.hashCode()
        return result
    }

}

fun MedicamentoEntity.toModel() = MedicamentoModel(
    numRegistro = numRegistro,
    nombre = nombre,
    url = url,
    prospecto = prospecto,
    imagen = imagen,
    laboratorio = laboratorio,
    dosis = dosis,
    prescripcion = prescripcion,
    conduccion = conduccion,
    favorito = favorito
)

fun MedicamentoItem.toModel() = MedicamentoModel(
    numRegistro = numRegistro,
    nombre = nombre,
    url = url,
    prospecto = prospecto,
    imagen = imagen,
    laboratorio = laboratorio,
    dosis = dosis,
    prescripcion = prescripcion,
    conduccion = conduccion,
    favorito = favorito
)