package com.a23pablooc.proxectofct.ui.view.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import com.a23pablooc.proxectofct.R
import com.a23pablooc.proxectofct.databinding.FragmentCreateUserFragmentDialogBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateUserDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentCreateUserFragmentDialogBinding

    private lateinit var listener: OnDataEnteredListener

    interface OnDataEnteredListener {
        fun onDataEntered(userName: String, isDefault: Boolean)
    }

    companion object {
        const val TAG = "CreateUserFragmentDialog"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            listener = parentFragment as OnDataEnteredListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement OnDataEnteredListener")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            AlertDialog.Builder(it).apply {
                setView(createView())
                setTitle(getString(R.string.create_user))
                setPositiveButton(getString(R.string.accept), null)
                setNegativeButton(getString(R.string.cancel), null)
            }.create().apply {
                setOnShowListener {
                    setUpPositiveButton(this)
                }
            }
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun setUpPositiveButton(dialog: AlertDialog) {
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).apply {
            isEnabled = false

            binding.etUserName.addTextChangedListener(
                afterTextChanged = { text ->
                    isEnabled = !text.isNullOrBlank()
                }
            )

            setOnClickListener {
                val nombre = binding.etUserName.text.toString().trim()

                if (nombre.isBlank()) {
                    binding.etUserName.error = getString(R.string.required_field)
                    return@setOnClickListener
                }

                val isDefault = binding.ivFavBg.visibility == View.VISIBLE

                listener.onDataEntered(nombre, isDefault)
                dialog.dismiss()
            }
        }
    }

    private fun createView(): View {
        binding = FragmentCreateUserFragmentDialogBinding.inflate(layoutInflater)

        binding.favFrame.setOnClickListener {
            if (binding.ivFavBg.visibility == View.GONE) {
                binding.ivFavBg.visibility = View.VISIBLE
                Toast.makeText(
                    context,
                    getString(R.string.new_default_user),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                binding.ivFavBg.visibility = View.GONE
            }
        }

        return binding.root
    }
}