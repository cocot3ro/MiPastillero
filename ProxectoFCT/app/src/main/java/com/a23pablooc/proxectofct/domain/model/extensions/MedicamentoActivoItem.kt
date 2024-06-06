package com.a23pablooc.proxectofct.domain.model.extensions

import com.a23pablooc.proxectofct.data.database.entities.MedicamentoActivoEntity
import com.a23pablooc.proxectofct.data.database.entities.MedicamentoWithMedicamentoActivo
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem

fun MedicamentoActivoItem.toDatabase(userId: Long): MedicamentoWithMedicamentoActivo {
    return MedicamentoWithMedicamentoActivo(
        medicamentosActivos = listOf(
            MedicamentoActivoEntity(
                pkMedicamentoActivo = pkMedicamentoActivo,
                fkMedicamento = fkMedicamento.pkCodNacionalMedicamento,
                fkUsuario = userId,
                dosis = dosis,
                fechaFin = fechaFin,
                fechaInicio = fechaInicio,
                horario = horario,
                tomas = tomas
            )
        ),
        medicamento = fkMedicamento.toDatabase(userId)
    )
}