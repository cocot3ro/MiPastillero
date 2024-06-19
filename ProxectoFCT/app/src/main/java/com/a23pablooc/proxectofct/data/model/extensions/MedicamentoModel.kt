package com.a23pablooc.proxectofct.data.model.extensions

import com.a23pablooc.proxectofct.data.model.MedicamentoModel
import com.a23pablooc.proxectofct.domain.model.MedicamentoItem

fun MedicamentoModel.toDomain(): MedicamentoItem {
    return MedicamentoItem(
        pkCodNacionalMedicamento = 0,
        fkUsuario = 0,
        esFavorito = false,
        url = url,
        prospecto = prospecto,
        receta = receta,
        prescripcion = prescripcion,
        numRegistro = numRegistro,
        laboratorio = laboratorio,
        imagen = imagen,
        afectaConduccion = afectaConduccion,
        nombre = nombre,
        principiosActivos = pricipiosActivos
    )
}