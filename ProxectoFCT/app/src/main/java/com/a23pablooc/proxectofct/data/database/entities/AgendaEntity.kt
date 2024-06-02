package com.a23pablooc.proxectofct.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.a23pablooc.proxectofct.data.database.definitions.AgendaTableDefinition
import com.a23pablooc.proxectofct.data.database.definitions.UsuarioTableDefinition
import java.util.Date

@Entity(
    tableName = AgendaTableDefinition.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = UsuarioEntity::class,
            parentColumns = [UsuarioTableDefinition.Columns.PK_USUARIO],
            childColumns = [AgendaTableDefinition.Columns.FK_USUARIO],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(
            name = AgendaTableDefinition.Indexes.IDX_AGENDA_FK_USUARIO,
            value = [AgendaTableDefinition.Columns.FK_USUARIO],
            unique = false
        ),
    ]
)
data class AgendaEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = AgendaTableDefinition.Columns.PK_AGENDA)
    val pkAgenda: Long = 0,

    @ColumnInfo(name = AgendaTableDefinition.Columns.FK_USUARIO)
    val fkUsuario: Long,

    @ColumnInfo(name = AgendaTableDefinition.Columns.HORA)
    val descripcion: String,

    @ColumnInfo(name = AgendaTableDefinition.Columns.FECHA)
    val fecha: Date
)