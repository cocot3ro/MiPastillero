package com.cocot3ro.mipastillero.ui.screens.calendar

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cocot3ro.mipastillero.R
import com.cocot3ro.mipastillero.ui.screens.calendar.components.CalendarPage
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    modifier: Modifier,
    viewModel: CalendarViewModel = hiltViewModel(),
    onOpenDrawer: () -> Unit
) {
    var showDatePickerDialog by rememberSaveable { mutableStateOf(false) }

    val pagerState = rememberPagerState(
        initialPage = Int.MAX_VALUE / 2,
        pageCount = { Int.MAX_VALUE }
    )

    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.calendar))
                },
                actions = {
                    IconButton(onClick = { showDatePickerDialog = true }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.search_32dp),
                            contentDescription = null // Todo: Content description
                        )
                    }

                    IconButton(onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(Int.MAX_VALUE / 2)
                        }
                    }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.today_32dp),
                            contentDescription = null // Todo: Content description
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = null // Todo: Content description
                        )
                    }
                }
            )
        },
    ) {

        HorizontalPager(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
            ,
            state = pagerState
        ) { position ->

            val offset = position - Int.MAX_VALUE / 2
            val date = viewModel.calculateDate(offset)

            val medList by viewModel.medicamentosFlow(date)
                .collectAsState(initial = emptyList())

            CalendarPage(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp)
                ,
                date = date,
                medList = medList,
                onMarcarToma = { med, dia, hora, tomado ->
                    viewModel.marcarToma(med, dia, hora, tomado)
                }
            )
        }

        if (showDatePickerDialog) {
            val datePickerState = rememberDatePickerState()

            DatePickerDialog(
                onDismissRequest = { showDatePickerDialog = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            scope.launch {
                                showDatePickerDialog = false
                                val offset =
                                    viewModel.calculateOffset(datePickerState.selectedDateMillis!!)

                                pagerState.animateScrollToPage(Int.MAX_VALUE / 2 + offset)
                            }
                        }
                    ) {
                        Text(text = stringResource(R.string.accept))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePickerDialog = false }) {
                        Text(text = stringResource(R.string.cancel))
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
    }
}
