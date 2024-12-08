package com.cocot3ro.mipastillero.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.cocot3ro.mipastillero.data.database.DataBaseClearer
import com.cocot3ro.mipastillero.data.database.DatabaseClearerImpl
import com.cocot3ro.mipastillero.data.database.MiPastilleroDatabase
import com.cocot3ro.mipastillero.data.database.definitions.DatabaseDefinition
import com.cocot3ro.mipastillero.data.database.definitions.MedicamentoTableDefinition
import com.cocot3ro.mipastillero.data.database.security.DatabasePassphrase
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

    @Singleton
    @Provides
    fun provideRoom(
        @ApplicationContext context: Context,
        supportFactory: SupportFactory
    ): MiPastilleroDatabase {
        return Room.databaseBuilder(
            context,
            MiPastilleroDatabase::class.java,
            DatabaseDefinition.DATABASE_NAME
        ).apply {
            openHelperFactory(supportFactory)
            addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    initializeDatabase(db)
                }
            })
        }.build()
    }

    private fun initializeDatabase(db: SupportSQLiteDatabase) {
        db.beginTransaction()
        try {
            val updatedRows = db.compileStatement(
                """
                        UPDATE sqlite_sequence SET seq = 999999 WHERE name = '${MedicamentoTableDefinition.TABLE_NAME}'
                    """
            ).executeUpdateDelete()

            if (updatedRows == 0) {
                db.execSQL(
                    """
                            INSERT INTO sqlite_sequence (name, seq) VALUES ('${MedicamentoTableDefinition.TABLE_NAME}', 999999)
                        """
                )
            }

            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    @Singleton
    @Provides
    fun provideDatabaseClearer(db: MiPastilleroDatabase): DataBaseClearer {
        return DatabaseClearerImpl(db, db.openHelper.writableDatabase)
    }

    @Singleton
    @Provides
    fun provideMedicamentoDao(db: MiPastilleroDatabase) = db.getMedicamentoDao()

    @Singleton
    @Provides
    fun provideMedicamentoActivoDao(db: MiPastilleroDatabase) = db.getMedicamentoActivoDao()

    @Singleton
    @Provides
    fun provideUsuarioDao(db: MiPastilleroDatabase) = db.getUsuarioDao()

    @Singleton
    @Provides
    fun provideAgendaDao(db: MiPastilleroDatabase) = db.getAgendaDao()

    @Singleton
    @Provides
    fun provideNotificacionDao(db: MiPastilleroDatabase) = db.getNotificacionDao()

    @Singleton
    @Provides
    fun provideMedicamentoAndMedicamentoActivoDao(db: MiPastilleroDatabase) =
        db.getMedicamentoAndMedicamentoActivoDao()

    @Singleton
    @Provides
    fun provideMedicamentoActivoWithNotificacionDAO(db: MiPastilleroDatabase) =
        db.getMedicamentoActivoWithNotificacionDao()
}