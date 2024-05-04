package com.a23pablooc.proxectofct.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoFavoritoTable
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoTable
import com.a23pablooc.proxectofct.data.database.definitions.UsuarioTable

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
    ],
    indices = [
        Index(
            name = "idx_medicamento_favorito_fk_medicamento",
            unique = false,
            value = [MedicamentoFavoritoTable.Columns.FK_MEDICAMENTO]
        ),
        Index(
            name = "idx_medicamento_favorito_fk_usuario",
            unique = false,
            value = [MedicamentoFavoritoTable.Columns.FK_USUARIO]
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

//TODO: Funciones de extensión