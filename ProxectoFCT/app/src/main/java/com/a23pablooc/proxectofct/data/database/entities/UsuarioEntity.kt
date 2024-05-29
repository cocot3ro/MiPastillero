package com.a23pablooc.proxectofct.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.a23pablooc.proxectofct.data.database.definitions.UsuarioTable

@Entity(tableName = UsuarioTable.TABLE_NAME)
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = UsuarioTable.Columns.PK_USUARIO)
    val pkUsuario: Int = 0,

    @ColumnInfo(name = UsuarioTable.Columns.NOMBRE)
    val nombre: String
)