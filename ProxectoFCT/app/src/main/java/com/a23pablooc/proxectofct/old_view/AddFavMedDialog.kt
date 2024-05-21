package com.a23pablooc.proxectofct.old_view

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import com.a23pablooc.proxectofct.old_model.Medicamento
import com.a23pablooc.proxectofct.old_viewModel.PillboxViewModel
import com.a23pablooc.proxectofct.R
import com.a23pablooc.proxectofct.databinding.OldDialogAddFavMedBinding
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

    private var _binding: OldDialogAddFavMedBinding? =
        OldDialogAddFavMedBinding.inflate(LayoutInflater.from(context))
    private val binding get() = _binding!!

    private var _pillboxViewModel: PillboxViewModel? = null
    private val pillboxViewModel get() = _pillboxViewModel!!

    private val inputCodNacional: EditText = binding.codNacional
    private val inputNombre: EditText = binding.nombre
    private var numRegistro: String? = null
    private var url: String? = null
    private var fichaTecnica: String? = null
    private var prospecto: String? = null
    private var imagen: ByteArray? = null
    private var laboratorio: String? = null
    private var dosis: String? = null
    private var principiosActivos: List<String>? = null
    private var receta: String? = null

    private val alertDialog: AlertDialog = AlertDialog.Builder(context)
        .setView(binding.root)
        .setTitle(context.getString(R.string.añadir_medicamento))
        .setPositiveButton(context.getString(R.string.aceptar), null)
        .setNegativeButton(context.getString(R.string.cancelar), null)
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
                context, context.getString(R.string.buscando), Toast.LENGTH_LONG
            ).also { it.show() }

            GlobalScope.launch(Dispatchers.Main) {
                val codNacional = inputCodNacional.text.toString().split(".")[0].trim()

                val medicamento = pillboxViewModel.searchMedicamento(codNacional)?.setCodNacional(codNacional.toInt())?.build()

                withContext(Dispatchers.Main) {
                    searchingToast.cancel()
                    if (medicamento == null) {
                        Toast.makeText(
                            context,
                            context.getString(R.string.codigo_nacional_no_encontrado),
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        inputNombre.setText(medicamento.nombre)
                        numRegistro = medicamento.numRegistro
                        url = medicamento.url
                        fichaTecnica = medicamento.fichaTecnica
                        prospecto = medicamento.prospecto
                        imagen = medicamento.imagen
                        laboratorio = medicamento.laboratorio
                        dosis = medicamento.dosis
                        principiosActivos = medicamento.principiosActivos
                        receta = medicamento.receta
                    }
                }
            }

        }

        binding.btnHelp.setOnClickListener {
            Toast.makeText(context, context.getString(R.string.codigo_nacional_ayuda), Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun validateForm(): Boolean {
        if (inputNombre.text.isNullOrBlank()) {
            Toast.makeText(context, context.getString(R.string.sin_nombre), Toast.LENGTH_LONG)
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
            var codNacional = inputCodNacional.text.toString().split(".")[0].trim()
            if (codNacional.isBlank()) {
                codNacional = "-1"
            }
            val fichaTecnica = fichaTecnica
            val prospecto = prospecto

            alertDialog.dismiss()
            listener.onDataEntered(
                Medicamento.Builder()
                    .setNombre(nombre)
                    .setCodNacional(codNacional.toInt())
                    .setNumRegistro(numRegistro ?: "")
                    .setUrl(url ?: "")
                    .setFichaTecnica(fichaTecnica ?: "")
                    .setProspecto(prospecto ?: "")
                    .setImagen(imagen ?: byteArrayOf())
                    .setLaboratorio(laboratorio ?: "")
                    .setDosis(dosis ?: "")
                    .setPrincipiosActivos(principiosActivos ?: listOf())
                    .setReceta(receta ?: "")
                    .setFavorito(true)
                    .build()
            )
        }
    }

    fun show() {
        alertDialog.show()
    }
}