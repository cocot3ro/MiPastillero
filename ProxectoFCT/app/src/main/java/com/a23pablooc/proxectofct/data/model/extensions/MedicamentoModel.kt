package com.a23pablooc.proxectofct.data.model.extensions

import androidx.core.net.toUri
import com.a23pablooc.proxectofct.data.model.MedicamentoModel
import com.a23pablooc.proxectofct.domain.model.MedicamentoItem

fun MedicamentoModel.toDomain(): MedicamentoItem {
    return MedicamentoItem(
        pkCodNacionalMedicamento = 0,
        fkUsuario = 0,
        url = url,
        prospecto = prospecto,
        prescripcion = prescripcion,
        numRegistro = numRegistro,
        laboratorio = laboratorio,
        imagen = imagen.toUri(),
        afectaConduccion = afectaConduccion,
        esFavorito = false,
        nombre = nombre
    )
}