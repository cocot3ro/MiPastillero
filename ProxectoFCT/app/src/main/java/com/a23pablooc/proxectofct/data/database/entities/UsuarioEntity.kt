package com.a23pablooc.proxectofct.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.a23pablooc.proxectofct.data.database.definitions.UsuarioTableDefinition
import com.a23pablooc.proxectofct.data.database.definitions.UsuarioTableDefinition.Columns.NOMBRE
import com.a23pablooc.proxectofct.data.database.definitions.UsuarioTableDefinition.Columns.PK_USUARIO

@Entity(tableName = UsuarioTableDefinition.TABLE_NAME)
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = PK_USUARIO)
    val pkUsuario: Long = 0,

    @ColumnInfo(name = NOMBRE)
    val nombre: String
)