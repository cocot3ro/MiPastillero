package com.a23pablooc.proxectofct.di

import android.content.Context
import androidx.room.Room
import com.a23pablooc.proxectofct.data.database.PillboxDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    private const val DATABASE_NAME = "pillbox_database"

    @Singleton
    @Provides
    fun provideRoom(@ApplicationContext context: Context): PillboxDatabase {
        return Room.databaseBuilder(
            context,
            PillboxDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    @Singleton
    @Provides
    fun provideMedicamentoDao(database: PillboxDatabase) = database.getMedicamentoDao()

    //TODO: Añadir aquí los DAOs que se vayan creando
}