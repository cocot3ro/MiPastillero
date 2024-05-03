package com.a23pablooc.proxectofct.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.a23pablooc.proxectofct.data.database.table_definitions.MedicamentoFavoritoTable
import com.a23pablooc.proxectofct.data.database.table_definitions.MedicamentoTable
import com.a23pablooc.proxectofct.data.database.table_definitions.UsuarioTable

@Entity(
    tableName = MedicamentoFavoritoTable.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = MedicamentoEntity::class,
            parentColumns = [MedicamentoTable.Columns.ID],
            childColumns = [MedicamentoFavoritoTable.Columns.FK_MEDICAMENTO],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UsuarioEntity::class,
            parentColumns = [UsuarioTable.Columns.ID],
            childColumns = [MedicamentoFavoritoTable.Columns.FK_USUARIO],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MedicamentoFavoritoEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = MedicamentoFavoritoTable.Columns.ID)
    val id: Int = 0,

    @ColumnInfo(name = MedicamentoFavoritoTable.Columns.FK_MEDICAMENTO)
    val idMedicamento: Int,

    @ColumnInfo(name = MedicamentoFavoritoTable.Columns.FK_USUARIO)
    val idUsuario: Int
)

//TODO: funciones de extensión