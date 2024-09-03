package com.cocot3ro.mipastillero.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.cocot3ro.mipastillero.data.database.converters.DateBooleanMapConverter
import com.cocot3ro.mipastillero.data.database.converters.DateConverter
import com.cocot3ro.mipastillero.data.database.converters.DateSetConverter
import com.cocot3ro.mipastillero.data.database.converters.StringListConverter
import com.cocot3ro.mipastillero.data.database.converters.UriConverter
import com.cocot3ro.mipastillero.data.database.dao.AgendaDAO
import com.cocot3ro.mipastillero.data.database.dao.MedicamentoActivoDAO
import com.cocot3ro.mipastillero.data.database.dao.MedicamentoActivoWithNotificacionDAO
import com.cocot3ro.mipastillero.data.database.dao.MedicamentoDAO
import com.cocot3ro.mipastillero.data.database.dao.MedicamentoWithMedicamentoActivoDAO
import com.cocot3ro.mipastillero.data.database.dao.NotificacionDAO
import com.cocot3ro.mipastillero.data.database.dao.UsuarioDAO
import com.cocot3ro.mipastillero.data.database.entities.AgendaEntity
import com.cocot3ro.mipastillero.data.database.entities.MedicamentoActivoEntity
import com.cocot3ro.mipastillero.data.database.entities.MedicamentoEntity
import com.cocot3ro.mipastillero.data.database.entities.NotificacionEntity
import com.cocot3ro.mipastillero.data.database.entities.UsuarioEntity

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