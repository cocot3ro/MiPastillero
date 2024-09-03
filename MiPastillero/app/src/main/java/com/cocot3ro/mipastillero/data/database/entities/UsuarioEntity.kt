package com.cocot3ro.mipastillero.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cocot3ro.mipastillero.data.database.definitions.UsuarioTableDefinition
import com.cocot3ro.mipastillero.data.database.definitions.UsuarioTableDefinition.Columns.NOMBRE
import com.cocot3ro.mipastillero.data.database.definitions.UsuarioTableDefinition.Columns.PK_USUARIO

@Entity(tableName = UsuarioTableDefinition.TABLE_NAME)
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = PK_USUARIO)
    val pkUsuario: Long = 0,

    @ColumnInfo(name = NOMBRE)
    val nombre: String
)