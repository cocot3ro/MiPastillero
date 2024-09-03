package com.cocot3ro.mipastillero.data.database.entities

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.cocot3ro.mipastillero.data.database.definitions.MedicamentoTableDefinition.Columns.AFECTA_CONDUCCION
import com.cocot3ro.mipastillero.data.database.definitions.MedicamentoTableDefinition.Columns.ES_FAVORITO
import com.cocot3ro.mipastillero.data.database.definitions.MedicamentoTableDefinition.Columns.FK_USUARIO
import com.cocot3ro.mipastillero.data.database.definitions.MedicamentoTableDefinition.Columns.IMAGEN
import com.cocot3ro.mipastillero.data.database.definitions.MedicamentoTableDefinition.Columns.LABORATORIO
import com.cocot3ro.mipastillero.data.database.definitions.MedicamentoTableDefinition.Columns.NOMBRE
import com.cocot3ro.mipastillero.data.database.definitions.MedicamentoTableDefinition.Columns.NUM_REGISTRO
import com.cocot3ro.mipastillero.data.database.definitions.MedicamentoTableDefinition.Columns.PK_COD_NACIONAL_MEDICAMENTO
import com.cocot3ro.mipastillero.data.database.definitions.MedicamentoTableDefinition.Columns.RECETA
import com.cocot3ro.mipastillero.data.database.definitions.MedicamentoTableDefinition.Columns.PROSPECTO
import com.cocot3ro.mipastillero.data.database.definitions.MedicamentoTableDefinition.Columns.URL
import com.cocot3ro.mipastillero.data.database.definitions.MedicamentoTableDefinition.Columns.PRINCIPIOS_ACTIVOS
import com.cocot3ro.mipastillero.data.database.definitions.MedicamentoTableDefinition.Columns.PRESCRIPCION
import com.cocot3ro.mipastillero.data.database.definitions.MedicamentoTableDefinition.Indexes.IDX_MEDICAMENTO_USER_ID
import com.cocot3ro.mipastillero.data.database.definitions.MedicamentoTableDefinition.TABLE_NAME
import com.cocot3ro.mipastillero.data.database.definitions.UsuarioTableDefinition

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
    var url: Uri,

    @ColumnInfo(name = NOMBRE)
    var nombre: String,

    @ColumnInfo(name = PROSPECTO)
    var prospecto: Uri,

    @ColumnInfo(name = ES_FAVORITO)
    var esFavorito: Boolean,

    @ColumnInfo(name = LABORATORIO)
    var laboratorio: String,

    @ColumnInfo(name = NUM_REGISTRO)
    var numRegistro: String,

    @ColumnInfo(name = IMAGEN)
    var imagen: Uri,

    @ColumnInfo(name = RECETA)
    var receta: Boolean,

    @ColumnInfo(name = AFECTA_CONDUCCION)
    var afectaConduccion: Boolean,

    @ColumnInfo(name = PRINCIPIOS_ACTIVOS)
    var principiosActivos: List<String>,

    @ColumnInfo(name = PRESCRIPCION)
    var prescripcion: String
)