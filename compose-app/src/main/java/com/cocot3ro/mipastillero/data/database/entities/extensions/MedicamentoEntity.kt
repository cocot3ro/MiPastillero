package com.cocot3ro.mipastillero.data.database.entities.extensions

import com.cocot3ro.mipastillero.data.database.entities.MedicamentoEntity
import com.cocot3ro.mipastillero.domain.model.MedicamentoItem

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