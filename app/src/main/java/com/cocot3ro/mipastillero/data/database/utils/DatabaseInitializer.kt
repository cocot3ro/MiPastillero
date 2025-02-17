package com.cocot3ro.mipastillero.data.database.utils

import androidx.sqlite.db.SupportSQLiteDatabase
import com.cocot3ro.mipastillero.data.database.definitions.MedTable

object DatabaseInitializer {

    fun initialize(db: SupportSQLiteDatabase) {
        db.beginTransaction()
        try {
            val updatedRows = db.compileStatement(
                """
                        UPDATE sqlite_sequence SET seq = 999999 WHERE name = '${MedTable.TABLE_NAME}'
                    """
            ).executeUpdateDelete()

            if (updatedRows == 0) {
                db.execSQL(
                    """
                            INSERT INTO sqlite_sequence (name, seq) VALUES ('${MedTable.TABLE_NAME}', 999999)
                        """
                )
            }

            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

}