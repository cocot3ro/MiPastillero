package com.a23pablooc.proxectofct.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.a23pablooc.proxectofct.data.database.table_definitions.MedicamentoActivoTable
import com.a23pablooc.proxectofct.data.database.table_definitions.MedicamentoTable
import com.a23pablooc.proxectofct.data.database.table_definitions.UsuarioTable
import java.util.Date

@Entity(
    tableName = MedicamentoActivoTable.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = MedicamentoEntity::class,
            parentColumns = [MedicamentoTable.Columns.ID],
            childColumns = [MedicamentoActivoTable.Columns.FK_MEDICAMENTO],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UsuarioEntity::class,
            parentColumns = [UsuarioTable.Columns.ID],
            childColumns = [MedicamentoActivoTable.Columns.FK_USUARIO],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MedicamentoActivoEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = MedicamentoActivoTable.Columns.ID)
    val id: Int = 0,

    @ColumnInfo(name = MedicamentoActivoTable.Columns.FK_MEDICAMENTO)
    val idMedicamento: Int,

    @ColumnInfo(name = MedicamentoActivoTable.Columns.FK_USUARIO)
    val idUsuario: Int,

    @ColumnInfo(name = MedicamentoActivoTable.Columns.FECHA_INICIO)
    val fechaInicio: Date,

    @ColumnInfo(name = MedicamentoActivoTable.Columns.FECHA_FIN)
    val fechaFin: Date,

    @ColumnInfo(name = MedicamentoActivoTable.Columns.HORARIO)
    val horario: Set<Date>,

    @ColumnInfo(name = MedicamentoActivoTable.Columns.DOSIS)
    val dosis: String
)

//TODO: funciones de extensión