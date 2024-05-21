package com.a23pablooc.proxectofct.data.database.extensions

import com.a23pablooc.proxectofct.data.database.entities.MedicamentoCalendarioAndMedicamento
import com.a23pablooc.proxectofct.domain.model.MedicamentoCalendarioItem

fun MedicamentoCalendarioAndMedicamento.toDomain(): MedicamentoCalendarioItem {
    return MedicamentoCalendarioItem(
        id = medicamentoCalendarioEntity.id,
        medicamento = medicamento.toDomain(),
        idUsuario = medicamentoCalendarioEntity.idUsuario,
        fecha = medicamentoCalendarioEntity.fecha,
        hora = medicamentoCalendarioEntity.hora,
        seHaTomado = medicamentoCalendarioEntity.seHaTomado
    )
}