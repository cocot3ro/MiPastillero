package com.a23pablooc.proxectofct.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoCalendarioTable
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoTable
import com.a23pablooc.proxectofct.data.database.definitions.UsuarioTable
import java.util.Date

@Entity(
    tableName = MedicamentoCalendarioTable.TABLE_NAME,
    primaryKeys = [
        MedicamentoCalendarioTable.Columns.FK_MEDICAMENTO,
        MedicamentoCalendarioTable.Columns.FECHA,
        MedicamentoCalendarioTable.Columns.HORA
    ],
    foreignKeys = [
        ForeignKey(
            entity = MedicamentoEntity::class,
            parentColumns = [MedicamentoTable.Columns.PK_COD_NACIONAL],
            childColumns = [MedicamentoCalendarioTable.Columns.FK_MEDICAMENTO],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UsuarioEntity::class,
            parentColumns = [UsuarioTable.Columns.PK_USUARIO],
            childColumns = [MedicamentoCalendarioTable.Columns.FK_USUARIO],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(
            name = MedicamentoCalendarioTable.Indexes.IDX_MEDICAMENTO_CALENDARIO_FK_MEDICAMENTO,
            unique = false,
            value = [MedicamentoCalendarioTable.Columns.FK_MEDICAMENTO]
        ),
        Index(
            name = MedicamentoCalendarioTable.Indexes.IDX_MEDICAMENTO_CALENDARIO_FK_USUARIO,
            unique = false,
            value = [MedicamentoCalendarioTable.Columns.FK_USUARIO]
        ),
    ]
)
data class MedicamentoCalendarioEntity(
    @ColumnInfo(name = MedicamentoCalendarioTable.Columns.PK_MEDICAMENTO_CALENDARIO)
    val pkMedicamentoCalendario: Int = 0,

    @ColumnInfo(name = MedicamentoCalendarioTable.Columns.FK_MEDICAMENTO)
    val fkMedicamento: Int,

    @ColumnInfo(name = MedicamentoCalendarioTable.Columns.FK_USUARIO)
    val fkUsuario: Int,

    @ColumnInfo(name = MedicamentoCalendarioTable.Columns.FECHA)
    val fecha: Date,

    @ColumnInfo(name = MedicamentoCalendarioTable.Columns.HORA)
    val hora: Date,

    @ColumnInfo(name = MedicamentoCalendarioTable.Columns.TOMADO)
    val seHaTomado: Boolean
)