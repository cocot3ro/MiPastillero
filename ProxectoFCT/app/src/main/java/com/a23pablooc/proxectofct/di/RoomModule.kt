package com.a23pablooc.proxectofct.di

import android.content.Context
import androidx.room.Room
import com.a23pablooc.proxectofct.data.database.PillboxDatabase
import com.a23pablooc.proxectofct.data.database.dao.AgendaDAO
import com.a23pablooc.proxectofct.data.database.dao.HistorialDAO
import com.a23pablooc.proxectofct.data.database.dao.MedicamentoActivoDAO
import com.a23pablooc.proxectofct.data.database.dao.MedicamentoCalendarioDAO
import com.a23pablooc.proxectofct.data.database.dao.MedicamentoDAO
import com.a23pablooc.proxectofct.data.database.dao.NotificacionDAO
import com.a23pablooc.proxectofct.data.database.dao.UsuarioDAO
import com.a23pablooc.proxectofct.data.database.security.DatabasePassphrase
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
    fun provideUserDatabasePassphrase(@ApplicationContext context: Context): DatabasePassphrase =
        DatabasePassphrase(context)

    @Provides
    @Singleton
    fun provideSupportFactory(databasePassphrase: DatabasePassphrase): SupportFactory =
        SupportFactory(databasePassphrase.getPassphrase())

    //TODO: restore openHelperFactory. Disabled for allowing access from the app inspector
    @Singleton
    @Provides
    fun provideRoom(
        @ApplicationContext context: Context,
        supportFactory: SupportFactory
    ): PillboxDatabase {
        return Room.databaseBuilder(context, PillboxDatabase::class.java, DATABASE_NAME)
//            .openHelperFactory(supportFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideMedicamentoDao(database: PillboxDatabase): MedicamentoDAO =
        database.getMedicamentoDao()

    @Singleton
    @Provides
    fun provideMedicamentoActivoDao(database: PillboxDatabase): MedicamentoActivoDAO =
        database.getMedicamentoActivoDao()

    @Singleton
    @Provides
    fun provideCalendarioDao(database: PillboxDatabase): MedicamentoCalendarioDAO =
        database.getMedicamentoCalendarioDao()

    @Singleton
    @Provides
    fun provideHistorialDao(database: PillboxDatabase): HistorialDAO = database.getHistorialDao()

    @Singleton
    @Provides
    fun provideUsuarioDao(database: PillboxDatabase): UsuarioDAO = database.getUsuarioDao()

    @Singleton
    @Provides
    fun provideAgendaDao(database: PillboxDatabase): AgendaDAO = database.getAgendaDao()

    @Singleton
    @Provides
    fun provideNotificacionDao(database: PillboxDatabase): NotificacionDAO =
        database.getNotificacionDao()
}