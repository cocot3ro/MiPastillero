package com.example.uf1_proyecto

import java.io.Serializable

data class Medicamento(
    val nombre: String,
    val codNacional: Int,
    val fichaTecnica: String,
    val prospecto: String,
    val fechaInicio: Long,
    val fechaFin: Long,
    val horario: List<Long>,
    var isFavorite: Boolean
) : Serializable