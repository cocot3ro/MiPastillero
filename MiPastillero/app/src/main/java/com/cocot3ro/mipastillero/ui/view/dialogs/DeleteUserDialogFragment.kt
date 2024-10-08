package com.cocot3ro.mipastillero.ui.view.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import com.cocot3ro.mipastillero.R
import com.cocot3ro.mipastillero.databinding.FragmentDeleteUserDialogBinding
import com.cocot3ro.mipastillero.domain.model.UsuarioItem

class DeleteUserDialogFragment(
    private val userToDelete: UsuarioItem
) : DialogFragment() {

    private lateinit var binding: FragmentDeleteUserDialogBinding

    private lateinit var listener: OnUserDeletedListener

    interface OnUserDeletedListener {
        fun onUserDeleted(usuario: UsuarioItem)
    }

    companion object {
        const val TAG = "DeleteUserDialogFragment"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            listener = parentFragment as OnUserDeletedListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement OnDataEnteredListener")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            AlertDialog.Builder(it).apply {
                setView(createView())
                setTitle(getString(R.string.delete_user))
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
        binding = FragmentDeleteUserDialogBinding.inflate(layoutInflater)

        binding.tvConfirm.apply {
            text = text.toString().format(userToDelete.nombre)
        }

        return binding.root
    }

    private fun setUpPositiveButton(dialog: AlertDialog) {
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).apply {
            isEnabled = false

            setOnClickListener {
                if (binding.etConfirm.text.toString() != userToDelete.nombre) return@setOnClickListener

                listener.onUserDeleted(userToDelete)
                dialog.dismiss()
            }

            binding.etConfirm.addTextChangedListener(
                afterTextChanged = { text ->
                    isEnabled = text.toString() == userToDelete.nombre
                }
            )
        }
    }
}