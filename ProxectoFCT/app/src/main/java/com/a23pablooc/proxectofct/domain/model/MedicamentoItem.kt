package com.a23pablooc.proxectofct.domain.model

data class MedicamentoItem(
    var pkCodNacionalMedicamento: Long,
    var url: String,
    var nombre: String,
    var alias: String,
    var prospecto: String,
    var numRegistro: String,
    var laboratorio: String,
    var esFavorito: Boolean,
    var imagen: String,
    var prescripcion: String,
    var afectaConduccion: Boolean
)