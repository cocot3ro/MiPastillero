package com.a23pablooc.proxectofct.data.repositories

import android.icu.util.Calendar
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
import com.a23pablooc.proxectofct.domain.model.UsuarioItem
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
    private val medicamentoWithMedicamentoActivoDAO: MedicamentoWithMedicamentoActivoDAO,
    private val userInfoProvider: UserInfoProvider
) {
    fun getAllWithMedicamentosByDiaOrderByHora(dia: Date): Flow<List<MedicamentoActivoItem>> {
        return medicamentoWithMedicamentoActivoDAO.getAllByDiaOrderByHora(
            userInfoProvider.currentUser.pkUsuario, dia.time
        ).map { list ->
            list.distinct().map { medWithMedActivo ->
                medWithMedActivo.medicamentosActivos.map { medActivo ->
                    medActivo.toDomain(medWithMedActivo.medicamento)
                }
            }.flatten()
        }
    }

    suspend fun update(med: MedicamentoActivoItem) {
        medicamentoActivoDAO.update(med.toDatabase(userInfoProvider.currentUser.pkUsuario).medicamentosActivos[0])
    }

    fun getAllFavoriteMeds(): Flow<List<MedicamentoItem>> {
        return medicamentoDAO.getAllFavoritos(userInfoProvider.currentUser.pkUsuario)
            .map { list -> list.map { med -> med.toDomain() } }
    }

    fun getMedicamentosActivos(): Flow<List<MedicamentoActivoItem>> {
        return medicamentoWithMedicamentoActivoDAO.getAllFromDate(
            userInfoProvider.currentUser.pkUsuario
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
        val dbMed = med.toDatabase(userInfoProvider.currentUser.pkUsuario)
        val insertedCodNacional = medicamentoDAO.upsert(dbMed.medicamento)
        medicamentoActivoDAO.insert(dbMed.medicamentosActivos[0].apply {
            fkMedicamento = insertedCodNacional.takeIf { it != -1L }
                ?: dbMed.medicamento.pkCodNacionalMedicamento
        })
    }

    suspend fun updateMedicamento(medicamento: MedicamentoItem) {
        medicamentoDAO.update(medicamento.toDatabase(userInfoProvider.currentUser.pkUsuario))
    }

    fun getMedicamentosTerminados(): List<MedicamentoActivoItem> {
        return medicamentoWithMedicamentoActivoDAO.getMedicamentosTerminados(
            userInfoProvider.currentUser.pkUsuario,
            Calendar.getInstance().time.zeroTime().time
        ).map { medWithMedActivo ->
            medWithMedActivo.medicamentosActivos.map { medActivo ->
                medActivo.toDomain(medWithMedActivo.medicamento)
            }
        }.flatten()
    }

    suspend fun deleteMedicamentosTerminados(meds: List<MedicamentoActivoItem>) {
        medicamentoActivoDAO.deleteAll(meds.map { it.toDatabase(userInfoProvider.currentUser.pkUsuario).medicamentosActivos[0] })
    }

    suspend fun addHistorial(med: HistorialItem) {
        historialDAO.insert(med.toDatabase(userInfoProvider.currentUser.pkUsuario))
    }

    fun getUsers(): Flow<List<UsuarioItem>> {
        return usuarioDAO.getAll().map { list -> list.map { user -> user.toDomain() } }
    }

    suspend fun createUser(user: UsuarioItem): Long {
        return usuarioDAO.insert(user.toDatabase())
    }

    suspend fun deleteUser(user: UsuarioItem) {
        usuarioDAO.delete(user.toDatabase())
    }
}