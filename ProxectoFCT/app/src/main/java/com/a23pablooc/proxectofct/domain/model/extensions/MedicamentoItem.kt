package com.a23pablooc.proxectofct.domain.model.extensions

import com.a23pablooc.proxectofct.data.database.entities.MedicamentoEntity
import com.a23pablooc.proxectofct.domain.model.MedicamentoItem

fun MedicamentoItem.toDatabase(userId: Long): MedicamentoEntity {
    return MedicamentoEntity(
        pkCodNacionalMedicamento = pkCodNacionalMedicamento,
        fkUsuario = userId,
        numRegistro = numRegistro,
        nombre = nombre,
        url = url,
        prospecto = prospecto,
        prescripcion = prescripcion,
        afectaConduccion = afectaConduccion,
        imagen = imagen,
        laboratorio = laboratorio,
        esFavorito = esFavorito,
    )
}