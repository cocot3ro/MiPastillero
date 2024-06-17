package com.a23pablooc.proxectofct.ui.view.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import com.a23pablooc.proxectofct.R
import com.a23pablooc.proxectofct.databinding.FragmentDeleteDataDialogBinding

class DeleteDataDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentDeleteDataDialogBinding

    private lateinit var listener: OnDeleteDataListener

    interface OnDeleteDataListener {
        fun onDeleteData()
    }

    companion object {
        const val TAG = "DeleteDataDialogFragment"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            listener = parentFragment as OnDeleteDataListener
        } catch (e: ClassCastException) {
            throw ClassCastException(("$context must implement OnDeleteDataListener"))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            AlertDialog.Builder(it).apply {
                setView(createView())
                setTitle("Borrar datos") // TODO: Hardcode string
                setPositiveButton(R.string.aceptar, null)
                setNegativeButton(R.string.cancelar, null)
            }.create().apply {
                setOnShowListener {
                    setUpPositiveButton(this)
                }
            }
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun createView(): View {
        binding = FragmentDeleteDataDialogBinding.inflate(layoutInflater)

        return binding.root
    }

    private fun setUpPositiveButton(dialog: AlertDialog) {
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).apply {
            isEnabled = false

            setOnClickListener {
                if (binding.etConfirm.text.toString() != "CONFIRMAR") return@setOnClickListener

                listener.onDeleteData()
                dialog.dismiss()
            }

            binding.etConfirm.addTextChangedListener(
                afterTextChanged = { text ->
                    isEnabled = text.toString() == "CONFIRMAR"
                }
            )
        }
    }
}