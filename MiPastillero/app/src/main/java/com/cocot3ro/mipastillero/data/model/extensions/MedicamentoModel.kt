package com.cocot3ro.mipastillero.data.model.extensions

import com.cocot3ro.mipastillero.data.model.MedicamentoModel
import com.cocot3ro.mipastillero.domain.model.MedicamentoItem

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