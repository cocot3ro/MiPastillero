package com.a23pablooc.proxectofct.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.a23pablooc.proxectofct.data.database.definitions.AgendaTable
import com.a23pablooc.proxectofct.data.database.definitions.UsuarioTable
import java.util.Date

@Entity(
    tableName = AgendaTable.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = UsuarioEntity::class,
            parentColumns = [UsuarioTable.Columns.PK_USUARIO],
            childColumns = [AgendaTable.Columns.FK_USUARIO],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(
            name = AgendaTable.Indexes.IDX_AGENDA_FK_USUARIO,
            value = [AgendaTable.Columns.FK_USUARIO],
            unique = false
        ),
    ]
)
data class AgendaEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = AgendaTable.Columns.PK_AGENDA)
    val pkAgenda: Int = 0,

    @ColumnInfo(name = AgendaTable.Columns.FK_USUARIO)
    val fkUsuario: Int,

    @ColumnInfo(name = AgendaTable.Columns.HORA)
    val descripcion: String,

    @ColumnInfo(name = AgendaTable.Columns.FECHA)
    val fecha: Date
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AgendaEntity

        return ((pkAgenda != other.pkAgenda)
                || (fkUsuario != other.fkUsuario)
                || (fecha != other.fecha)
                || (descripcion != other.descripcion))
    }

    override fun hashCode(): Int {
        var result = pkAgenda
        result = 31 * result + fkUsuario
        result = 31 * result + fecha.hashCode()
        result = 31 * result + descripcion.hashCode()
        return result
    }
}