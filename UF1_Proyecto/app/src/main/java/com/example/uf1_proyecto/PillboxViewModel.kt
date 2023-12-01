package com.example.uf1_proyecto

import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.net.Uri
import androidx.lifecycle.ViewModel
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

    fun openPDF(context: Context, url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        context.startActivity(intent)
    }

    fun millisToHour(millis: Long): String =
        SimpleDateFormat.getTimeInstance().format(Date(millis))

    fun millisToDate(millis: Long): String =
        SimpleDateFormat.getDateInstance().format(Date(millis))

    fun hourToMillis(hour: String): Long =
        SimpleDateFormat.getTimeInstance().parse(hour).time

    fun dateToMillis(date: String): Long =
        SimpleDateFormat.getDateInstance().parse(date).time

    // TODO: fun get this week ""
    fun getActivos() = dbHelper.getActivos()

    fun addActiveMed(medicamento: Medicamento) = dbHelper.insertIntoActivos(medicamento)

    fun deleteActiveMed(medicamento: Medicamento) = dbHelper.deleteFromActivos(medicamento)

    fun addFavMed(medicamento: Medicamento) = dbHelper.insertIntoFavoritos(medicamento)

    fun deleteFavMed(medicamento: Medicamento) = dbHelper.deleteFromFavoritos(medicamento)

    // TODO: Borrar
    @Deprecated("Marked for removal")
    fun ejemplosActivos() {
        dbHelper.ejemplosActivos()
    }

}
