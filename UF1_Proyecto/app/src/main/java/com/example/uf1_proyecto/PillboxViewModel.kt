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
    private var diaryText = MutableLiveData(getDiaryEntry())

    companion object {
        @Volatile
        private var instance: PillboxViewModel? = null

        fun getInstance(context: Context): PillboxViewModel =
            instance ?: synchronized(this) {
                instance ?: PillboxViewModel(context.applicationContext).also { instance = it }
            }

    }

    fun openPDF(context: Context, url: String?): Boolean {
        if (url.isNullOrEmpty()) {
            return false
        }

        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        context.startActivity(intent)
        return true
    }

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

    fun getTodayAsString(): String = millisToDate(System.currentTimeMillis())

    fun getTodayAsMillis(): Long = dateToMillis(getTodayAsString())

    fun millisToHour(millis: Long): String = SimpleDateFormat.getTimeInstance().format(Date(millis))

    fun millisToDate(millis: Long): String = SimpleDateFormat.getDateInstance().format(Date(millis))

    fun hourToMillis(hour: String): Long = SimpleDateFormat.getTimeInstance().parse(hour).time

    fun dateToMillis(date: String): Long = SimpleDateFormat.getDateInstance().parse(date).time

    fun is24HourFormat(context: Context) = DateFormat.is24HourFormat(context)

    fun createDate(year: Int, monthOfYear: Int, dayOfMonth: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, monthOfYear)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        return calendar.timeInMillis
    }

    fun createHour(hourOfDay: Int, minute: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        return calendar.timeInMillis
    }

    fun getDiaryCurrDate(): Long = diaryCurrDate.value!!

    fun setDiaryCurrDate(date: Long) {
        diaryCurrDate.value = date
        diaryText.value = getDiaryEntry()
    }

    fun diaryNextDay() {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = diaryCurrDate.value!!
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        diaryCurrDate.value = calendar.timeInMillis
        diaryText.value = getDiaryEntry()
    }

    fun diaryPrevDay() {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = diaryCurrDate.value!!
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        diaryCurrDate.value = calendar.timeInMillis
        diaryText.value = getDiaryEntry()
    }

    fun getDiaryText(): String = diaryText.value!!

    private fun getDiaryEntry() = dbHelper.getDiaryEntry(getDiaryCurrDate())


    // TODO: fun get activos por semana

    // TODO: fun get entradas de agenda

    fun getActivos() = dbHelper.getActivos()

    fun getFavoritos() = dbHelper.getFavoritos()

    fun addActiveMed(medicamento: Medicamento) = dbHelper.insertIntoActivos(medicamento)

    fun deleteActiveMed(medicamento: Medicamento) = dbHelper.deleteFromActivos(medicamento)

    fun addFavMed(medicamento: Medicamento) = dbHelper.insertIntoFavoritos(medicamento)

    fun deleteFavMed(medicamento: Medicamento) = dbHelper.deleteFromFavoritos(medicamento)

    fun insertIntoAgenda(descripcion: String): Boolean {
        return if (dbHelper.insertIntoAgenda(getDiaryCurrDate(), descripcion)) {
            diaryText.value = descripcion
            true
        } else {
            false
        }
    }

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
