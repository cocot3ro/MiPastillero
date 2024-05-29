package com.a23pablooc.proxectofct.data.database.entities.extensions

import com.a23pablooc.proxectofct.data.database.entities.MedicamentoActivoAndMedicamento
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem

fun MedicamentoActivoAndMedicamento.toDomain(): MedicamentoActivoItem {
    return MedicamentoActivoItem(
        pkMedicamentoActivo = medicamentoActivoEntity.pkMedicamentoActivo,
        dosis = medicamentoActivoEntity.dosis,
        fechaFin = medicamentoActivoEntity.fechaFin,
        fechaInicio = medicamentoActivoEntity.fechaInicio,
        horario = medicamentoActivoEntity.horario,
        fkMedicamento = medicamento.toDomain()
    )
}