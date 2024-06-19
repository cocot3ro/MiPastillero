package com.a23pablooc.proxectofct.domain.usecases

import android.net.Uri
import com.a23pablooc.proxectofct.core.DataStoreManager
import com.a23pablooc.proxectofct.data.network.CimaApiDefinitions
import com.a23pablooc.proxectofct.data.repositories.PillboxDbRepository
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem
import com.a23pablooc.proxectofct.domain.model.MedicamentoItem
import com.a23pablooc.proxectofct.domain.model.extensions.toDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddMedicamentoUseCase @Inject constructor(
    private val downloadImageUseCase: DownloadImageUseCase,
    private val saveImageUseCase: SaveImageUseCase,
    private val repository: PillboxDbRepository,
    private val dataStoreManager: DataStoreManager
) {
    suspend fun invoke(med: MedicamentoActivoItem) {
        val dbMed = med.toDatabase()
        val insertedCodNacional = invoke(med.fkMedicamento)
        repository.addMedicamentoActivo(dbMed.medicamentosActivos[0].apply {
            fkMedicamento = insertedCodNacional.takeIf { it != -1L }
                ?: dbMed.medicamento.pkCodNacionalMedicamento
        })

        withContext(Dispatchers.IO) {
            val imgPath = med.fkMedicamento.imagen.toString()

            if (imgPath.startsWith(CimaApiDefinitions.BASE_URL)) {
                med.fkMedicamento.imagen = if (dataStoreManager.useImages().first()) {

                    val imageData = downloadImageUseCase.invoke(
                        med.fkMedicamento.numRegistro,
                        imgPath.substringAfterLast('/')
                    )

                    val localStoragePath =
                        saveImageUseCase.invoke(
                            "${med.fkMedicamento.numRegistro}.${
                                imgPath.substringAfterLast(
                                    '.'
                                )
                            }", imageData
                        )

                    localStoragePath
                } else {
                    Uri.EMPTY
                }
            }

            repository.updateMedicamento(med.fkMedicamento)
        }
    }

    suspend fun invoke(med: MedicamentoItem): Long {
        return repository.upsertMedicamento(med)
    }
}