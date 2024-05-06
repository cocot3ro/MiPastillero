package com.a23pablooc.proxectofct.data.database.entities

import androidx.room.Entity
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoTable
import com.a23pablooc.proxectofct.data.database.definitions.NotificacionTable
import com.a23pablooc.proxectofct.data.database.definitions.UsuarioTable
import java.util.Date

@Entity(
    tableName = NotificacionTable.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = MedicamentoEntity::class,
            parentColumns = [MedicamentoTable.Columns.ID],
            childColumns = [NotificacionTable.Columns.FK_MEDICAMENTO],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UsuarioEntity::class,
            parentColumns = [UsuarioTable.Columns.ID],
            childColumns = [NotificacionTable.Columns.FK_USUARIO],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(
            name = "idx_notificacion_fk_medicamento",
            unique = false,
            value = [NotificacionTable.Columns.FK_MEDICAMENTO]
        ),
        Index(
            name = "idx_notificacion_fk_usuario",
            unique = false,
            value = [NotificacionTable.Columns.FK_USUARIO]
        )
    ]
)
data class NotificacionEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = NotificacionTable.Columns.ID)
    val id: Int = 0,

    @ColumnInfo(name = NotificacionTable.Columns.FK_MEDICAMENTO)
    val idMedicamento: Int,

    @ColumnInfo(name = NotificacionTable.Columns.FK_USUARIO)
    val idUsuario: Int,

    @ColumnInfo(name = NotificacionTable.Columns.FECHA)
    val fecha: Date,

    @ColumnInfo(name = NotificacionTable.Columns.HORA)
    val hora: Date
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NotificacionEntity

        if (id != other.id) return false
        if (idMedicamento != other.idMedicamento) return false
        if (idUsuario != other.idUsuario) return false
        if (fecha != other.fecha) return false
        if (hora != other.hora) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + idMedicamento
        result = 31 * result + idUsuario
        result = 31 * result + fecha.hashCode()
        result = 31 * result + hora.hashCode()
        return result
    }
}

//TODO: Funciones de extensión