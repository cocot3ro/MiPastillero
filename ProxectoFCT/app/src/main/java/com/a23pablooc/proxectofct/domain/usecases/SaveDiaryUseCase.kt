package com.a23pablooc.proxectofct.domain.usecases

import com.a23pablooc.proxectofct.data.repositories.PillboxDbRepository
import com.a23pablooc.proxectofct.domain.model.AgendaItem
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