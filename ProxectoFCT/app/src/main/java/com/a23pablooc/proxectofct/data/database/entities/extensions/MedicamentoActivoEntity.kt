package com.a23pablooc.proxectofct.data.database.entities.extensions

import com.a23pablooc.proxectofct.data.database.entities.MedicamentoActivoEntity
import com.a23pablooc.proxectofct.data.database.entities.MedicamentoEntity
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem

fun MedicamentoActivoEntity.toDomain(medEntity: MedicamentoEntity): MedicamentoActivoItem {
    return MedicamentoActivoItem(
        pkMedicamentoActivo = this.pkMedicamentoActivo,
        fkMedicamento = medEntity.toDomain(),
        fechaInicio = this.fechaInicio,
        fechaFin = this.fechaFin,
        dosis = this.dosis,
        tomas = this.tomas,
        horario = this.horario,
    )
}