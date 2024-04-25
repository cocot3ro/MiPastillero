package com.a23pablooc.proxectofct.model

import java.io.Serializable

/**
 * Clase que representa un medicamento
 * @property nombre nombre del medicamento
 * @property codNacional código nacional del medicamento
 * @property fichaTecnica url de la ficha técnica del medicamento
 * @property prospecto url del prospecto del medicamento
 * @property fechaInicio fecha de inicio del tratamiento
 * @property fechaFin fecha de fin del tratamiento
 * @property horario horario del tratamiento
 * @property isFavorite indica si el medicamento es favorito
 */

data class Medicamento(
    val nombre: String?,
    val codNacional: Int?,
    val numRegistro: String?,
    val url: String?,
    val fichaTecnica: String?,
    val prospecto: String?,
    val imagen: ByteArray?,
    val laboratorio: String?,
    val dosis: String?,
    val principiosActivos: List<String>?,
    val receta: String?,
    val fechaInicio: Long?,
    val fechaFin: Long?,
    val horario: Set<Long>?,
    var isFavorite: Boolean?,
    var seHaTomado: Boolean?
) : Serializable {

    companion object
    class Builder {
        private var nombre: String? = null
        private var codNacional: Int? = null
        private var numRegistro: String? = null
        private var url: String? = null
        private var fichaTecnica: String? = null
        private var prospecto: String? = null
        private var imagen: ByteArray? = null
        private var laboratorio: String? = null
        private var dosis: String? = null
        private var principiosActivos: List<String>? = null
        private var receta: String? = null
        private var fechaInicio: Long? = null
        private var fechaFin: Long? = null
        private var horario: Set<Long>? = null
        private var isFavorite: Boolean? = null
        private var seHaTomado: Boolean? = null

        fun build(): Medicamento {
            return Medicamento(
                nombre,
                codNacional,
                numRegistro,
                url,
                fichaTecnica,
                prospecto,
                imagen,
                laboratorio,
                dosis,
                principiosActivos,
                receta,
                fechaInicio,
                fechaFin,
                horario,
                isFavorite,
                seHaTomado
            )
        }

        fun setNombre(nombre: String) = apply { this.nombre = nombre }

        fun setCodNacional(codNacional: Int) = apply { this.codNacional = codNacional }

        fun setNumRegistro(numRegistro: String) = apply { this.numRegistro = numRegistro }

        fun setUrl(url: String) = apply { this.url = url }

        fun setFichaTecnica(fichaTecnica: String) = apply { this.fichaTecnica = fichaTecnica }

        fun setProspecto(prospecto: String) = apply { this.prospecto = prospecto }

        fun setImagen(imagen: ByteArray) = apply { this.imagen = imagen }

        fun setLaboratorio(laboratorio: String) = apply { this.laboratorio = laboratorio }

        fun setDosis(dosis: String) = apply { this.dosis = dosis }

        fun setPrincipiosActivos(principioActivo: List<String>) =
            apply { this.principiosActivos = principioActivo }

        fun setReceta(receta: String) = apply { this.receta = receta }

        fun setFechaInicio(fechaInicio: Long) = apply { this.fechaInicio = fechaInicio }

        fun setFechaFin(fechaFin: Long) = apply { this.fechaFin = fechaFin }

        fun setHorario(horario: Set<Long>) = apply { this.horario = horario }

        fun setFavorito(isFavorite: Boolean) = apply { this.isFavorite = isFavorite }

        fun setSeHaTomado(seHaTomado: Boolean) = apply { this.seHaTomado = seHaTomado }
    }

    override fun hashCode(): Int {
        var result = nombre?.hashCode() ?: 0
        result = 31 * result + (codNacional ?: 0)
        result = 31 * result + (numRegistro?.hashCode() ?: 0)
        result = 31 * result + (url?.hashCode() ?: 0)
        result = 31 * result + (fichaTecnica?.hashCode() ?: 0)
        result = 31 * result + (prospecto?.hashCode() ?: 0)
        result = 31 * result + (imagen?.contentHashCode() ?: 0)
        result = 31 * result + (laboratorio?.hashCode() ?: 0)
        result = 31 * result + (dosis?.hashCode() ?: 0)
        result = 31 * result + (principiosActivos?.hashCode() ?: 0)
        result = 31 * result + (receta?.hashCode() ?: 0)
        result = 31 * result + (fechaInicio?.hashCode() ?: 0)
        result = 31 * result + (fechaFin?.hashCode() ?: 0)
        result = 31 * result + (horario?.hashCode() ?: 0)
        result = 31 * result + (isFavorite?.hashCode() ?: 0)
        result = 31 * result + (seHaTomado?.hashCode() ?: 0)
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Medicamento

        if (nombre != other.nombre) return false
        if (codNacional != other.codNacional) return false
        if (numRegistro != other.numRegistro) return false
        if (url != other.url) return false
        if (fichaTecnica != other.fichaTecnica) return false
        if (prospecto != other.prospecto) return false
        if (imagen != null) {
            if (other.imagen == null) return false
            if (!imagen.contentEquals(other.imagen)) return false
        } else if (other.imagen != null) return false
        if (laboratorio != other.laboratorio) return false
        if (dosis != other.dosis) return false
        if (principiosActivos != other.principiosActivos) return false
        if (receta != other.receta) return false
        if (fechaInicio != other.fechaInicio) return false
        if (fechaFin != other.fechaFin) return false
        if (horario != other.horario) return false
        if (isFavorite != other.isFavorite) return false
        return seHaTomado == other.seHaTomado
    }

    override fun toString(): String {
        return "Medicamento(nombre:String?=\"$nombre\", codNacional:Int?=\"$codNacional\", numRegistro:String?=\"$numRegistro\", url:String?=\"$url\", fichaTecnica:String?=\"$fichaTecnica\", prospecto:String?=\"$prospecto\", imagen:ByteArray?=\"$imagen\", imagenHashCode=\"${imagen.contentHashCode()}\", laboratorio:String?=\"$laboratorio\", dosis:String?=\"$dosis\", principiosActivos:List<String>?=\"$principiosActivos\", receta:String?=\"$receta\", fechaInicio:Long?=\"$fechaInicio\", fechaFin:Long?=\"$fechaFin\", horario:Set<Long>?=\"$horario\", isFavorite:Boolean?=\"$isFavorite\", seHaTomado:Boolean?=\"$seHaTomado\")"
    }

}