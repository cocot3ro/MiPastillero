package com.a23pablooc.proxectofct.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.a23pablooc.proxectofct.data.database.definitions.HistorialTable
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoTable
import com.a23pablooc.proxectofct.data.database.definitions.UsuarioTable
import java.util.Date

@Entity(
    tableName = HistorialTable.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = UsuarioEntity::class,
            parentColumns = [UsuarioTable.Columns.ID],
            childColumns = [HistorialTable.Columns.FK_USUARIO],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MedicamentoEntity::class,
            parentColumns = [MedicamentoTable.Columns.ID],
            childColumns = [HistorialTable.Columns.FK_MEDICAMENTO],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(
            name = HistorialTable.Indexes.IDX_HISTORIAL_FK_USUARIO,
            unique = false,
            value = [HistorialTable.Columns.FK_USUARIO]
        ),
        Index(
            name = HistorialTable.Indexes.IDX_HISTORIAL_FK_MEDICAMENTO,
            unique = false,
            value = [HistorialTable.Columns.FK_MEDICAMENTO]
        )
    ]
)
data class HistorialEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = HistorialTable.Columns.ID)
    val id: Int = 0,

    @ColumnInfo(name = HistorialTable.Columns.FK_USUARIO)
    val fkUsuario: Int,

    @ColumnInfo(name = HistorialTable.Columns.FK_MEDICAMENTO)
    val fkMedicamento: Int,

    @ColumnInfo(name = HistorialTable.Columns.FECHA_INICIO)
    val fechaInicio: Date,

    @ColumnInfo(name = HistorialTable.Columns.FECHA_FIN)
    val fechaFin: Date
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HistorialEntity

        if (id != other.id) return false
        if (fkUsuario != other.fkUsuario) return false
        if (fkMedicamento != other.fkMedicamento) return false
        if (fechaInicio != other.fechaInicio) return false
        if (fechaFin != other.fechaFin) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + fkUsuario
        result = 31 * result + fkMedicamento
        result = 31 * result + fechaInicio.hashCode()
        result = 31 * result + fechaFin.hashCode()
        return result
    }
}

//TODO: Funciones de extensión