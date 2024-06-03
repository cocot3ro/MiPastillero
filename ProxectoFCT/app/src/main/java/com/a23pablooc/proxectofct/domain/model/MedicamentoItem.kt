package com.a23pablooc.proxectofct.domain.model

import android.net.Uri

data class MedicamentoItem(
    var pkCodNacionalMedicamento: Long,
    var url: String,
    var nombre: String,
    var prospecto: String,
    var numRegistro: String,
    var laboratorio: String,
    var esFavorito: Boolean,
    var imagen: Uri,
    var prescripcion: String,
    var afectaConduccion: Boolean
)