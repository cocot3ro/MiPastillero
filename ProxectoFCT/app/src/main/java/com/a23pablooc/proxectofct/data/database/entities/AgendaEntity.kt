package com.a23pablooc.proxectofct.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.a23pablooc.proxectofct.data.database.definitions.AgendaTable
import java.util.Date

@Entity(
    tableName = AgendaTable.TABLE_NAME,
    indices = [
        Index(
            name = "idx_agenda_fk_usuario",
            value = [AgendaTable.Columns.FK_USUARIO],
            unique = false
        ),
    ]
)
data class AgendaEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = AgendaTable.Columns.ID)
    val id: Int = 0,

    @ColumnInfo(name = AgendaTable.Columns.FK_USUARIO)
    val idUsuario: Int,

    @ColumnInfo(name = AgendaTable.Columns.FECHA)
    val fecha: Date,

    @ColumnInfo(name = AgendaTable.Columns.HORA)
    val descripcion: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AgendaEntity

        if (id != other.id) return false
        if (idUsuario != other.idUsuario) return false
        if (fecha != other.fecha) return false
        if (descripcion != other.descripcion) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + idUsuario
        result = 31 * result + fecha.hashCode()
        result = 31 * result + descripcion.hashCode()
        return result
    }
}
//TODO: Funciones de extensión