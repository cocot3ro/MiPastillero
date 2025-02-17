package com.cocot3ro.mipastillero.data.database.utils

import androidx.sqlite.db.SupportSQLiteDatabase
import com.cocot3ro.mipastillero.data.database.MiPastilleroDatabase
import com.cocot3ro.mipastillero.data.database.definitions.MedTable

class DataBaseClearer(
    private val miPastilleroDatabase: MiPastilleroDatabase,
    private val writableDatabase: SupportSQLiteDatabase
) {

    fun clear() {
        miPastilleroDatabase.clearAllTables()
        writableDatabase.execSQL("DELETE FROM sqlite_sequence")
        writableDatabase.execSQL("INSERT INTO sqlite_sequence (name, seq) VALUES ('${MedTable.TABLE_NAME}', 999999)")
    }

}