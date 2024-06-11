package com.a23pablooc.proxectofct.domain.model.extensions

import com.a23pablooc.proxectofct.data.database.entities.HistorialEntity
import com.a23pablooc.proxectofct.domain.model.HistorialItem

fun HistorialItem.toDatabase(): HistorialEntity {
    return HistorialEntity(
        pkHistorial = pkHistorial,
        fkUsuario = fkUsuario,
        fkMedicamento = fkMedicamento.toDatabase(),
        fkMedicamentoActivo = fkMedicamentoActivo.toDatabase().medicamentosActivos[0],
    )
}