package com.a23pablooc.proxectofct.data.database.converters

import android.net.Uri
import androidx.core.net.toUri
import androidx.room.TypeConverter

object UriConverter {
    @TypeConverter
    fun uriToString(uri: Uri): String {
        return uri.toString()
    }

    @TypeConverter
    fun stringToUri(uri: String): Uri {
        return uri.toUri()
    }
}