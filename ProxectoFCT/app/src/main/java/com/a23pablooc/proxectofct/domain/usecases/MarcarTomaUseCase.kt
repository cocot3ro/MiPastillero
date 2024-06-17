package com.a23pablooc.proxectofct.domain.usecases

import com.a23pablooc.proxectofct.core.DateTimeUtils.isToday
import com.a23pablooc.proxectofct.data.repositories.PillboxDbRepository
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem
import java.util.Date
import javax.inject.Inject

class MarcarTomaUseCase @Inject constructor(private val repository: PillboxDbRepository) {

    suspend fun invoke(med: MedicamentoActivoItem, timeStamp: Date, value: Boolean? = null) {
        if (timeStamp.isToday().not())
            throw IllegalArgumentException("Not today son!")

        med.tomas[timeStamp] = value ?: med.tomas[timeStamp]?.not() ?: true

        repository.updateMed(med)
    }
}