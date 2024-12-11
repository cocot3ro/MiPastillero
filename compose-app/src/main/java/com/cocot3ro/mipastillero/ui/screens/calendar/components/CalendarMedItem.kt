package com.cocot3ro.mipastillero.ui.screens.calendar.components

import android.icu.util.Calendar
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.cocot3ro.mipastillero.core.DateTimeUtils.get
import com.cocot3ro.mipastillero.domain.model.MedicamentoActivoItem
import kotlinx.coroutines.launch
import java.util.Date

@Composable
fun CalendarMedItem(
    modifier: Modifier,
    med: MedicamentoActivoItem,
    date: Date,
    hour: Date,
    onMarcarToma: suspend (MedicamentoActivoItem, Date, Date, Boolean) -> Unit
) {
    Card(modifier = modifier) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (med.fkMedicamento.imagen != Uri.EMPTY) {
                AsyncImage(
                    modifier = Modifier.size(80.dp),
                    model = med.fkMedicamento.imagen,
                    contentDescription = null, // TODO: Content description
                    contentScale = ContentScale.Crop
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    modifier = Modifier.padding(top = 2.dp, start = 2.dp),
                    text = med.fkMedicamento.nombre,
                    fontSize = 16.sp
                )

                Text(
                    modifier = Modifier.padding(start = 2.dp, bottom = 2.dp, end = 8.dp),
                    text = med.dosis,
                    fontSize = 16.sp
                )
            }

            val timeStamp = Calendar.getInstance().apply {
                set(Calendar.YEAR, date.get(Calendar.YEAR))
                set(Calendar.MONTH, date.get(Calendar.MONTH))
                set(Calendar.DAY_OF_MONTH, date.get(Calendar.DAY_OF_MONTH))
                set(Calendar.HOUR_OF_DAY, hour.get(Calendar.HOUR_OF_DAY))
                set(Calendar.MINUTE, hour.get(Calendar.MINUTE))
                set(Calendar.SECOND, hour.get(Calendar.SECOND))
                set(Calendar.MILLISECOND, hour.get(Calendar.MILLISECOND))
            }.time

            var checked by remember { mutableStateOf(med.tomas[timeStamp] ?: false) }
            val scope = rememberCoroutineScope()

            Checkbox(
                checked = checked,
                onCheckedChange = { isChecked ->
                    scope.launch {
                        try {
                            onMarcarToma.invoke(med, date, hour, isChecked)
                            checked = isChecked
                        } catch (_: Exception) {
                            checked = !isChecked
                        }
                    }
                }
            )
        }

    }
}
