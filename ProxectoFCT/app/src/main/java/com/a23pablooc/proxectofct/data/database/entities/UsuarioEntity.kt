package com.a23pablooc.proxectofct.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.a23pablooc.proxectofct.data.database.definitions.UsuarioTable

@Entity(tableName = UsuarioTable.TABLE_NAME)
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = UsuarioTable.Columns.ID)
    val pkUsuario: Int = 0,

    @ColumnInfo(name = UsuarioTable.Columns.NOMBRE)
    val nombre: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UsuarioEntity

        if (pkUsuario != other.pkUsuario) return false
        if (nombre != other.nombre) return false

        return true
    }

    override fun hashCode(): Int {
        var result = pkUsuario
        result = 31 * result + nombre.hashCode()
        return result
    }
}

//TODO: Funciones de extensión