package com.example.uf1_proyecto

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import com.example.uf1_proyecto.databinding.DialogAddFavMedBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddFavMedDialog(
    private val context: Context,
    private val listener: OnDataEnteredListener
) {

    interface OnDataEnteredListener {
        fun onDataEntered(medicamento: Medicamento)
    }

    private var _binding: DialogAddFavMedBinding? =
        DialogAddFavMedBinding.inflate(LayoutInflater.from(context))
    private val binding get() = _binding!!

    private var _pillboxViewModel: PillboxViewModel? = null
    private val pillboxViewModel get() = _pillboxViewModel!!

    private val inputCodNacional: EditText = binding.codNacional
    private val inputNombre: EditText = binding.nombre
    private var fichaTecnica: String? = null
    private var prospecto: String? = null

    private val alertDialog: AlertDialog = AlertDialog.Builder(context)
        .setView(binding.root)
        .setTitle(context.getString(R.string.add_medicament))
        .setPositiveButton(context.getString(R.string.accept), null)
        .setNegativeButton(context.getString(R.string.cancel), null)
        .create()

    init {
        _pillboxViewModel = PillboxViewModel.getInstance(context)

        alertDialog.setOnShowListener {
            setupPositiveButton()
        }

        binding.btnSearch.setOnClickListener {
            if (inputCodNacional.text.isNullOrBlank()) {
                return@setOnClickListener
            }

            val searchingToast = Toast.makeText(
                context, context.getString(R.string.searching), Toast.LENGTH_LONG
            ).also { it.show() }

            GlobalScope.launch(Dispatchers.Main) {
                val codNacional = inputCodNacional.text.toString().split(".")[0].trim()

                val medicamento = pillboxViewModel.searchMedicamento(codNacional)

                withContext(Dispatchers.Main) {
                    searchingToast.cancel()
                    if (medicamento == null) {
                        Toast.makeText(
                            context,
                            context.getString(R.string.codNacional_not_found),
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        inputNombre.setText(medicamento.nombre)
                        fichaTecnica = medicamento.fichaTecnica
                        prospecto = medicamento.prospecto
                    }
                }
            }

        }

        binding.btnHelp.setOnClickListener {
            Toast.makeText(context, context.getString(R.string.codNacional_help), Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun validateForm(): Boolean {
        if (inputNombre.text.isNullOrBlank()) {
            Toast.makeText(context, context.getString(R.string.empty_name), Toast.LENGTH_LONG)
                .show()
            return false
        }

        return true
    }

    private fun setupPositiveButton() {
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            if (!validateForm()) {
                return@setOnClickListener
            }

            val nombre = inputNombre.text.toString()
            val codNacional = inputCodNacional.text.toString().split(".")[0].trim()
            val fichaTecnica = fichaTecnica
            val prospecto = prospecto

            alertDialog.dismiss()
            listener.onDataEntered(
                MedicamentoBuilder()
                    .setNombre(nombre)
                    .setCodNacional(codNacional.toInt())
                    .setFichaTecnica(fichaTecnica!!)
                    .setProspecto(prospecto!!)
                    .setFavorito(true)
                    .build()
            )
        }
    }

    fun show() {
        alertDialog.show()
    }
}