package com.cocot3ro.mipastillero.di

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.cocot3ro.mipastillero.data.database.MiPastilleroDatabase
import com.cocot3ro.mipastillero.data.database.daos.UserDao
import com.cocot3ro.mipastillero.data.database.definitions.Database
import com.cocot3ro.mipastillero.data.database.repository.DatabaseRepository
import com.cocot3ro.mipastillero.data.database.security.DatabasePassphrase
import com.cocot3ro.mipastillero.data.database.utils.DataBaseClearer
import com.cocot3ro.mipastillero.data.database.utils.DatabaseInitializer
import net.sqlcipher.database.SupportFactory
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val roomModule = module {
    includes(
        daoModule,
        databaseModule,
        repositoryModule
    )
}

private val databaseModule = module {

    single<MiPastilleroDatabase> {
        Room.databaseBuilder(
            androidContext(),
            MiPastilleroDatabase::class.java,
            Database.DATABASE_NAME
        )
            .openHelperFactory(SupportFactory(DatabasePassphrase.getPassphrase(androidContext())))
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    DatabaseInitializer.initialize(db)
                }
            })
            .build()
    }

    factory<DataBaseClearer> {
        get<MiPastilleroDatabase>().let { db ->
            DataBaseClearer(
                miPastilleroDatabase = db,
                writableDatabase = db.openHelper.writableDatabase
            )
        }
    }

}

private val daoModule = module {

    single<UserDao> {
        get<MiPastilleroDatabase>().getUserDao()
    }

}

private val repositoryModule = module {
    singleOf(::DatabaseRepository)
}
