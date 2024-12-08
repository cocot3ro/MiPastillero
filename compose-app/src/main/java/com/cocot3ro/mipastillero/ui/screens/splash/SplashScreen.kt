package com.cocot3ro.mipastillero.ui.screens.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SplashScreen(
    modifier: Modifier,
    onLoginRequired: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    Scaffold(modifier = modifier) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Text(text = "Splash Screen")
        }
    }
}
