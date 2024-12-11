package com.cocot3ro.mipastillero.ui.screens.calendar.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cocot3ro.mipastillero.R
import com.cocot3ro.mipastillero.core.DateTimeUtils.formatDate
import com.cocot3ro.mipastillero.core.DateTimeUtils.getDayName
import com.cocot3ro.mipastillero.domain.model.MedicamentoActivoItem
import java.util.Date

@Composable
fun CalendarPage(
    modifier: Modifier,
    date: Date,
    medList: List<MedicamentoActivoItem>,
    onMarcarToma: suspend (MedicamentoActivoItem, Date, Date, Boolean) -> Unit
) {
    Column(modifier = modifier) {
        val context = LocalContext.current
        Text(
            text = "${date.getDayName(context)} - ${date.formatDate()}",
            modifier = Modifier.fillMaxWidth(),
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
        )

        HorizontalDivider(
            modifier = Modifier.padding(all = 4.dp)
        )

        if (medList.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    text = stringResource(R.string.no_meds_at_day),
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp
                )
                Spacer(modifier = Modifier.weight(1f))
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(medList.getHorario()) { hour ->
                    val meds = medList.filter { med ->
                        med.horario.any { it.time == it.time }
                    }

                    CalendarMedGroup(
                        modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                        date = date,
                        hour = hour,
                        meds = meds,
                        onMarcarToma = onMarcarToma
                    )
                }
            }
        }
    }
}

private fun List<MedicamentoActivoItem>.getHorario(): List<Date> =
    this.map { it.horario }
        .flatten()
        .distinctBy { it.time }
        .sortedBy { it.time }
