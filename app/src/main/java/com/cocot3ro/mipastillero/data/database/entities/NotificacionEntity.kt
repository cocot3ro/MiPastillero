package com.cocot3ro.mipastillero.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.cocot3ro.mipastillero.data.database.definitions.MedicamentoActivoTableDefinition
import com.cocot3ro.mipastillero.data.database.definitions.NotificacionTableDefinition.Columns.FK_MEDICAMENTO_ACTIVO
import com.cocot3ro.mipastillero.data.database.definitions.NotificacionTableDefinition.Columns.FK_USUARIO
import com.cocot3ro.mipastillero.data.database.definitions.NotificacionTableDefinition.Columns.PK_NOTIFICACION
import com.cocot3ro.mipastillero.data.database.definitions.NotificacionTableDefinition.Columns.TIME_STAMP
import com.cocot3ro.mipastillero.data.database.definitions.NotificacionTableDefinition.Indexes.IDX_NOTIFICACION_FK_MEDICAMENTO
import com.cocot3ro.mipastillero.data.database.definitions.NotificacionTableDefinition.Indexes.IDX_NOTIFICACION_FK_USUARIO
import com.cocot3ro.mipastillero.data.database.definitions.NotificacionTableDefinition.TABLE_NAME
import com.cocot3ro.mipastillero.data.database.definitions.UsuarioTableDefinition
import java.util.Date

@Entity(
    tableName = TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = MedicamentoActivoEntity::class,
            parentColumns = [MedicamentoActivoTableDefinition.Columns.PK_MEDICAMENTO_ACTIVO],
            childColumns = [FK_MEDICAMENTO_ACTIVO],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UsuarioEntity::class,
            parentColumns = [UsuarioTableDefinition.Columns.PK_USUARIO],
            childColumns = [FK_USUARIO],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(
            name = IDX_NOTIFICACION_FK_MEDICAMENTO,
            unique = false,
            value = [FK_MEDICAMENTO_ACTIVO]
        ),
        Index(
            name = IDX_NOTIFICACION_FK_USUARIO,
            unique = false,
            value = [FK_USUARIO]
        )
    ]
)
data class NotificacionEntity(
    @PrimaryKey
    @ColumnInfo(name = PK_NOTIFICACION)
    val pkNotificacion: Int,

    @ColumnInfo(name = FK_MEDICAMENTO_ACTIVO)
    val fkMedicamentoActivo: Long,

    @ColumnInfo(name = FK_USUARIO)
    val fkUsuario: Long,

    @ColumnInfo(name = TIME_STAMP)
    val timeStamp: Date
)