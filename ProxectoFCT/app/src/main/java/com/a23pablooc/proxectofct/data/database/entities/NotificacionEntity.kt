package com.a23pablooc.proxectofct.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoTable
import com.a23pablooc.proxectofct.data.database.definitions.NotificacionTable
import com.a23pablooc.proxectofct.data.database.definitions.UsuarioTable
import java.util.Date

@Entity(
    tableName = NotificacionTable.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = MedicamentoEntity::class,
            parentColumns = [MedicamentoTable.Columns.PK_COD_NACIONAL],
            childColumns = [NotificacionTable.Columns.FK_MEDICAMENTO],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UsuarioEntity::class,
            parentColumns = [UsuarioTable.Columns.PK_USUARIO],
            childColumns = [NotificacionTable.Columns.FK_USUARIO],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(
            name = NotificacionTable.Indexes.IDX_NOTIFICACION_FK_MEDICAMENTO,
            unique = false,
            value = [NotificacionTable.Columns.FK_MEDICAMENTO]
        ),
        Index(
            name = NotificacionTable.Indexes.IDX_NOTIFICACION_FK_USUARIO,
            unique = false,
            value = [NotificacionTable.Columns.FK_USUARIO]
        )
    ]
)
data class NotificacionEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = NotificacionTable.Columns.PK_NOTIFICACION)
    val pkNotificacion: Int = 0,

    @ColumnInfo(name = NotificacionTable.Columns.FK_MEDICAMENTO)
    val fkMedicamento: Int,

    @ColumnInfo(name = NotificacionTable.Columns.FK_USUARIO)
    val fkUsuario: Int,

    @ColumnInfo(name = NotificacionTable.Columns.FECHA)
    val fecha: Date,

    @ColumnInfo(name = NotificacionTable.Columns.HORA)
    val hora: Date
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NotificacionEntity

        return ((pkNotificacion != other.pkNotificacion)
                || (fkMedicamento != other.fkMedicamento)
                || (fkUsuario != other.fkUsuario)
                || (fecha != other.fecha)
                || (hora != other.hora))
    }

    override fun hashCode(): Int {
        var result = pkNotificacion
        result = 31 * result + fkMedicamento
        result = 31 * result + fkUsuario
        result = 31 * result + fecha.hashCode()
        result = 31 * result + hora.hashCode()
        return result
    }
}