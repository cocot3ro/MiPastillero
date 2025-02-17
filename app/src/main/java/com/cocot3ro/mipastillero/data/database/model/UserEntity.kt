package com.cocot3ro.mipastillero.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cocot3ro.mipastillero.data.database.definitions.UserTable

@Entity(tableName = UserTable.TABLE_NAME)
data class UserEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = UserTable.Columns.USER_ID)
    val userId: Long,

    @ColumnInfo(name = UserTable.Columns.NAME)
    val name: String
)
