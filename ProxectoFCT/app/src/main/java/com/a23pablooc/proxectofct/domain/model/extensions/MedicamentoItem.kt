package com.a23pablooc.proxectofct.domain.model.extensions

import com.a23pablooc.proxectofct.core.UserInfoProvider
import com.a23pablooc.proxectofct.data.database.entities.MedicamentoEntity
import com.a23pablooc.proxectofct.domain.model.MedicamentoItem

fun MedicamentoItem.toDatabase(): MedicamentoEntity {
    return MedicamentoEntity(
        pkMedicamento = pkMedicamento,
        fkUsuario = UserInfoProvider.currentUser.pkUsuario,
        numRegistro = numRegistro,
        nombre = nombre,
        url = url,
        prospecto = prospecto,
        prescripcion = prescripcion,
        afectaConduccion = afectaConduccion,
        apiImagen = apiImagen,
        laboratorio = laboratorio,
        esFavorito = esFavorito,
        customImage = customImage
    )
}