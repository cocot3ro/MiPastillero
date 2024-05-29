package com.a23pablooc.proxectofct.data.model

data class MedicamentoModel(
    val numRegistro: String,
    val nombre: String,
    val url: String,
    val prospecto: String,
    val apiImagen: String,
    val laboratorio: String,
    val prescripcion: String,
    val conduccion: Boolean
) {
    class Builder {
        private var numRegistro: String = ""
        private var nombre: String = ""
        private var url: String = ""
        private var prospecto: String = ""
        private var apiImagen: String = ""
        private var laboratorio: String = ""
        private var prescripcion: String = ""
        private var conduccion: Boolean = false

        fun numRegistro(numRegistro: String) = apply { this.numRegistro = numRegistro }
        fun nombre(nombre: String) = apply { this.nombre = nombre }
        fun url(url: String) = apply { this.url = url }
        fun prospecto(prospecto: String) = apply { this.prospecto = prospecto }
        fun apiImagen(apiImagen: String) = apply { this.apiImagen = apiImagen }
        fun laboratorio(laboratorio: String) = apply { this.laboratorio = laboratorio }
        fun prescripcion(prescripcion: String) = apply { this.prescripcion = prescripcion }
        fun conduccion(conduccion: Boolean) = apply { this.conduccion = conduccion }

        fun build() = MedicamentoModel(
            numRegistro,
            nombre,
            url,
            prospecto,
            apiImagen,
            laboratorio,
            prescripcion,
            conduccion
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MedicamentoModel

        return ((numRegistro != other.numRegistro)
                || (nombre != other.nombre)
                || (url != other.url)
                || (prospecto != other.prospecto)
                || (apiImagen != other.apiImagen)
                || (laboratorio != other.laboratorio)
                || (prescripcion != other.prescripcion)
                || (conduccion != other.conduccion))
    }

    override fun hashCode(): Int {
        var result = numRegistro.hashCode()
        result = 31 * result + nombre.hashCode()
        result = 31 * result + url.hashCode()
        result = 31 * result + prospecto.hashCode()
        result = 31 * result + apiImagen.hashCode()
        result = 31 * result + laboratorio.hashCode()
        result = 31 * result + prescripcion.hashCode()
        result = 31 * result + conduccion.hashCode()
        return result
    }

}