package com.cocot3ro.mipastillero.domain.model

import android.net.Uri

data class MedicamentoItem(
    var pkCodNacionalMedicamento: Long,
    var fkUsuario: Long,
    var url: Uri,
    var nombre: String,
    var prospecto: Uri,
    var numRegistro: String,
    var laboratorio: String,
    var esFavorito: Boolean,
    var imagen: Uri,
    var receta: Boolean,
    var afectaConduccion: Boolean,
    var principiosActivos: List<String>,
    var prescripcion: String
)