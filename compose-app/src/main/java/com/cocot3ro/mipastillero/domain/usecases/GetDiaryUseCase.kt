package com.cocot3ro.mipastillero.domain.usecases

import com.cocot3ro.mipastillero.data.repositories.MiPastilleroDbRepository
import com.cocot3ro.mipastillero.domain.model.AgendaItem
import kotlinx.coroutines.flow.Flow
import java.util.Date
import javax.inject.Inject

class GetDiaryUseCase @Inject constructor(
    private val miPastilleroDbRepository: MiPastilleroDbRepository
) {
    fun invoke(date: Date): Flow<List<AgendaItem>> {
        return miPastilleroDbRepository.getDiaryFlow(date)
    }
}