package com.a23pablooc.proxectofct.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.a23pablooc.proxectofct.data.database.definitions.HistorialTable
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoTable
import com.a23pablooc.proxectofct.data.database.definitions.UsuarioTable

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
            name = "idx_historial_fk_usuario",
            unique = false,
            value = [HistorialTable.Columns.FK_USUARIO]
        ),
        Index(
            name = "idx_historial_fk_medicamento",
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
    val fechaInicio: String,

    @ColumnInfo(name = HistorialTable.Columns.FECHA_FIN)
    val fechaFin: String
)

//TODO: Funciones de extensión