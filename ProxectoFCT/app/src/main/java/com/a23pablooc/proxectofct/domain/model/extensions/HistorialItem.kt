package com.a23pablooc.proxectofct.domain.model.extensions

import com.a23pablooc.proxectofct.data.database.entities.HistorialEntity
import com.a23pablooc.proxectofct.domain.model.HistorialItem

fun HistorialItem.toDatabase(userId: Long): HistorialEntity {
    return HistorialEntity(
        pkHistorial = pkHistorial,
        fkUsuario = userId,
        fkMedicamento = fkMedicamento.toDatabase(userId),
        fkMedicamentoActivo = fkMedicamentoActivo.toDatabase(userId).medicamentosActivos[0],
    )
}