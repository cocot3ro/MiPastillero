package com.cocot3ro.mipastillero.domain.model.extensions

import com.cocot3ro.mipastillero.data.database.entities.MedicamentoEntity
import com.cocot3ro.mipastillero.domain.model.MedicamentoItem

fun MedicamentoItem.toDatabase(): MedicamentoEntity {
    return MedicamentoEntity(
        pkCodNacionalMedicamento = pkCodNacionalMedicamento,
        fkUsuario = fkUsuario,
        numRegistro = numRegistro,
        nombre = nombre,
        url = url,
        prospecto = prospecto,
        receta = receta,
        afectaConduccion = afectaConduccion,
        imagen = imagen,
        laboratorio = laboratorio,
        esFavorito = esFavorito,
        prescripcion = prescripcion,
        principiosActivos = principiosActivos
    )
}