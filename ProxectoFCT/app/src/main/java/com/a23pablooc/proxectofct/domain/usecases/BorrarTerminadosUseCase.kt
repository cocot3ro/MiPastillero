package com.a23pablooc.proxectofct.domain.usecases

import com.a23pablooc.proxectofct.data.repositories.PillboxDbRepository
import com.a23pablooc.proxectofct.domain.model.HistorialItem
import javax.inject.Inject

class BorrarTerminadosUseCase @Inject constructor(private val repository: PillboxDbRepository) {

    suspend operator fun invoke() {
        val terminados = repository.getMedicamentosTerminados()

        terminados.forEach {
            repository.addHistorial(HistorialItem(
                fkMedicamento = it.fkMedicamento,
                fkMedicamentoActivo = it
            ))
        }

        repository.deleteMedicamentosTerminados(terminados)
    }
}