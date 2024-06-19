package com.a23pablooc.proxectofct.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.a23pablooc.proxectofct.data.database.DataBaseClearer
import com.a23pablooc.proxectofct.data.database.DatabaseClearerImpl
import com.a23pablooc.proxectofct.data.database.PillboxDatabase
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

    @Singleton
    @Provides
    fun provideRoom(
        @ApplicationContext context: Context,
        supportFactory: SupportFactory
    ): PillboxDatabase {
        return Room.databaseBuilder(
            context,
            PillboxDatabase::class.java,
            DatabaseDefinition.DATABASE_NAME
        ).apply {
//            openHelperFactory(supportFactory)
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
    fun provideDatabaseClearer(db: PillboxDatabase): DataBaseClearer {
        return DatabaseClearerImpl(db, db.openHelper.writableDatabase)
    }

    @Singleton
    @Provides
    fun provideMedicamentoDao(db: PillboxDatabase) = db.getMedicamentoDao()

    @Singleton
    @Provides
    fun provideMedicamentoActivoDao(db: PillboxDatabase) = db.getMedicamentoActivoDao()

    @Singleton
    @Provides
    fun provideUsuarioDao(db: PillboxDatabase) = db.getUsuarioDao()

    @Singleton
    @Provides
    fun provideAgendaDao(db: PillboxDatabase) = db.getAgendaDao()

    @Singleton
    @Provides
    fun provideNotificacionDao(db: PillboxDatabase) = db.getNotificacionDao()

    @Singleton
    @Provides
    fun provideMedicamentoAndMedicamentoActivoDao(db: PillboxDatabase) =
        db.getMedicamentoAndMedicamentoActivoDao()

    @Singleton
    @Provides
    fun provideMedicamentoActivoWithNotificacionDAO(db: PillboxDatabase) =
        db.getMedicamentoActivoWithNotificacionDao()
}