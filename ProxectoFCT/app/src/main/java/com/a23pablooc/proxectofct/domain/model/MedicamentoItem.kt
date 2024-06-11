package com.a23pablooc.proxectofct.domain.model

import android.net.Uri
import java.io.Serializable

data class MedicamentoItem(
    var pkCodNacionalMedicamento: Long,
    var fkUsuario: Long,
    var url: String,
    var nombre: String,
    var prospecto: String,
    var numRegistro: String,
    var laboratorio: String,
    var esFavorito: Boolean,
    var imagen: Uri,
    var prescripcion: String,
    var afectaConduccion: Boolean
) : Serializable