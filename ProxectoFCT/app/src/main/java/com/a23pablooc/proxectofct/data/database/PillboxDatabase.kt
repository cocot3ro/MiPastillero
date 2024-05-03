package com.a23pablooc.proxectofct.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.a23pablooc.proxectofct.data.database.converters.DateConverter
import com.a23pablooc.proxectofct.data.database.converters.SetConverter
import com.a23pablooc.proxectofct.data.database.dao.MedicamentoDAO
import com.a23pablooc.proxectofct.data.database.entities.MedicamentoActivoEntity
import com.a23pablooc.proxectofct.data.database.entities.MedicamentoCalendarioEntity
import com.a23pablooc.proxectofct.data.database.entities.MedicamentoEntity
import com.a23pablooc.proxectofct.data.database.entities.MedicamentoFavoritoEntity
import com.a23pablooc.proxectofct.data.database.entities.UsuarioEntity

@Database(
    entities = [
        UsuarioEntity::class,
        MedicamentoEntity::class,
        MedicamentoActivoEntity::class,
        MedicamentoFavoritoEntity::class,
        MedicamentoCalendarioEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    value = [
        SetConverter::class,
        DateConverter::class
    ]
)
abstract class PillboxDatabase : RoomDatabase() {
    abstract fun getMedicamentoDao(): MedicamentoDAO

    //TODO: Añadir aquí los DAOs que se vayan creando
}
