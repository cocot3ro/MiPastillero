package com.a23pablooc.proxectofct.domain.usecases

import com.a23pablooc.proxectofct.data.repositories.PillboxDbRepository
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem
import javax.inject.Inject

class BorrarMedicamentoUseCase @Inject constructor(
    private val pillboxDbRepository: PillboxDbRepository,
    private val cancelarNotificacionesUseCase: CancelarNotificacionesUseCase
) {

    suspend fun invoke(med: MedicamentoActivoItem) {
        cancelarNotificacionesUseCase.invoke(med)
        pillboxDbRepository.deleteMed(med)
    }
}