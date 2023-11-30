package com.example.uf1_proyecto

data class Medicamento(
    val codNacional: Int,
    val nombre: String,
    val fichaTecnica: String,
    val prospecto: String,
    val fechaInicio: Long,
    val fechaFin: Long,
    val horario: List<Long>,
    var isFavorite: Boolean
)