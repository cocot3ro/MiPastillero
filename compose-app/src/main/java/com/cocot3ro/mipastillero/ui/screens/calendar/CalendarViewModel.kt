package com.cocot3ro.mipastillero.ui.screens.calendar

import android.icu.util.Calendar
import androidx.lifecycle.ViewModel
import com.cocot3ro.mipastillero.core.DateTimeUtils
import com.cocot3ro.mipastillero.domain.model.MedicamentoActivoItem
import com.cocot3ro.mipastillero.domain.usecases.GetMedicamentosCalendarioUseCase
import com.cocot3ro.mipastillero.domain.usecases.MarcarTomaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val getMedicamentosCalendarioUseCase: GetMedicamentosCalendarioUseCase,
    private val marcarTomaUseCase: MarcarTomaUseCase
) : ViewModel() {

    fun medicamentosFlow(date: Date): Flow<List<MedicamentoActivoItem>> =
        getMedicamentosCalendarioUseCase.invoke(date)

    suspend fun marcarToma(med: MedicamentoActivoItem, dia: Date, hora: Date, tomado: Boolean) {
        marcarTomaUseCase.invoke(med, dia, hora, tomado)
    }

    fun calculateOffset(time: Long): Int {
        val date = Calendar.getInstance().apply {
            timeInMillis = time
        }.time

        val today = DateTimeUtils.now

        return DateTimeUtils.daysBetweenDates(today, date)
    }

    fun calculateDate(offset: Int): Date {
        return Calendar.getInstance().apply {
            time = DateTimeUtils.now

            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            add(Calendar.DAY_OF_YEAR, offset)
        }.time
    }
}
