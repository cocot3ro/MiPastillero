package com.cocot3ro.mipastillero.di

import android.net.Uri
import com.cocot3ro.mipastillero.data.model.MedicamentoModel
import com.cocot3ro.mipastillero.data.model.typeadapters.DateTypeAdapter
import com.cocot3ro.mipastillero.data.model.typeadapters.MedicamentoModelTypeAdapter
import com.cocot3ro.mipastillero.data.model.typeadapters.UriTypeAdapter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.Date
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GsonModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(
                MedicamentoModel::class.java,
                MedicamentoModelTypeAdapter()
            )
            .registerTypeAdapter(Uri::class.java, UriTypeAdapter())
            .registerTypeAdapter(Date::class.java, DateTypeAdapter())
            .create()
    }
}