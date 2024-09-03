package com.cocot3ro.mipastillero.di

import com.cocot3ro.mipastillero.data.network.CimaApiClient
import com.cocot3ro.mipastillero.data.network.CimaApiDefinitions
import com.google.gson.Gson
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
    fun provideRetrofit(gson: Gson): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(CimaApiDefinitions.BASE_URL)
            .build()
    }

    @Singleton
    @Provides
    fun provideCimaApiClient(retrofit: Retrofit): CimaApiClient {
        return retrofit.create(CimaApiClient::class.java)
    }
}