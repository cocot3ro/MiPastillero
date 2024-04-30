package com.a23pablooc.proxectofct.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.a23pablooc.proxectofct.data.model.MedicamentoModel
import com.a23pablooc.proxectofct.domain.model.MedicamentoItem

@Entity(tableName = "medicamentos")
data class MedicamentoEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "PK_medicamento")
    val id: Int = 0,

    @ColumnInfo(name = "num_registro")
    val numRegistro: String,

    @ColumnInfo(name = "nombre")
    val nombre: String,

    @ColumnInfo(name = "url")
    val url: String,

    @ColumnInfo(name = "prospecto")
    val prospecto: String,

    @ColumnInfo(name = "imagen")
    val imagen: ByteArray,

    @ColumnInfo(name = "laboratorio")
    val laboratorio: String,

    @ColumnInfo(name = "dosis")
    val dosis: String,

    @ColumnInfo(name = "prescripcion")
    val prescripcion: String,

    @ColumnInfo(name = "conduccion")
    val conduccion: Boolean,

    @ColumnInfo(name = "favorito")
    val favorito: Boolean
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MedicamentoEntity

        if (id != other.id) return false
        if (numRegistro != other.numRegistro) return false
        if (nombre != other.nombre) return false
        if (url != other.url) return false
        if (prospecto != other.prospecto) return false
        if (!imagen.contentEquals(other.imagen)) return false
        if (laboratorio != other.laboratorio) return false
        if (dosis != other.dosis) return false
        if (prescripcion != other.prescripcion) return false
        if (conduccion != other.conduccion) return false
        if (favorito != other.favorito) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + numRegistro.hashCode()
        result = 31 * result + nombre.hashCode()
        result = 31 * result + url.hashCode()
        result = 31 * result + prospecto.hashCode()
        result = 31 * result + imagen.contentHashCode()
        result = 31 * result + laboratorio.hashCode()
        result = 31 * result + dosis.hashCode()
        result = 31 * result + prescripcion.hashCode()
        result = 31 * result + conduccion.hashCode()
        result = 31 * result + favorito.hashCode()
        return result
    }
}

fun MedicamentoModel.toDatabase(): MedicamentoEntity {
    return MedicamentoEntity(
        numRegistro = numRegistro,
        nombre = nombre,
        url = url,
        prospecto = prospecto,
        imagen = imagen,
        laboratorio = laboratorio,
        dosis = dosis,
        prescripcion = prescripcion,
        conduccion = conduccion,
        favorito = favorito)
}

fun MedicamentoItem.toDatabase(): MedicamentoEntity {
    return MedicamentoEntity(
        numRegistro = numRegistro,
        nombre = nombre,
        url = url,
        prospecto = prospecto,
        imagen = imagen,
        laboratorio = laboratorio,
        dosis = dosis,
        prescripcion = prescripcion,
        conduccion = conduccion,
        favorito = favorito)
}