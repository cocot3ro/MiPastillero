package com.cocot3ro.mipastillero.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.cocot3ro.mipastillero.ui.navigation.NavigationWrapper
import com.cocot3ro.mipastillero.ui.theme.MiPastilleroTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MiPastilleroTheme {
                NavigationWrapper()
            }
        }
    }
}