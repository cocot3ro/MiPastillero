package com.a23pablooc.proxectofct.di

import android.net.Uri
import com.a23pablooc.proxectofct.data.model.MedicamentoModel
import com.a23pablooc.proxectofct.data.model.typeadapters.DateTypeAdapter
import com.a23pablooc.proxectofct.data.model.typeadapters.MedicamentoModelTypeAdapter
import com.a23pablooc.proxectofct.data.model.typeadapters.UriTypeAdapter
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