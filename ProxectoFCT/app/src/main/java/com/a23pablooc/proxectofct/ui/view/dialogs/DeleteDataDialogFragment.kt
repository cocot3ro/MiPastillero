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
import okhttp3.internal.format

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
            throw ClassCastException("$context must implement OnDeleteDataListener")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            AlertDialog.Builder(it).apply {
                setView(createView())
                setTitle(getString(R.string.delete_data))
                setPositiveButton(R.string.accept, null)
                setNegativeButton(R.string.cancel, null)
            }.create().apply {
                setOnShowListener {
                    setUpPositiveButton(this)
                }
            }
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun createView(): View {
        binding = FragmentDeleteDataDialogBinding.inflate(layoutInflater)

        binding.tvConfirm.text = String.format(
            getString(R.string.delete_data_confirmation),
            getString(R.string.delete_data_confirmation_key)
        )

        return binding.root
    }

    private fun setUpPositiveButton(dialog: AlertDialog) {
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).apply {
            isEnabled = false

            setOnClickListener {
                if (binding.etConfirm.text.toString() != getString(R.string.delete_data_confirmation_key)) return@setOnClickListener

                listener.onDeleteData()
                dialog.dismiss()
            }

            binding.etConfirm.addTextChangedListener(
                afterTextChanged = { text ->
                    isEnabled = text.toString() == getString(R.string.delete_data_confirmation_key)
                }
            )
        }
    }
}