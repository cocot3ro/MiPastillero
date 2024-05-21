package com.a23pablooc.proxectofct.data.database.extensions

import com.a23pablooc.proxectofct.data.database.entities.MedicamentoEntity
import com.a23pablooc.proxectofct.domain.model.MedicamentoItem

fun MedicamentoEntity.toDomain(): MedicamentoItem {
    return MedicamentoItem(
        id = id,
        numRegistro = numRegistro,
        nombre = nombre,
        url = url,
        prospecto = prospecto,
        imagen = imagen,
        laboratorio = laboratorio,
        prescripcion = prescripcion,
        afectaConduccion = afectaConduccion
    )
}