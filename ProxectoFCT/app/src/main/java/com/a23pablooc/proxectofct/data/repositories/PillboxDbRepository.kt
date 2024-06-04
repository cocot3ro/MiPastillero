package com.a23pablooc.proxectofct.data.repositories

import android.util.Log
import com.a23pablooc.proxectofct.core.DateTimeUtils.zeroTime
import com.a23pablooc.proxectofct.core.UserInfoProvider
import com.a23pablooc.proxectofct.data.database.dao.AgendaDAO
import com.a23pablooc.proxectofct.data.database.dao.HistorialDAO
import com.a23pablooc.proxectofct.data.database.dao.MedicamentoActivoDAO
import com.a23pablooc.proxectofct.data.database.dao.MedicamentoActivoWithNotificacionDAO
import com.a23pablooc.proxectofct.data.database.dao.MedicamentoDAO
import com.a23pablooc.proxectofct.data.database.dao.MedicamentoWithMedicamentoActivoDAO
import com.a23pablooc.proxectofct.data.database.dao.NotificacionDAO
import com.a23pablooc.proxectofct.data.database.dao.UsuarioDAO
import com.a23pablooc.proxectofct.data.database.entities.extensions.toDomain
import com.a23pablooc.proxectofct.domain.model.HistorialItem
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem
import com.a23pablooc.proxectofct.domain.model.MedicamentoItem
import com.a23pablooc.proxectofct.domain.model.extensions.toDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject

class PillboxDbRepository @Inject constructor(
    private val usuarioDAO: UsuarioDAO,
    private val medicamentoDAO: MedicamentoDAO,
    private val medicamentoActivoDAO: MedicamentoActivoDAO,
    private val agendaDAO: AgendaDAO,
    private val historialDAO: HistorialDAO,
    private val notificacionDAO: NotificacionDAO,
    private val medicamentoActivoWithNotificacionDAO: MedicamentoActivoWithNotificacionDAO,
    private val medicamentoWithMedicamentoActivoDAO: MedicamentoWithMedicamentoActivoDAO
) {
    fun getAllWithMedicamentosByDiaOrderByHora(dia: Date): Flow<List<MedicamentoActivoItem>> {
        return medicamentoWithMedicamentoActivoDAO.getAllByDiaOrderByHora(
            UserInfoProvider.currentUser.pkUsuario, dia.time
        ).map { list ->
            list.map { medWithMedActivo ->
                medWithMedActivo.medicamentosActivos.map { medActivo ->
                    medActivo.toDomain(medWithMedActivo.medicamento)
                }
            }.flatten()
        }
    }

    suspend fun update(med: MedicamentoActivoItem) {
        medicamentoActivoDAO.update(med.toDatabase().medicamentosActivos[0])
    }

    fun getAllFavoriteMeds(): Flow<List<MedicamentoItem>> {
        return medicamentoDAO.getAllFavoritos(UserInfoProvider.currentUser.pkUsuario)
            .map { list -> list.map { med -> med.toDomain() } }
    }

    fun getMedicamentosActivos(fromDate: Date): Flow<List<MedicamentoActivoItem>> {
        return medicamentoWithMedicamentoActivoDAO.getAllFromDate(
            UserInfoProvider.currentUser.pkUsuario,
            fromDate.time
        ).map { list ->
            list.map { medWithMedActivo ->
                medWithMedActivo.medicamentosActivos.map { medActivo ->
                    medActivo.toDomain(medWithMedActivo.medicamento)
                }
            }.flatten()
        }
    }

    fun findMedicamentoByCodNacional(userId: Long, codNacional: Long): MedicamentoItem? {
        return medicamentoDAO.findByCodNacional(userId, codNacional)?.toDomain()
    }

    suspend fun addMedicamentoActivo(med: MedicamentoActivoItem) {
        val dbMed = med.toDatabase()
        val codNacional = medicamentoDAO.upsert(dbMed.medicamento)
        Log.v("PillboxDbRepository", "codNacional after upsert: $codNacional")
        medicamentoActivoDAO.insert(dbMed.medicamentosActivos[0].apply {
            fkMedicamento = codNacional ?: dbMed.medicamento.pkCodNacionalMedicamento
        })
    }

    suspend fun updateMedicamento(medicamento: MedicamentoItem) {
        medicamentoDAO.update(medicamento.toDatabase())
    }

    fun getMedicamentosTerminados(): List<MedicamentoActivoItem> {
        return medicamentoWithMedicamentoActivoDAO.getMedicamentosTerminados(
            UserInfoProvider.currentUser.pkUsuario,
            Date().zeroTime().time
        ).map { medWithMedActivo ->
            medWithMedActivo.medicamentosActivos.map { medActivo ->
                medActivo.toDomain(medWithMedActivo.medicamento)
            }
        }.flatten()
    }

    suspend fun deleteMedicamentosTerminados(meds: List<MedicamentoActivoItem>) {
        medicamentoActivoDAO.deleteAll(meds.map { it.toDatabase().medicamentosActivos[0] })
    }

    suspend fun addHistorial(med: HistorialItem) {
        historialDAO.insert(med.toDatabase())
    }
}