package com.cocot3ro.mipastillero.ui.screens.calendar.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.cocot3ro.mipastillero.core.DateTimeUtils.formatShortTime
import com.cocot3ro.mipastillero.domain.model.MedicamentoActivoItem
import java.util.Date

@Composable
fun CalendarMedGroup(
    modifier: Modifier,
    date: Date,
    hour: Date,
    meds: List<MedicamentoActivoItem>,
    onMarcarToma: suspend (MedicamentoActivoItem, Date, Date, Boolean) -> Unit
) {
    Card(modifier = modifier) {
        Text(
            text = hour.formatShortTime(),
            fontSize = 28.sp
        )

        meds.filter { it.horario.contains(hour) }.forEach { med ->
            CalendarMedItem(
                modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                med = med,
                date = date,
                hour = hour,
                onMarcarToma = onMarcarToma
            )
        }
    }

}
