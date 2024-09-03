package com.cocot3ro.mipastillero.data.model

import android.net.Uri
import androidx.core.net.toUri

data class MedicamentoModel(
    var numRegistro: String,
    var nombre: String,
    var url: Uri,
    var prospecto: Uri,
    var imagen: Uri,
    var laboratorio: String,
    var receta: Boolean,
    var prescripcion: String,
    var afectaConduccion: Boolean,
    var pricipiosActivos: List<String>
) {
    class Builder {
        private var url: String = ""
        private var nombre: String = ""
        private var imagen: String = ""
        private var receta: Boolean = false
        private var prospecto: String = ""
        private var numRegistro: String = ""
        private var laboratorio: String = ""
        private var prescripcion: String = ""
        private var afectaConduccion: Boolean = false
        private var pricipiosActivos: MutableList<String> = mutableListOf()

        fun url(url: String) = apply { this.url = url }

        fun nombre(nombre: String) = apply { this.nombre = nombre }

        fun imagen(imagen: String) = apply { this.imagen = imagen }

        fun receta(receta: Boolean) = apply { this.receta = receta }

        fun prospecto(prospecto: String) = apply { this.prospecto = prospecto }

        fun numRegistro(numRegistro: String) = apply { this.numRegistro = numRegistro }

        fun laboratorio(laboratorio: String) = apply { this.laboratorio = laboratorio }

        fun prescripcion(prescripcion: String) = apply { this.prescripcion = prescripcion }

        fun afectaConduccion(afectaConduccion: Boolean) =
            apply { this.afectaConduccion = afectaConduccion }

        fun pricipiosActivos(pricipioActivo: String) =
            apply { this.pricipiosActivos.add(pricipioActivo) }

        fun build(): MedicamentoModel {
            return MedicamentoModel(
                url = url.toUri(),
                imagen = imagen.toUri(),
                nombre = nombre,
                receta = receta,
                prospecto = prospecto.toUri(),
                laboratorio = laboratorio,
                numRegistro = numRegistro,
                prescripcion = prescripcion,
                afectaConduccion = afectaConduccion,
                pricipiosActivos = pricipiosActivos
            )
        }
    }
}