package com.example.uf1_proyecto

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.google.gson.GsonBuilder
import khttp.responses.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Calendar

class PillboxViewModel private constructor(context: Context) : ViewModel() {

    /**
     * Instancia de la clase [DBHelper] para acceder a la base de datos
     */
    private var dbHelper: DBHelper = DBHelper.getInstance(context)

    companion object {
        /**
         * Instancia única de la clase [PillboxViewModel]
         */
        @Volatile
        private var instance: PillboxViewModel? = null

        /**
         * Devuelve la instancia única de la clase [PillboxViewModel]
         */
        fun getInstance(context: Context): PillboxViewModel =
            instance ?: synchronized(this) {
                instance ?: PillboxViewModel(context.applicationContext).also { instance = it }
            }
    }

    /**
     * Abre un enlace en el navegador
     * @param context contexto de la aplicación
     * @param url URL a abrir
     */
    fun openURL(context: Context, url: String?): Boolean {
        if (url.isNullOrBlank()) {
            return false
        }

        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            context.startActivity(intent)
        } catch (e: Exception) {
            return false
        }

        return true
    }

    /**
     * Devuelve los medicamentos activos de un día agrupados por hora
     * @param dia día en milisegundos
     */
    fun getActivosCalendario(dia: Long) = dbHelper.getActivosCalendario(dia)

    /**
     * Devuelve el texto de la entrada de la agenda del dia correspondiente
     */
    fun getDiaryText(dia: Long) = dbHelper.getDiaryEntry(dia) ?: ""

    /**
     * Devuelve todos los medicamentos activos
     */
    fun getActivos() = dbHelper.getActivos()

    /**
     * Devuelve los medicamentos favoritos
     */
    fun getFavoritos() = dbHelper.getFavoritos()

    /**
     * Añade un medicamento a la lista de medicamentos activos
     */
    fun addActiveMed(medicamento: Medicamento) =
        dbHelper.insertIntoActivos(medicamento)

    /**
     * Elimina un medicamento de la lista de medicamentos activos
     */
    fun deleteActiveMed(medicamento: Medicamento) =
        dbHelper.deleteFromActivos(medicamento)

    /**
     * Añade un medicamento a la lista de medicamentos favoritos
     */
    fun addFavMed(medicamento: Medicamento) = dbHelper.insertIntoFavoritos(medicamento)

    /**
     * Elimina un medicamento de la lista de medicamentos favoritos
     */
    fun deleteFavMed(medicamento: Medicamento) = dbHelper.deleteFromFavoritos(medicamento)

    /**
     * Inserta una entrada en la agenda
     * @param descripcion descripción de la entrada
     * @return true si se ha insertado correctamente, false si no
     */
    fun insertIntoAgenda(date: Long, descripcion: String): Boolean {
        return dbHelper.insertIntoAgenda(date, descripcion)
    }

    /**
     * Desmarca la toma de un medicamento
     * @param med medicamento
     * @param hora hora de la toma
     * @param dia día de la toma
     * @return true si se ha desmarcado correctamente, false si no
     */
    fun desmarcarToma(med: Medicamento, hora: Long, dia: Long) =
        dbHelper.desmarcarToma(med, hora, dia)

    /**
     * Marca un medicamento como tomado en una hora y día concretos
     * @param med medicamento
     * @param hora hora de la toma
     * @param dia día de la toma
     * @return true si se ha marcado correctamente, false si no
     */
    fun marcarToma(med: Medicamento, hora: Long, dia: Long) = dbHelper.marcarToma(med, hora, dia)

    /**
     * Borra de la tabla [ContratoActivos.NOMBRE_TABLA] los medicamentos activos cuyo periodo de
     * toma haya terminado y los inserta en la tabla [ContratoHistorial.NOMBRE_TABLA]
     */
    fun comprobarTerminados() = dbHelper.comprobarTerminados()

    /**
     * Devuelve el historial de medicamentos
     */
    fun getHistorial(): Map<Long, List<Medicamento>> = dbHelper.getHistorial()

    /**
     * Devuelve la paleta de colores para la estación actual
     * @return Pair<Int, Int> con los colores de la estación
     */
    fun getEstacionColor(): Pair<Int, Int>  = getEstacionColor(DateTimeUtils.getTodayAsMillis())

    /**
     * Devuelve la paleta de colores para la estación correspondiente a una fecha
     * @param date fecha
     * @return Pair<Int, Int> con los colores de la estación
     */
    fun getEstacionColor(date: Long): Pair<Int, Int> {
        return when (getEstacion(date)!!) {
            Estaciones.INVIERNO -> R.color.primaryDarkWinter to R.color.primaryWinter
            Estaciones.PRIMAVERA -> R.color.primaryDarkSpring to R.color.primarySpring
            Estaciones.VERANO -> R.color.primaryDarkSummer to R.color.primarySummer
            Estaciones.OTONHO -> R.color.primaryDarkAutumn to R.color.primaryAutumn
        }
    }

    /**
     * Devuelve la estación actual
     * @return estación actual
     * @see Estaciones
     */
    private fun getEstacion(date: Long): Estaciones? {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = date
        }

        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        return when (month) {
            Calendar.JANUARY, Calendar.FEBRUARY -> Estaciones.INVIERNO
            Calendar.APRIL, Calendar.MAY -> Estaciones.PRIMAVERA
            Calendar.JULY, Calendar.AUGUST -> Estaciones.VERANO
            Calendar.OCTOBER, Calendar.NOVEMBER -> Estaciones.OTONHO
            Calendar.MARCH -> if (dayOfMonth < 21) Estaciones.INVIERNO else Estaciones.PRIMAVERA
            Calendar.JUNE -> if (dayOfMonth < 21) Estaciones.PRIMAVERA else Estaciones.VERANO
            Calendar.SEPTEMBER -> if (dayOfMonth < 21) Estaciones.VERANO else Estaciones.OTONHO
            Calendar.DECEMBER -> if (dayOfMonth < 21) Estaciones.OTONHO else Estaciones.INVIERNO
            else -> null
        }
    }

    /**
     * Consulta el API de la AEMPS para buscar un medicamento
     * @param codNacional código nacional del medicamento
     * @return medicamento si se ha encontrado, null si no
     */
    suspend fun searchMedicamento(codNacional: String): Medicamento.Builder? {
        return withContext(Dispatchers.IO) {
            val apiUrl = "https://cima.aemps.es/cima/rest/medicamento?cn=$codNacional"

            try {
                val response: Response = khttp.get(apiUrl, timeout = 5.0)

                if (response.statusCode != 200) {
                    return@withContext null
                }

                GsonBuilder()
                    .registerTypeAdapter(Medicamento.Builder::class.java, APITypeAdapter())
                    .create()
                    .fromJson(response.text, Medicamento.Builder::class.java)

            } catch (e: Exception) {
                null
            }
        }
    }
}
