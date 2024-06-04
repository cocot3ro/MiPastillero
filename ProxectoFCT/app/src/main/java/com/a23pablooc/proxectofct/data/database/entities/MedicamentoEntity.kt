package com.a23pablooc.proxectofct.data.database.entities

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoTableDefinition
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoTableDefinition.Columns.AFECTA_CONDUCCION
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoTableDefinition.Columns.ES_FAVORITO
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoTableDefinition.Columns.FK_USUARIO
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoTableDefinition.Columns.IMAGEN
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoTableDefinition.Columns.LABORATORIO
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoTableDefinition.Columns.NOMBRE
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoTableDefinition.Columns.NUM_REGISTRO
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoTableDefinition.Columns.PK_COD_NACIONAL_MEDICAMENTO
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoTableDefinition.Columns.PRESCRIPCION
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoTableDefinition.Columns.PROSPECTO
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoTableDefinition.Columns.URL
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoTableDefinition.Indexes.IDX_MEDICAMENTO_USER_ID
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoTableDefinition.TABLE_NAME
import com.a23pablooc.proxectofct.data.database.definitions.UsuarioTableDefinition

@Entity(
    tableName = TABLE_NAME,
    foreignKeys = [
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
            value = [FK_USUARIO],
            name = IDX_MEDICAMENTO_USER_ID,
            unique = false
        )
    ]
)
data class MedicamentoEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = PK_COD_NACIONAL_MEDICAMENTO)
    var pkCodNacionalMedicamento: Long = 0,

    @ColumnInfo(name = FK_USUARIO)
    var fkUsuario: Long,

    @ColumnInfo(name = URL)
    var url: String,

    @ColumnInfo(name = NOMBRE)
    var nombre: String,

    @ColumnInfo(name = PROSPECTO)
    var prospecto: String,

    @ColumnInfo(name = ES_FAVORITO)
    var esFavorito: Boolean,

    @ColumnInfo(name = LABORATORIO)
    var laboratorio: String,

    @ColumnInfo(name = NUM_REGISTRO)
    var numRegistro: String,

    @ColumnInfo(name = IMAGEN)
    var imagen: Uri,

    @ColumnInfo(name = PRESCRIPCION)
    var prescripcion: String,

    @ColumnInfo(name = AFECTA_CONDUCCION)
    var afectaConduccion: Boolean
)