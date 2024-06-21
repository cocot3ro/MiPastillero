package com.a23pablooc.proxectofct.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.a23pablooc.proxectofct.data.database.converters.DateBooleanMapConverter
import com.a23pablooc.proxectofct.data.database.converters.DateConverter
import com.a23pablooc.proxectofct.data.database.converters.DateSetConverter
import com.a23pablooc.proxectofct.data.database.converters.StringListConverter
import com.a23pablooc.proxectofct.data.database.converters.UriConverter
import com.a23pablooc.proxectofct.data.database.dao.AgendaDAO
import com.a23pablooc.proxectofct.data.database.dao.MedicamentoActivoDAO
import com.a23pablooc.proxectofct.data.database.dao.MedicamentoActivoWithNotificacionDAO
import com.a23pablooc.proxectofct.data.database.dao.MedicamentoDAO
import com.a23pablooc.proxectofct.data.database.dao.MedicamentoWithMedicamentoActivoDAO
import com.a23pablooc.proxectofct.data.database.dao.NotificacionDAO
import com.a23pablooc.proxectofct.data.database.dao.UsuarioDAO
import com.a23pablooc.proxectofct.data.database.entities.AgendaEntity
import com.a23pablooc.proxectofct.data.database.entities.MedicamentoActivoEntity
import com.a23pablooc.proxectofct.data.database.entities.MedicamentoEntity
import com.a23pablooc.proxectofct.data.database.entities.NotificacionEntity
import com.a23pablooc.proxectofct.data.database.entities.UsuarioEntity

@Database(
    entities = [
        AgendaEntity::class,
        UsuarioEntity::class,
        MedicamentoEntity::class,
        NotificacionEntity::class,
        MedicamentoActivoEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    value = [
        UriConverter::class,
        DateConverter::class,
        DateSetConverter::class,
        StringListConverter::class,
        DateBooleanMapConverter::class
    ]
)
abstract class PillboxDatabase : RoomDatabase() {
    abstract fun getMedicamentoDao(): MedicamentoDAO

    abstract fun getMedicamentoActivoDao(): MedicamentoActivoDAO

    abstract fun getUsuarioDao(): UsuarioDAO

    abstract fun getAgendaDao(): AgendaDAO

    abstract fun getNotificacionDao(): NotificacionDAO

    abstract fun getMedicamentoAndMedicamentoActivoDao(): MedicamentoWithMedicamentoActivoDAO

    abstract fun getMedicamentoActivoWithNotificacionDao(): MedicamentoActivoWithNotificacionDAO
}