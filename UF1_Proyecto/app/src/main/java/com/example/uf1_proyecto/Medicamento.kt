package com.example.uf1_proyecto

import java.io.Serializable

/**
 * Clase que representa un medicamento
 * @property nombre nombre del medicamento
 * @property codNacional código nacional del medicamento
 * @property fichaTecnica url de la ficha técnica del medicamento
 * @property prospecto url del prospecto del medicamento
 * @property fechaInicio fecha de inicio del tratamiento
 * @property fechaFin fecha de fin del tratamiento
 * @property horario horario del tratamiento
 * @property isFavorite indica si el medicamento es favorito
 */
data class Medicamento(
    val nombre: String?,
    val codNacional: Int?,
    val fichaTecnica: String?,
    val prospecto: String?,
    val fechaInicio: Long?,
    val fechaFin: Long?,
    val horario: Set<Long>?,
    var isFavorite: Boolean?,
    var seHaTomado: Boolean?
) : Serializable