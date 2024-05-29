package com.a23pablooc.proxectofct.data.database.entities.extensions

import com.a23pablooc.proxectofct.data.database.entities.MedicamentoEntity
import com.a23pablooc.proxectofct.domain.model.MedicamentoItem

fun MedicamentoEntity.toDomain(): MedicamentoItem {
    return MedicamentoItem(
        pkMedicamento = pkMedicamento,
        numRegistro = numRegistro,
        nombre = nombre,
        alias = alias,
        url = url,
        prospecto = prospecto,
        customImage = customImage,
        apiImagen = apiImagen,
        laboratorio = laboratorio,
        prescripcion = prescripcion,
        afectaConduccion = afectaConduccion,
        esFavorito = esFavorito
    )
}