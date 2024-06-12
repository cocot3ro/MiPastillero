package com.a23pablooc.proxectofct.domain.model.extensions

import com.a23pablooc.proxectofct.data.database.entities.MedicamentoHistorialEntity
import com.a23pablooc.proxectofct.domain.model.MedicamentoHistorialItem

fun MedicamentoHistorialItem.toDatabase(): MedicamentoHistorialEntity {
    return MedicamentoHistorialEntity(
        pkMedicamentoHistorial = pkMedicamentoHistorial,
        fkUsuario = fkUsuario,
        fkMedicamentoActivo = fkMedicamentoActivo.pkMedicamentoActivo,
        fkMedicamento = fkMedicamento.toDatabase()
    )
}