package com.a23pablooc.proxectofct.domain.model.extensions

import com.a23pablooc.proxectofct.core.UserInfoProvider
import com.a23pablooc.proxectofct.data.database.entities.MedicamentoEntity
import com.a23pablooc.proxectofct.domain.model.MedicamentoItem

fun MedicamentoItem.toDatabase(): MedicamentoEntity {
    return MedicamentoEntity(
        id = id,
        userId = UserInfoProvider.userId,
        numRegistro = numRegistro,
        nombre = nombre,
        url = url,
        prospecto = prospecto,
        prescripcion = prescripcion,
        afectaConduccion = afectaConduccion,
        imagen = imagen,
        laboratorio = laboratorio,
        esFavorito = esFavorito
    )
}