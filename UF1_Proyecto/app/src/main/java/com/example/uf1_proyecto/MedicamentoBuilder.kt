package com.example.uf1_proyecto

class MedicamentoBuilder {
    private var nombre: String? = null
    private var codNacional: Int? = null
    private var fichaTecnica: String? = null
    private var prospecto: String? = null
    private var fechaInicio: Long? = null
    private var fechaFin: Long? = null
    private var horario: Set<Long>? = null
    private var isFavorite: Boolean? = null
    private var seHaTomado: Boolean? = null

    fun build(): Medicamento {
        return Medicamento(
            nombre,
            codNacional,
            fichaTecnica,
            prospecto,
            fechaInicio,
            fechaFin,
            horario,
            isFavorite,
            seHaTomado
        )
    }

    fun setNombre(nombre: String) = apply { this.nombre = nombre }

    fun setCodNacional(codNacional: Int) = apply { this.codNacional = codNacional }

    fun setFichaTecnica(fichaTecnica: String) = apply { this.fichaTecnica = fichaTecnica }

    fun setProspecto(prospecto: String) = apply { this.prospecto = prospecto }

    fun setFechaInicio(fechaInicio: Long) = apply { this.fechaInicio = fechaInicio }

    fun setFechaFin(fechaFin: Long) = apply { this.fechaFin = fechaFin }

    fun setHorario(horario: Set<Long>) = apply { this.horario = horario }

    fun setFavorito(isFavorite: Boolean) = apply { this.isFavorite = isFavorite }

    fun setSeHaTomado(seHaTomado: Boolean) = apply { this.seHaTomado = seHaTomado }
}
