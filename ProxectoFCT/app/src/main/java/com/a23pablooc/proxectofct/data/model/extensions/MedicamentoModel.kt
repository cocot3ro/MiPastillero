package com.a23pablooc.proxectofct.data.model.extensions

import com.a23pablooc.proxectofct.data.model.MedicamentoModel
import com.a23pablooc.proxectofct.domain.model.MedicamentoItem

fun MedicamentoModel.toDomain(): MedicamentoItem {
    return MedicamentoItem(
        id = 0,
        url = url,
        prospecto = prospecto,
        prescripcion = prescripcion,
        numRegistro = numRegistro,
        laboratorio = laboratorio,
        imagen = byteArrayOf(),
        afectaConduccion = conduccion,
        esFavorito = false,
        nombre = nombre
    )
}