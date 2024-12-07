package com.cocot3ro.mipastillero.data.database.entities.extensions

import com.cocot3ro.mipastillero.data.database.entities.MedicamentoActivoEntity
import com.cocot3ro.mipastillero.data.database.entities.MedicamentoEntity
import com.cocot3ro.mipastillero.domain.model.MedicamentoActivoItem

fun MedicamentoActivoEntity.toDomain(medEntity: MedicamentoEntity): MedicamentoActivoItem {
    return MedicamentoActivoItem(
        pkMedicamentoActivo = this.pkMedicamentoActivo,
        fkMedicamento = medEntity.toDomain(),
        fechaInicio = this.fechaInicio,
        fechaFin = this.fechaFin,
        dosis = this.dosis,
        tomas = this.tomas,
        horario = this.horario,
        fkUsuario = fkUsuario
    )
}