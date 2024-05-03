package com.a23pablooc.proxectofct.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.a23pablooc.proxectofct.data.database.tabledefinitions.MedicamentoCalendarioTable
import com.a23pablooc.proxectofct.data.database.tabledefinitions.MedicamentoTable
import com.a23pablooc.proxectofct.data.database.tabledefinitions.UsuarioTable
import java.util.Date

@Entity(
    tableName = MedicamentoCalendarioTable.TABLE_NAME,
    primaryKeys = [
        MedicamentoCalendarioTable.Columns.ID_MEDICAMENTO,
        MedicamentoCalendarioTable.Columns.FECHA,
        MedicamentoCalendarioTable.Columns.HORA
    ],
    foreignKeys = [
        ForeignKey(
            entity = MedicamentoEntity::class,
            parentColumns = [MedicamentoTable.Columns.ID],
            childColumns = [MedicamentoCalendarioTable.Columns.ID_MEDICAMENTO],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UsuarioEntity::class,
            parentColumns = [UsuarioTable.Columns.ID],
            childColumns = [MedicamentoCalendarioTable.Columns.ID_USUARIO],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MedicamentoCalendarioEntity(
    @ColumnInfo(name = MedicamentoCalendarioTable.Columns.ID)
    val id: Int = 0,

    @ColumnInfo(name = MedicamentoCalendarioTable.Columns.ID_MEDICAMENTO)
    val idMedicamento: Int,

    @ColumnInfo(name = MedicamentoCalendarioTable.Columns.ID_USUARIO)
    val idUsuario: Int,

    @ColumnInfo(name = MedicamentoCalendarioTable.Columns.FECHA)
    val fecha: Date,

    @ColumnInfo(name = MedicamentoCalendarioTable.Columns.HORA)
    val hora: Date,

    @ColumnInfo(name = MedicamentoCalendarioTable.Columns.TOMADO)
    val seHaTomado: Boolean
)

//TODO: funciones de extensión