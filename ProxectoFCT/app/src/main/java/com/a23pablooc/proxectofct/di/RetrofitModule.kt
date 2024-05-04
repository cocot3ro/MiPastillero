package com.a23pablooc.proxectofct.di

import com.a23pablooc.proxectofct.data.model.MedicamentoModel
import com.a23pablooc.proxectofct.data.model.typeadapters.MedicamentoModelTypeAdapter
import com.a23pablooc.proxectofct.data.network.CimaApiClient
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        val gson = GsonBuilder()
            .registerTypeAdapter(MedicamentoModel.Builder::class.java, MedicamentoModelTypeAdapter())
            .create()

        return Retrofit.Builder()
            .baseUrl("https://cima.aemps.es/cima/rest/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Singleton
    @Provides
    fun provideMedicamentoService(retrofit: Retrofit): CimaApiClient {
        return retrofit.create(CimaApiClient::class.java)
    }
}