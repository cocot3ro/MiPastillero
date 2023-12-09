package com.example.uf1_proyecto

import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.text.format.DateFormat
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.GsonBuilder
import khttp.responses.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Date

class PillboxViewModel private constructor(context: Context) : ViewModel() {
    private var dbHelper: DBHelper = DBHelper.getInstance(context)

    private var diaryCurrDate = MutableLiveData(getTodayAsMillis())
    private var diaryText = MutableLiveData(getDiaryEntry() ?: "")

    companion object {
        @Volatile
        private var instance: PillboxViewModel? = null

        fun getInstance(context: Context): PillboxViewModel =
            instance ?: synchronized(this) {
                instance ?: PillboxViewModel(context.applicationContext).also { instance = it }
            }

    }

    /**
     * Abre un enlace a un PDF en el navegador
     * @param context contexto de la aplicación
     * @param url URL del PDF
     */
    fun openPDF(context: Context, url: String?): Boolean {
        if (url.isNullOrEmpty()) {
            return false
        }

        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        context.startActivity(intent)
        return true
    }

    /**
     * Devuelve el día de la semana actual
     */
    fun getTodayAsDayOfWeek(context: Context): String {
        return when (Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> context.getString(R.string.monday)
            Calendar.TUESDAY -> context.getString(R.string.tuesday)
            Calendar.WEDNESDAY -> context.getString(R.string.wednesday)
            Calendar.THURSDAY -> context.getString(R.string.thursday)
            Calendar.FRIDAY -> context.getString(R.string.friday)
            Calendar.SATURDAY -> context.getString(R.string.saturday)
            Calendar.SUNDAY -> context.getString(R.string.sunday)
            else -> ""
        }
    }

    /**
     * Devuelve la fecha actual en el formato de la configuración regional
     */
    fun getTodayAsString(): String = millisToDate(System.currentTimeMillis())

    /**
     * Devuelve la fecha actual en milisegundos
     */
    fun getTodayAsMillis(): Long = dateToMillis(getTodayAsString())

    /**
     * Convierte milisegundos a hora en el formato de la configuración regional
     */
    fun millisToHour(millis: Long): String = SimpleDateFormat.getTimeInstance().format(Date(millis))

    /**
     * Convierte milisegundos a fecha en el formato de la configuración regional
     */
    fun millisToDate(millis: Long): String = SimpleDateFormat.getDateInstance().format(Date(millis))

    /**
     * Convierte hora en el formato de la configuración regional a milisegundos
     */
    fun hourToMillis(hour: String): Long = SimpleDateFormat.getTimeInstance().parse(hour).time

    /**
     * Convierte fecha en el formato de la configuración regional a milisegundos
     */
    fun dateToMillis(date: String): Long = SimpleDateFormat.getDateInstance().parse(date).time

    /**
     * Comprueba si el formato de la hora es de 24 horas
     * @return true si el formato de la hora es de 24 horas, false si es de 12 horas
     */
    fun is24HourFormat(context: Context) = DateFormat.is24HourFormat(context)

    /**
     * Convierte una fecha a milisegundos
     * @param year año
     * @param monthOfYear mes
     * @param dayOfMonth día
     * @return fecha en milisegundos
     */
    fun createDate(year: Int, monthOfYear: Int, dayOfMonth: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, monthOfYear)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        return calendar.timeInMillis
    }

    /**
     * Convierte una hora a milisegundos
     * @param hourOfDay hora
     * @param minute minuto
     * @return hora en milisegundos
     */
    fun createHour(hourOfDay: Int, minute: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        return calendar.timeInMillis
    }

    /**
     * Devuelve la fecha que se está mostrando en la agenda
     */
    fun getDiaryCurrDate(): Long = diaryCurrDate.value!!

    /**
     * Establece la fecha que se está mostrando en la agenda
     * @param date fecha en milisegundos
     */
    fun setDiaryCurrDate(date: Long) {
        diaryCurrDate.value = date
        diaryText.value = getDiaryEntry() ?: ""
    }

    /**
     * Avanza un día en la agenda
     */
    fun diaryNextDay() {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = diaryCurrDate.value!!
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        diaryCurrDate.value = calendar.timeInMillis
        diaryText.value = getDiaryEntry() ?: ""
    }

    /**
     * Retrocede un día en la agenda
     */
    fun diaryPrevDay() {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = diaryCurrDate.value!!
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        diaryCurrDate.value = calendar.timeInMillis
        diaryText.value = getDiaryEntry() ?: ""
    }

    /**
     * Devuelve el texto de la entrada de la agenda
     */
    fun getDiaryText(): String = diaryText.value!!

    /**
     * Recupera de la base de datos el texto de la agenda correspondiente a la fecha actual
     * @see getDiaryCurrDate
     */
    private fun getDiaryEntry() = dbHelper.getDiaryEntry(getDiaryCurrDate())


    // TODO: fun get activos por semana

    // TODO: fun get entradas de agenda

    /**
     * Devuelve los medicamentos activos
     */
    fun getActivos() = dbHelper.getActivos()

    /**
     * Devuelve los medicamentos favoritos
     */
    fun getFavoritos() = dbHelper.getFavoritos()

    /**
     * Añade un medicamento a la lista de medicamentos activos
     */
    fun addActiveMed(medicamento: Medicamento) = dbHelper.insertIntoActivos(medicamento)

    /**
     * Elimina un medicamento de la lista de medicamentos activos
     */
    fun deleteActiveMed(medicamento: Medicamento) = dbHelper.deleteFromActivos(medicamento)

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
    fun insertIntoAgenda(descripcion: String): Boolean {
        return if (dbHelper.insertIntoAgenda(getDiaryCurrDate(), descripcion)) {
            diaryText.value = descripcion
            true
        } else {
            false
        }
    }

    /**
     * Consulta el API de la AEMPS para buscar un medicamento
     * @param codNacional código nacional del medicamento
     * @return medicamento si se ha encontrado, null si no
     */
    suspend fun searchMedicamento(codNacional: String): Medicamento? {
        return withContext(Dispatchers.IO) {
            val apiUrl = "https://cima.aemps.es/cima/rest/medicamento?cn=$codNacional"

            try {
                Log.i("API", "Antes de la llamada")
                val response: Response = khttp.get(apiUrl, timeout = 5.0)

                Log.i("API", "Después de la llamada")

                if (response.statusCode != 200) {
                    return@withContext null
                }

                val gson = GsonBuilder()
                    .registerTypeAdapter(Medicamento::class.java, APITypeAdapter())
                    .create()

                gson.fromJson(response.text, Medicamento::class.java)
            } catch (e: Exception) {
                null
            }
        }
    }

}
