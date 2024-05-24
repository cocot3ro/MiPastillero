package com.a23pablooc.proxectofct.data.database.entities.extensions

import com.a23pablooc.proxectofct.data.database.entities.MedicamentoFavoritoAndMedicamento
import com.a23pablooc.proxectofct.domain.model.MedicamentoFavoritoItem

fun MedicamentoFavoritoAndMedicamento.toDomain(): MedicamentoFavoritoItem {
    return MedicamentoFavoritoItem(
        id = medicamento.id,
        medicamento = medicamento.toDomain()
    )
}