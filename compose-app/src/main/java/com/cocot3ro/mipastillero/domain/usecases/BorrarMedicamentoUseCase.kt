package com.cocot3ro.mipastillero.domain.usecases

import com.cocot3ro.mipastillero.data.repositories.MiPastilleroDbRepository
import com.cocot3ro.mipastillero.domain.model.MedicamentoActivoItem
import javax.inject.Inject

class BorrarMedicamentoUseCase @Inject constructor(
    private val miPastilleroDbRepository: MiPastilleroDbRepository,
    private val cancelarNotificacionesUseCase: CancelarNotificacionesUseCase
) {

    suspend fun invoke(med: MedicamentoActivoItem) {
        cancelarNotificacionesUseCase.invoke(med)
        miPastilleroDbRepository.deleteMed(med)
    }
}