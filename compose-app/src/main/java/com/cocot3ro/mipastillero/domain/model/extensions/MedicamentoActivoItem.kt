package com.cocot3ro.mipastillero.domain.model.extensions

import com.cocot3ro.mipastillero.data.database.entities.MedicamentoActivoEntity
import com.cocot3ro.mipastillero.data.database.relationships.MedicamentoWithMedicamentoActivo
import com.cocot3ro.mipastillero.domain.model.MedicamentoActivoItem

fun MedicamentoActivoItem.toDatabase(): MedicamentoWithMedicamentoActivo {
    return MedicamentoWithMedicamentoActivo(
        medicamentosActivos = listOf(
            MedicamentoActivoEntity(
                pkMedicamentoActivo = pkMedicamentoActivo,
                fkMedicamento = fkMedicamento.pkCodNacionalMedicamento,
                fkUsuario = fkUsuario,
                dosis = dosis,
                fechaFin = fechaFin,
                fechaInicio = fechaInicio,
                horario = horario,
                tomas = tomas
            )
        ),
        medicamento = fkMedicamento.toDatabase()
    )
}