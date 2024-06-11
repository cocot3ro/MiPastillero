package com.a23pablooc.proxectofct.di

import com.a23pablooc.proxectofct.data.model.typeadapters.MedicamentoModelTypeAdapter
import com.a23pablooc.proxectofct.data.model.typeadapters.UriTypeAdapter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GsonModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(
                MedicamentoModelTypeAdapter::class.java,
                MedicamentoModelTypeAdapter()
            )
            .registerTypeAdapter(UriTypeAdapter::class.java, UriTypeAdapter())
            .create()
    }
}