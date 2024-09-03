package com.cocot3ro.mipastillero.domain.usecases

import android.icu.util.Calendar
import com.cocot3ro.mipastillero.core.DateTimeUtils.get
import com.cocot3ro.mipastillero.core.DateTimeUtils.isToday
import com.cocot3ro.mipastillero.data.repositories.PillboxDbRepository
import com.cocot3ro.mipastillero.domain.model.MedicamentoActivoItem
import java.util.Date
import javax.inject.Inject

class MarcarTomaUseCase @Inject constructor(private val repository: PillboxDbRepository) {

    suspend fun invoke(med: MedicamentoActivoItem, dia: Date, hora: Date) {
        val timeStamp = Calendar.getInstance().apply {
            set(Calendar.YEAR, dia.get(Calendar.YEAR))
            set(Calendar.MONTH, dia.get(Calendar.MONTH))
            set(Calendar.DAY_OF_MONTH, dia.get(Calendar.DAY_OF_MONTH))
            set(Calendar.HOUR_OF_DAY, hora.get(Calendar.HOUR_OF_DAY))
            set(Calendar.MINUTE, hora.get(Calendar.MINUTE))
            set(Calendar.SECOND, hora.get(Calendar.SECOND))
            set(Calendar.MILLISECOND, hora.get(Calendar.MILLISECOND))
        }.time

        invoke(med, timeStamp)
    }

    suspend fun invoke(med: MedicamentoActivoItem, timeStamp: Date, value: Boolean? = null) {
        if (timeStamp.isToday().not())
            throw IllegalArgumentException("Not today son!")

        med.tomas[timeStamp] = value ?: med.tomas[timeStamp]?.not() ?: true

        repository.updateMedicamentoActivo(med)
    }
}