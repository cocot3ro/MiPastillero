package com.cocot3ro.mipastillero.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cocot3ro.mipastillero.data.database.daos.UserDao
import com.cocot3ro.mipastillero.data.database.model.UserEntity

@Database(
    entities = [
        UserEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class MiPastilleroDatabase : RoomDatabase() {

    abstract fun getUserDao(): UserDao

}