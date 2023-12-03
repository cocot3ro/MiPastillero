package com.example.uf1_proyecto

import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.gson.GsonBuilder
import java.util.Date

class PillboxViewModel private constructor(context: Context) : ViewModel() {
    private var dbHelper: DBHelper = DBHelper.getInstance(context)

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

    fun millisToHour(millis: Long): String =
        SimpleDateFormat.getTimeInstance().format(Date(millis))

    fun millisToDate(millis: Long?): String =
        SimpleDateFormat.getDateInstance().format(Date(millis!!))

    fun hourToMillis(hour: String): Long =
        SimpleDateFormat.getTimeInstance().parse(hour).time

    fun dateToMillis(date: String): Long =
        SimpleDateFormat.getDateInstance().parse(date).time

    // TODO: fun get this week ""

    fun getActivos() = dbHelper.getActivos()

    fun getFavoritos() = dbHelper.getFavoritos()

    fun addActiveMed(medicamento: Medicamento) = dbHelper.insertIntoActivos(medicamento)

    fun deleteActiveMed(medicamento: Medicamento) = dbHelper.deleteFromActivos(medicamento)

    fun addFavMed(medicamento: Medicamento) = dbHelper.insertIntoFavoritos(medicamento)

    fun deleteFavMed(medicamento: Medicamento) = dbHelper.deleteFromFavoritos(medicamento)

    fun searchMedicamento(codNacional: String): Medicamento? {
        val apiUrl = "https://cima.aemps.es/cima/rest/medicamento?cn=$codNacional"

        val response = khttp.get(apiUrl)

        if (response.statusCode != 200) {
            return null
        }

        Log.e("JSON response", response.text)

        val gson = GsonBuilder()
            .registerTypeAdapter(Medicamento::class.java, MedicamentoTypeAdapter())
            .create()

        return gson.fromJson(response.text, Medicamento::class.java)
    }

    // TODO: Borrar
    @Deprecated("Marked for removal")
    fun ejemplosActivos() {
        dbHelper.ejemplosActivos()
    }

}
