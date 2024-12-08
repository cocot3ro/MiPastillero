package com.cocot3ro.mipastillero.ui.screens.history

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun HistoryScreen(modifier: Modifier) {
    Scaffold(modifier = modifier) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Text(text = "History Screen")
        }
    }
}