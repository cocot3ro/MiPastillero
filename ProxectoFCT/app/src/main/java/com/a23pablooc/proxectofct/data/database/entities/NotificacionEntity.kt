package com.a23pablooc.proxectofct.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoTableDefinition
import com.a23pablooc.proxectofct.data.database.definitions.NotificacionTableDefinition
import com.a23pablooc.proxectofct.data.database.definitions.UsuarioTableDefinition
import java.util.Date

@Entity(
    tableName = NotificacionTableDefinition.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = MedicamentoEntity::class,
            parentColumns = [MedicamentoTableDefinition.Columns.PK_COD_NACIONAL_MEDICAMENTO],
            childColumns = [NotificacionTableDefinition.Columns.FK_MEDICAMENTO_ACTIVO],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UsuarioEntity::class,
            parentColumns = [UsuarioTableDefinition.Columns.PK_USUARIO],
            childColumns = [NotificacionTableDefinition.Columns.FK_USUARIO],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(
            name = NotificacionTableDefinition.Indexes.IDX_NOTIFICACION_FK_MEDICAMENTO,
            unique = false,
            value = [NotificacionTableDefinition.Columns.FK_MEDICAMENTO_ACTIVO]
        ),
        Index(
            name = NotificacionTableDefinition.Indexes.IDX_NOTIFICACION_FK_USUARIO,
            unique = false,
            value = [NotificacionTableDefinition.Columns.FK_USUARIO]
        )
    ]
)
data class NotificacionEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = NotificacionTableDefinition.Columns.PK_NOTIFICACION)
    val pkNotificacion: Long = 0,

    @ColumnInfo(name = NotificacionTableDefinition.Columns.FK_MEDICAMENTO_ACTIVO)
    val fkMedicamentoActivo: Long,

    @ColumnInfo(name = NotificacionTableDefinition.Columns.FK_USUARIO)
    val fkUsuario: Long,

    @ColumnInfo(name = NotificacionTableDefinition.Columns.FECHA)
    val fecha: Date,

    @ColumnInfo(name = NotificacionTableDefinition.Columns.HORA)
    val hora: Date
)