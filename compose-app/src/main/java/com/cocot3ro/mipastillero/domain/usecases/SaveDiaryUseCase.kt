package com.cocot3ro.mipastillero.domain.usecases

import com.cocot3ro.mipastillero.data.repositories.MiPastilleroDbRepository
import com.cocot3ro.mipastillero.domain.model.AgendaItem
import javax.inject.Inject

class SaveDiaryUseCase @Inject constructor(
    private val miPastilleroDbRepository: MiPastilleroDbRepository
) {
    suspend fun invoke(item: AgendaItem) {
        if (item.descripcion.isBlank()) {
            miPastilleroDbRepository.deleteDiaryEntry(item)
        } else {
            miPastilleroDbRepository.saveDiaryEntry(item)
        }
    }
}