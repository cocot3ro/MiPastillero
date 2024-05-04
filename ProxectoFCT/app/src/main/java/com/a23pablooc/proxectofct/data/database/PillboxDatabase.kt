package com.a23pablooc.proxectofct.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.a23pablooc.proxectofct.data.database.converters.DateConverter
import com.a23pablooc.proxectofct.data.database.converters.SetConverter
import com.a23pablooc.proxectofct.data.database.dao.AgendaDAO
import com.a23pablooc.proxectofct.data.database.dao.CalendarioDAO
import com.a23pablooc.proxectofct.data.database.dao.HistorialDAO
import com.a23pablooc.proxectofct.data.database.dao.MedicamentoActivoDAO
import com.a23pablooc.proxectofct.data.database.dao.MedicamentoDAO
import com.a23pablooc.proxectofct.data.database.dao.MedicamentoFavoritoDAO
import com.a23pablooc.proxectofct.data.database.dao.NotificacionDAO
import com.a23pablooc.proxectofct.data.database.dao.UsuarioDAO
import com.a23pablooc.proxectofct.data.database.entities.AgendaEntity
import com.a23pablooc.proxectofct.data.database.entities.HistorialEntity
import com.a23pablooc.proxectofct.data.database.entities.MedicamentoActivoEntity
import com.a23pablooc.proxectofct.data.database.entities.MedicamentoCalendarioEntity
import com.a23pablooc.proxectofct.data.database.entities.MedicamentoEntity
import com.a23pablooc.proxectofct.data.database.entities.MedicamentoFavoritoEntity
import com.a23pablooc.proxectofct.data.database.entities.NotificacionEntity
import com.a23pablooc.proxectofct.data.database.entities.UsuarioEntity

@Database(
    entities = [
        UsuarioEntity::class,
        MedicamentoEntity::class,
        MedicamentoActivoEntity::class,
        MedicamentoFavoritoEntity::class,
        MedicamentoCalendarioEntity::class,
        AgendaEntity::class,
        HistorialEntity::class,
        NotificacionEntity::class
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

    abstract fun getMedicamentoActivoDao(): MedicamentoActivoDAO

    abstract fun getMedicamentoFavoritoDao(): MedicamentoFavoritoDAO

    abstract fun getCalendarioDao(): CalendarioDAO

    abstract fun getHistorialDao(): HistorialDAO

    abstract fun getUsuarioDao(): UsuarioDAO

    abstract fun getAgendaDao(): AgendaDAO

    abstract fun getNotificacionDao(): NotificacionDAO
}