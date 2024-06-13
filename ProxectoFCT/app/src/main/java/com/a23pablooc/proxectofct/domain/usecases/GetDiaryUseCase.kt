package com.a23pablooc.proxectofct.domain.usecases

import com.a23pablooc.proxectofct.data.repositories.PillboxDbRepository
import com.a23pablooc.proxectofct.domain.model.AgendaItem
import kotlinx.coroutines.flow.Flow
import java.util.Date
import javax.inject.Inject

class GetDiaryUseCase @Inject constructor(
    private val pillboxDbRepository: PillboxDbRepository
) {
    fun invoke(date: Date): Flow<List<AgendaItem>> {
        return pillboxDbRepository.getDiary(date)
    }
}