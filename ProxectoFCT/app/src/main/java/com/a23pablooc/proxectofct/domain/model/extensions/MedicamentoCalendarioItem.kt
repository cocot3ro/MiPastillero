package com.a23pablooc.proxectofct.domain.model.extensions

import com.a23pablooc.proxectofct.core.UserInfoProvider
import com.a23pablooc.proxectofct.data.database.entities.MedicamentoCalendarioAndMedicamento
import com.a23pablooc.proxectofct.data.database.entities.MedicamentoCalendarioEntity
import com.a23pablooc.proxectofct.domain.model.MedicamentoCalendarioItem

fun MedicamentoCalendarioItem.toDatabase(): MedicamentoCalendarioAndMedicamento {
    return MedicamentoCalendarioAndMedicamento(
        medicamento = fkMedicamento.toDatabase(),
        medicamentoCalendarioEntity = MedicamentoCalendarioEntity(
            pkMedicamentoCalendario = pkMedicamentoCalendario,
            fkMedicamento = fkMedicamento.pkCodNacionalMedicamento,
            fkUsuario = UserInfoProvider.currentUser.pkUsuario,
            hora = hora,
            fecha = fecha,
            seHaTomado = seHaTomado
        )
    )

}