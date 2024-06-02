package com.a23pablooc.proxectofct.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.a23pablooc.proxectofct.data.database.definitions.UsuarioTableDefinition

@Entity(tableName = UsuarioTableDefinition.TABLE_NAME)
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = UsuarioTableDefinition.Columns.PK_USUARIO)
    val pkUsuario: Long = 0,

    @ColumnInfo(name = UsuarioTableDefinition.Columns.NOMBRE)
    val nombre: String
)