package com.a23pablooc.proxectofct.domain.model.extensions

import com.a23pablooc.proxectofct.core.UserInfoProvider
import com.a23pablooc.proxectofct.data.database.entities.MedicamentoActivoAndMedicamento
import com.a23pablooc.proxectofct.data.database.entities.MedicamentoActivoEntity
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem

fun MedicamentoActivoItem.toDatabase(): MedicamentoActivoAndMedicamento {
    return MedicamentoActivoAndMedicamento(
        medicamentoActivoEntity = MedicamentoActivoEntity(
            pkMedicamentoActivo = pkMedicamentoActivo,
            fkMedicamento = fkMedicamento.pkCodNacionalMedicamento,
            fkUsuario = UserInfoProvider.currentUser.pkUsuario,
            dosis = dosis,
            fechaFin = fechaFin,
            fechaInicio = fechaInicio,
            horario = horario,
            tomas = tomas
        ),
        medicamento = fkMedicamento.toDatabase()
    )
}