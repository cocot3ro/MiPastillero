package com.a23pablooc.proxectofct.domain.model

import lombok.EqualsAndHashCode

@EqualsAndHashCode
data class MedicamentoFavoritoItem(
    val id: Int,
    val medicamento: MedicamentoItem,
    val usuario: UsuarioItem
)