package com.example.uf1_proyecto

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText

class DataInputDialog(context: Context, private val listener: OnDataEnteredListener) {

    interface OnDataEnteredListener {
        fun onDataEntered(data: String)
    }

    private val dialogView: View =
        LayoutInflater.from(context).inflate(R.layout.dialog_data_input, null)
    private val inputCodNacional: EditText = dialogView.findViewById(R.id.codNacional)

    private val alertDialog: AlertDialog = AlertDialog.Builder(context)
        .setView(dialogView)
        .setTitle(context.getString(R.string.add_medicament))
        .setPositiveButton(context.getString(R.string.accept)) { _, _ ->
            val codNacional = inputCodNacional.text.toString()
            listener.onDataEntered(codNacional)
        }
        .setNegativeButton(context.getString(R.string.cancel), null)
        .create()

    fun show() {
        alertDialog.show()
    }

    // TODO: fun add time picker

}