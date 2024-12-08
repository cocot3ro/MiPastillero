package com.cocot3ro.mipastillero

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltAndroidApp
class MiPastilleroApp : Application() {
    @Inject
    @ApplicationContext
    lateinit var appContext: Context
}