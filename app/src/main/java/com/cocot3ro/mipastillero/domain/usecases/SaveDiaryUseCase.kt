package com.cocot3ro.mipastillero.domain.usecases

import com.cocot3ro.mipastillero.data.repositories.PillboxDbRepository
import com.cocot3ro.mipastillero.domain.model.AgendaItem
import javax.inject.Inject

class SaveDiaryUseCase @Inject constructor(
    private val pillboxDbRepository: PillboxDbRepository
) {
    suspend fun invoke(item: AgendaItem) {
        if (item.descripcion.isBlank()) {
            pillboxDbRepository.deleteDiaryEntry(item)
        } else {
            pillboxDbRepository.saveDiaryEntry(item)
        }
    }
}