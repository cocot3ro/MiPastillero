package com.a23pablooc.proxectofct.di

import android.content.Context
import androidx.room.Room
import com.a23pablooc.proxectofct.core.UserDatabasePassphrase
import com.a23pablooc.proxectofct.data.database.PillboxDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.sqlcipher.database.SupportFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    private const val DATABASE_NAME = "pillbox_database"

    @Provides
    @Singleton
    fun provideUserDatabasePassphrase(@ApplicationContext context: Context) =
        UserDatabasePassphrase(context)

    @Provides
    @Singleton
    fun provideSupportFactory(userDatabasePassphrase: UserDatabasePassphrase) =
        SupportFactory(userDatabasePassphrase.getPassphrase())

    @Singleton
    @Provides
    fun provideRoom(
        @ApplicationContext context: Context,
        supportFactory: SupportFactory
    ): PillboxDatabase {
        return Room.databaseBuilder(context, PillboxDatabase::class.java, DATABASE_NAME)
            .openHelperFactory(supportFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideMedicamentoDao(database: PillboxDatabase) = database.getMedicamentoDao()

    @Singleton
    @Provides
    fun provideMedicamentoActivoDao(database: PillboxDatabase) = database.getMedicamentoActivoDao()

    @Singleton
    @Provides
    fun provideMedicamentoFavoritoDao(database: PillboxDatabase) =
        database.getMedicamentoFavoritoDao()

    @Singleton
    @Provides
    fun provideCalendarioDao(database: PillboxDatabase) = database.getCalendarioDao()

    @Singleton
    @Provides
    fun provideHistorialDao(database: PillboxDatabase) = database.getHistorialDao()

    @Singleton
    @Provides
    fun provideUsuarioDao(database: PillboxDatabase) = database.getUsuarioDao()

    @Singleton
    @Provides
    fun provideAgendaDao(database: PillboxDatabase) = database.getAgendaDao()

    @Singleton
    @Provides
    fun provideNotificacionDao(database: PillboxDatabase) = database.getNotificacionDao()
}