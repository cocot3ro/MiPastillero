package com.a23pablooc.proxectofct.data.database

import androidx.sqlite.db.SupportSQLiteDatabase
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoTableDefinition

class DatabaseClearerImpl(
    private val pillboxDatabase: PillboxDatabase,
    private val db: SupportSQLiteDatabase
) : DataBaseClearer {

    override fun clearAllData() {
        pillboxDatabase.clearAllTables()
        db.execSQL("DELETE FROM sqlite_sequence")
        db.execSQL("INSERT INTO sqlite_sequence (name, seq) VALUES ('${MedicamentoTableDefinition.TABLE_NAME}', 999999)")
    }
}