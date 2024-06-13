package com.a23pablooc.proxectofct.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.a23pablooc.proxectofct.data.database.PillboxDatabase
import com.a23pablooc.proxectofct.data.database.dao.AgendaDAO
import com.a23pablooc.proxectofct.data.database.dao.MedicamentoActivoDAO
import com.a23pablooc.proxectofct.data.database.dao.MedicamentoDAO
import com.a23pablooc.proxectofct.data.database.dao.NotificacionDAO
import com.a23pablooc.proxectofct.data.database.dao.UsuarioDAO
import com.a23pablooc.proxectofct.data.database.definitions.DatabaseDefinition
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoTableDefinition
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
    @Suppress("UNUSED_PARAMETER")
    fun provideRoom(
        @ApplicationContext context: Context,
        supportFactory: SupportFactory
    ): PillboxDatabase {
        return Room.databaseBuilder(context, PillboxDatabase::class.java, DatabaseDefinition.DATABASE_NAME)
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    db.execSQL("INSERT INTO sqlite_sequence VALUES ('${MedicamentoTableDefinition.TABLE_NAME}', 999999);")
                }
            })
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
    fun provideUsuarioDao(database: PillboxDatabase): UsuarioDAO = database.getUsuarioDao()

    @Singleton
    @Provides
    fun provideAgendaDao(database: PillboxDatabase): AgendaDAO = database.getAgendaDao()

    @Singleton
    @Provides
    fun provideNotificacionDao(database: PillboxDatabase): NotificacionDAO =
        database.getNotificacionDao()

    @Singleton
    @Provides
    fun provideMedicamentoAndMedicamentoActivoDao(database: PillboxDatabase) =
        database.getMedicamentoAndMedicamentoActivoDao()

    @Singleton
    @Provides
    fun provideMedicamentoActivoWithNotificacionDAO(database: PillboxDatabase) =
        database.getMedicamentoActivoWithNotificacionDAO()
}