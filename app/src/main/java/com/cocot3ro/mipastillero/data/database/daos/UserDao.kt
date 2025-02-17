package com.cocot3ro.mipastillero.data.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.cocot3ro.mipastillero.data.database.model.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM tbl_users")
    fun getAll(): Flow<List<UserEntity>>

    @Insert
    fun insert(user: UserEntity): Long

    @Update
    fun update(user: UserEntity)

    @Upsert
    fun upsert(user: UserEntity)

    @Delete
    fun delete(user: UserEntity)

}