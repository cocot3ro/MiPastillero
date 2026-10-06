package dev.cocot3ro.mipastillero

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.compose.setSingletonImageLoaderFactory
import coil3.request.crossfade
import dev.cocot3ro.mipastillero.ui.theme.MiPastilleroTheme

@Suppress("ModifierRequired")
@Composable
fun App() {
    setSingletonImageLoaderFactory { context: PlatformContext ->
        ImageLoader.Builder(context)
            .crossfade(enable = true)
            .build()
    }

    MiPastilleroTheme {

    }
}

@Composable
@Preview
private fun AppPreview() {
    App()
}
