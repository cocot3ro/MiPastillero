package com.a23pablooc.proxectofct.data.database.entities.extensions

import com.a23pablooc.proxectofct.data.database.entities.MedicamentoEntity
import com.a23pablooc.proxectofct.domain.model.MedicamentoItem

fun MedicamentoEntity.toDomain(): MedicamentoItem {
    return MedicamentoItem(
        pkCodNacionalMedicamento = pkCodNacionalMedicamento,
        numRegistro = numRegistro,
        nombre = nombre,
        url = url,
        prospecto = prospecto,
        imagen = imagen,
        laboratorio = laboratorio,
        receta = receta,
        afectaConduccion = afectaConduccion,
        esFavorito = esFavorito,
        fkUsuario = fkUsuario,
        prescripcion = prescripcion,
        principiosActivos = principiosActivos
    )
}