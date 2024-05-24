package com.a23pablooc.proxectofct.domain

import android.content.Context
import java.io.File
import java.util.Date
import javax.inject.Inject

class SaveErrorUseCase @Inject constructor() {
    operator fun invoke(context: Context, errorMessage: String, timeStamp: Date, exception: Throwable) {
        val errorsDir = File(context.filesDir, "errors")

        val file = File(errorsDir, "${timeStamp.time}_${timeStamp}.txt")

        file.bufferedWriter().use { out ->
            out.write(errorMessage)
            out.newLine()
            out.write(exception.stackTraceToString())
        }
    }
}