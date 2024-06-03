package com.a23pablooc.proxectofct.data.model

data class MedicamentoModel(
    var numRegistro: String,
    var nombre: String,
    var url: String,
    var prospecto: String,
    var imagen: String,
    var laboratorio: String,
    var prescripcion: String,
    var afectaConduccion: Boolean
) {
    class Builder {
        private var url: String = ""
        private var nombre: String = ""
        private var imagen: String = ""
        private var prospecto: String = ""
        private var numRegistro: String = ""
        private var laboratorio: String = ""
        private var prescripcion: String = ""
        private var afectaConduccion: Boolean = false

        fun numRegistro(numRegistro: String) = apply { this.numRegistro = numRegistro }
        fun nombre(nombre: String) = apply { this.nombre = nombre }
        fun url(url: String) = apply { this.url = url }
        fun prospecto(prospecto: String) = apply { this.prospecto = prospecto }
        fun imagen(imagen: String) = apply { this.imagen = imagen }
        fun laboratorio(laboratorio: String) = apply { this.laboratorio = laboratorio }
        fun prescripcion(prescripcion: String) = apply { this.prescripcion = prescripcion }
        fun afectaConduccion(afectaConduccion: Boolean) =
            apply { this.afectaConduccion = afectaConduccion }

        fun build(): MedicamentoModel {
            return MedicamentoModel(
                numRegistro,
                nombre,
                url,
                prospecto,
                imagen,
                laboratorio,
                prescripcion,
                afectaConduccion
            )
        }
    }

}