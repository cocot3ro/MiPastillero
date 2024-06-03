package com.a23pablooc.proxectofct.data.model.extensions

import com.a23pablooc.proxectofct.data.model.MedicamentoModel
import com.a23pablooc.proxectofct.domain.model.MedicamentoItem

fun MedicamentoModel.toDomain(): MedicamentoItem {
    return MedicamentoItem(
        pkCodNacionalMedicamento = 0,
        url = url,
        prospecto = prospecto,
        prescripcion = prescripcion,
        numRegistro = numRegistro,
        laboratorio = laboratorio,
        imagen = imagen,
        afectaConduccion = afectaConduccion,
        esFavorito = false,
        nombre = nombre,
        alias = nombre
    )
}