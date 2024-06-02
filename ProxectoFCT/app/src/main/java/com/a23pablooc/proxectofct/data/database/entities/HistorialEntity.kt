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
            parentColumns = [UsuarioTable.Columns.PK_USUARIO],
            childColumns = [HistorialTable.Columns.FK_USUARIO],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MedicamentoEntity::class,
            parentColumns = [MedicamentoTable.Columns.PK_COD_NACIONAL_MEDICAMENTO],
            childColumns = [HistorialTable.Columns.FK_MEDICAMENTO],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
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
    @ColumnInfo(name = HistorialTable.Columns.PK_HISTORIAL)
    val pkHistorial: Int = 0,

    @ColumnInfo(name = HistorialTable.Columns.FK_USUARIO)
    val fkUsuario: Int,

    @ColumnInfo(name = HistorialTable.Columns.FK_MEDICAMENTO)
    val fkMedicamento: Int,

    @ColumnInfo(name = HistorialTable.Columns.FECHA_INICIO)
    val fechaInicio: Date,

    @ColumnInfo(name = HistorialTable.Columns.FECHA_FIN)
    val fechaFin: Date
)