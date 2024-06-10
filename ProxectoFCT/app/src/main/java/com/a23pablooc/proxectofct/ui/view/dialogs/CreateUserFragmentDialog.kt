package com.a23pablooc.proxectofct.ui.view.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.a23pablooc.proxectofct.R
import com.a23pablooc.proxectofct.databinding.FragmentCreateUserFragmentDialogBinding
import com.a23pablooc.proxectofct.domain.model.UsuarioItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateUserFragmentDialog : DialogFragment() {

    private lateinit var binding: FragmentCreateUserFragmentDialogBinding

    private lateinit var listener: OnDataEnteredListener

    interface OnDataEnteredListener {
        fun onDataEntered(user: UsuarioItem, isDefault: Boolean)
    }

    companion object {
        const val TAG = "CreateUserFragmentDialog"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            listener = parentFragment as OnDataEnteredListener
        } catch (e: ClassCastException) {
            throw ClassCastException(("$context must implement OnDataEnteredListener"))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            AlertDialog.Builder(it).apply {
                setView(createView())
                setTitle("Crear usuario") // TODO: hardcode string
                setPositiveButton(R.string.aceptar, null)
                setNegativeButton(R.string.cancelar, null)
            }.create().apply {
                setOnShowListener {
                    setUpPositiveButton(this)
                }
            }
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun setUpPositiveButton(dialog: AlertDialog) {
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val nombre = binding.etUserName.text.toString().trim()

            if (nombre.isBlank()) {
                binding.etUserName.error =
                    "Campo obligatorio. Debe contener al menos una letra y un máximo de 30 caracteres" // TODO: hardcode string
                return@setOnClickListener
            }

            val user = UsuarioItem(
                pkUsuario = 0,
                nombre = nombre
            )

            val isDefault = binding.ivFavBg.visibility == View.VISIBLE

            listener.onDataEntered(user, isDefault)
            dialog.dismiss()
        }
    }

    private fun createView(): View {
        binding = FragmentCreateUserFragmentDialogBinding.inflate(layoutInflater)

        binding.favFrame.setOnClickListener {
            if (binding.ivFavBg.visibility == View.GONE) {
                binding.ivFavBg.visibility = View.VISIBLE
                Toast.makeText(
                    context,
                    "Este será ahora el usuario por defecto",
                    Toast.LENGTH_SHORT
                ).show() // TODO: hardcode string
            } else {
                binding.ivFavBg.visibility = View.GONE
            }
        }

        return binding.root
    }
}