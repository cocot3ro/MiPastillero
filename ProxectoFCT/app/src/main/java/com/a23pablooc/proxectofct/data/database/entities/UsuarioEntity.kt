package com.a23pablooc.proxectofct.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "PK_usuario")
    val pkUsuario: Int = 0,

    @ColumnInfo(name = "nombre")
    val nombre: String,
)