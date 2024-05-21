package com.a23pablooc.proxectofct.data.model

import lombok.EqualsAndHashCode

@EqualsAndHashCode
data class MedicamentoModel(
    val numRegistro: String,
    val nombre: String,
    val url: String,
    val prospecto: String,
    val imagen: String,
    val laboratorio: String,
    val prescripcion: String,
    val conduccion: Boolean
) {
    class Builder {
        private var numRegistro: String = ""
        private var nombre: String = ""
        private var url: String = ""
        private var prospecto: String = ""
        private var imagen: String = ""
        private var laboratorio: String = ""
        private var prescripcion: String = ""
        private var conduccion: Boolean = false

        fun numRegistro(numRegistro: String) = apply { this.numRegistro = numRegistro }
        fun nombre(nombre: String) = apply { this.nombre = nombre }
        fun url(url: String) = apply { this.url = url }
        fun prospecto(prospecto: String) = apply { this.prospecto = prospecto }
        fun imagen(imagen: String) = apply { this.imagen = imagen }
        fun laboratorio(laboratorio: String) = apply { this.laboratorio = laboratorio }
        fun prescripcion(prescripcion: String) = apply { this.prescripcion = prescripcion }
        fun conduccion(conduccion: Boolean) = apply { this.conduccion = conduccion }

        fun build() = MedicamentoModel(
            numRegistro,
            nombre,
            url,
            prospecto,
            imagen,
            laboratorio,
            prescripcion,
            conduccion
        )
    }
}