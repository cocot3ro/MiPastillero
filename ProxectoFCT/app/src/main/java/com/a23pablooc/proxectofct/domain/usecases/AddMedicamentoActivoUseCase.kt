package com.a23pablooc.proxectofct.domain.usecases

import com.a23pablooc.proxectofct.core.UserInfoProvider
import com.a23pablooc.proxectofct.data.network.CimaApiDefinitions
import com.a23pablooc.proxectofct.data.repositories.PillboxDbRepository
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem
import javax.inject.Inject

class AddMedicamentoActivoUseCase @Inject constructor(
    private val downloadImageUseCase: DownloadImageUseCase,
    private val saveImageUseCase: SaveImageUseCase,
    private val userInfoProvider: UserInfoProvider,
    private val repository: PillboxDbRepository
) {
    suspend operator fun invoke(med: MedicamentoActivoItem) {
        val imgPath = med.fkMedicamento.imagen.toString()

        if (imgPath.startsWith(CimaApiDefinitions.BASE_URL)) {
            val imageData = downloadImageUseCase.invoke(
                med.fkMedicamento.numRegistro,
                imgPath.substringAfterLast('/')
            )

            val localStoragePath =
                saveImageUseCase.invoke(
                    "${userInfoProvider.currentUser.pkUsuario}_${med.fkMedicamento.numRegistro}.jpg",
                    imageData
                )

            med.fkMedicamento.imagen = localStoragePath
        }

        repository.addMedicamentoActivo(med)
    }
}