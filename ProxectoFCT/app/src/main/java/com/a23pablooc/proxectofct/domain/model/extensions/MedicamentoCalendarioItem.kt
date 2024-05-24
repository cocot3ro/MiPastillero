package com.a23pablooc.proxectofct.domain.model.extensions

import com.a23pablooc.proxectofct.core.UserInfoProvider
import com.a23pablooc.proxectofct.data.database.entities.MedicamentoCalendarioAndMedicamento
import com.a23pablooc.proxectofct.data.database.entities.MedicamentoCalendarioEntity
import com.a23pablooc.proxectofct.domain.model.MedicamentoCalendarioItem

fun MedicamentoCalendarioItem.toDatabase(): MedicamentoCalendarioAndMedicamento {
    return MedicamentoCalendarioAndMedicamento(
        medicamento = medicamento.toDatabase(),
        medicamentoCalendarioEntity = MedicamentoCalendarioEntity(
            id = id,
            hora = hora,
            idUsuario = UserInfoProvider.id,
            idMedicamento = medicamento.id,
            seHaTomado = seHaTomado,
            fecha = fecha
        )
    )

}