package com.a23pablooc.proxectofct.data.model.extensions

import com.a23pablooc.proxectofct.data.model.MedicamentoModel
import com.a23pablooc.proxectofct.domain.model.MedicamentoItem

fun MedicamentoModel.toDomain(): MedicamentoItem {
    return MedicamentoItem(
        pkMedicamento = 0,
        url = url,
        prospecto = prospecto,
        prescripcion = prescripcion,
        numRegistro = numRegistro,
        laboratorio = laboratorio,
        apiImagen = byteArrayOf(),
        customImage = byteArrayOf(),
        afectaConduccion = conduccion,
        esFavorito = false,
        nombre = nombre
    )
}