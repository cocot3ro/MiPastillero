package com.a23pablooc.proxectofct.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoActivoTable
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoTable
import com.a23pablooc.proxectofct.data.database.definitions.UsuarioTable
import java.util.Date

@Entity(
    tableName = MedicamentoActivoTable.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = MedicamentoEntity::class,
            parentColumns = [MedicamentoTable.Columns.PK_COD_NACIONAL_MEDICAMENTO],
            childColumns = [MedicamentoActivoTable.Columns.FK_MEDICAMENTO],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UsuarioEntity::class,
            parentColumns = [UsuarioTable.Columns.PK_USUARIO],
            childColumns = [MedicamentoActivoTable.Columns.FK_USUARIO],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(
            name = MedicamentoActivoTable.Indexes.IDX_MEDICAMENTO_ACTIVO_FK_MEDICAMENTO,
            unique = false,
            value = [MedicamentoActivoTable.Columns.FK_MEDICAMENTO]
        ),
        Index(
            name = MedicamentoActivoTable.Indexes.IDX_MEDICAMENTO_ACTIVO_FK_USUARIO,
            unique = false,
            value = [MedicamentoActivoTable.Columns.FK_USUARIO]
        )
    ]
)
data class MedicamentoActivoEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = MedicamentoActivoTable.Columns.PK_MEDICAMENTO_ACTIVO)
    var pkMedicamentoActivo: Int = 0,

    @ColumnInfo(name = MedicamentoActivoTable.Columns.FK_MEDICAMENTO)
    var fkMedicamento: Int,

    @ColumnInfo(name = MedicamentoActivoTable.Columns.FK_USUARIO)
    var fkUsuario: Int,

    @ColumnInfo(name = MedicamentoActivoTable.Columns.FECHA_INICIO)
    var fechaInicio: Date,

    @ColumnInfo(name = MedicamentoActivoTable.Columns.FECHA_FIN)
    var fechaFin: Date,

    @ColumnInfo(name = MedicamentoActivoTable.Columns.HORARIO)
    var horario: MutableSet<Date>,

    @ColumnInfo(name = MedicamentoActivoTable.Columns.DOSIS)
    var dosis: String,

    @ColumnInfo(name = MedicamentoActivoTable.Columns.TOMAS)
    var tomas: MutableMap<Date, MutableMap<Date, Boolean>>
)