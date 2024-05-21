package com.a23pablooc.proxectofct.domain.model

import lombok.EqualsAndHashCode

@EqualsAndHashCode
data class MedicamentoItem(
    val id: Int,
    val numRegistro: String,
    val nombre: String,
    val url: String,
    val prospecto: String,
    val imagen: ByteArray,
    val laboratorio: String,
    val prescripcion: String,
    val afectaConduccion: Boolean
)