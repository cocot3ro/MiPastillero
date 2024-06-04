package com.a23pablooc.proxectofct.domain.model.extensions

import com.a23pablooc.proxectofct.core.UserInfoProvider
import com.a23pablooc.proxectofct.data.database.entities.HistorialEntity
import com.a23pablooc.proxectofct.domain.model.HistorialItem

fun HistorialItem.toDatabase(): HistorialEntity {
    return HistorialEntity(
        pkHistorial = pkHistorial,
        fkUsuario = UserInfoProvider.currentUser.pkUsuario,
        fkMedicamento = fkMedicamento.toDatabase(),
        fkMedicamentoActivo = fkMedicamentoActivo.toDatabase().medicamentosActivos[0],
    )
}