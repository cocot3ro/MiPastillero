package com.a23pablooc.proxectofct.data.model

data class MedicamentoModel(
    val numRegistro: String,
    val nombre: String,
    val url: String,
    val prospecto: String,
    val apiImagen: String,
    val laboratorio: String,
    val prescripcion: String,
    val afectaConduccion: Boolean
) {
    class Builder {
        private var url: String? = null
        private var nombre: String? = null
        private var apiImagen: String? = null
        private var prospecto: String? = null
        private var numRegistro: String? = null
        private var laboratorio: String? = null
        private var prescripcion: String? = null
        private var afectaConduccion: Boolean? = null

        fun numRegistro(numRegistro: String) = apply { this.numRegistro = numRegistro }
        fun nombre(nombre: String) = apply { this.nombre = nombre }
        fun url(url: String) = apply { this.url = url }
        fun prospecto(prospecto: String) = apply { this.prospecto = prospecto }
        fun apiImagen(apiImagen: String) = apply { this.apiImagen = apiImagen }
        fun laboratorio(laboratorio: String) = apply { this.laboratorio = laboratorio }
        fun prescripcion(prescripcion: String) = apply { this.prescripcion = prescripcion }
        fun afectaConduccion(afectaConduccion: Boolean) = apply { this.afectaConduccion = afectaConduccion }

        /**
         * Builds the [MedicamentoModel] instance
         * @return a [MedicamentoModel] instance
         * @throws IllegalStateException if the [numRegistro], [nombre], [url], [prospecto], [apiImagen], [laboratorio], [prescripcion] or [afectaConduccion] is null
         */
        fun build(): MedicamentoModel {
            try {
                return MedicamentoModel(
                    numRegistro!!,
                    nombre!!,
                    url!!,
                    prospecto!!,
                    apiImagen!!,
                    laboratorio!!,
                    prescripcion!!,
                    afectaConduccion!!
                )
            } catch (e: NullPointerException) {
                throw IllegalStateException("The numRegistro, nombre, url, prospecto, apiImagen, laboratorio, prescripcion and afectaConduccion must not be null")
            }
        }
    }

}